package com.ueb.framework.channel.communication.support;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ueb.framework.channel.communication.ClientHandler;
import com.ueb.framework.channel.config.Channel;
import com.ueb.framework.channel.config.Request;
import com.ueb.framework.channel.config.Request.Parameter;
import com.ueb.framework.channel.config.RequestMethod;
import com.ueb.framework.channel.config.Service;
import com.ueb.framework.channel.pojo.Authorization;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class HttpClientHandler implements ClientHandler {

    @Override
    public byte[] request(Service svccfg, Authorization auth, Request requestParams) throws IOException {

        Channel chlcfg = svccfg.getChannel();

        RequestConfig config = RequestConfig.custom().setConnectTimeout(chlcfg.getConnectTimeout()).setSocketTimeout(svccfg.getSocketTimeout()).build();
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpRequestBase req = this.createHttpRequest(auth, requestParams, chlcfg, svccfg);
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(req);
        org.apache.http.StatusLine status = response.getStatusLine();
        if (status != null) {
            if (status.getStatusCode() != HttpStatus.SC_OK) {
                throw new RemoteAccessException(String.format("%d:%s", status.getStatusCode(), status.getReasonPhrase()));
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                byte[] resp = EntityUtils.toByteArray(entity);
                if (log.isDebugEnabled()) {
                    log.debug("receive response : {}", new String(resp, StandardCharsets.UTF_8));
                }
                return resp;
            }
        }

        return new byte[0];
    }


    private HttpRequestBase createHttpRequest(Authorization auth, Request requestParams, Channel chlcfg, Service svccfg) {
        HttpRequestBase httpReq = svccfg.getRequestMethod() == RequestMethod.HTTP_POST ? new HttpPost() : new HttpGet();
        String url = auth.getUrl() + svccfg.getPath();

        List<NameValuePair> nvps = new ArrayList<>();

        this.pack(httpReq, requestParams, nvps);

        // 添加签名参数
        if (StringUtils.hasText(requestParams.getSignValue())) {
            if (chlcfg.getAuthorized().isHeader())
                httpReq.addHeader(chlcfg.getAuthorized().getParamName(), requestParams.getSignValue());
            else
                nvps.add(new BasicNameValuePair(chlcfg.getAuthorized().getParamName(), requestParams.getSignValue()));
        }

        if (svccfg.getRequestMethod() == RequestMethod.HTTP_POST) {
            if (((HttpPost) httpReq).getEntity() == null) {
                ((HttpPost) httpReq).setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
                httpReq.setURI(URI.create(url));
            } else {
                httpReq.setURI(URI.create(url + "?" + URLEncodedUtils.format(nvps, StandardCharsets.UTF_8)));
            }
        } else if (svccfg.getRequestMethod() == RequestMethod.HTTP_GET) {
            httpReq.setURI(URI.create(url + "?" + URLEncodedUtils.format(nvps, StandardCharsets.UTF_8)));
        } else
            throw new IllegalArgumentException("不支持的请求方法" + svccfg.getRequestMethod());

        return httpReq;
    }


    private void pack(HttpRequestBase result, Request s, List<NameValuePair> nvps) {
        for (Parameter p : s.getParameters()) {

            switch (p.getRequestType()) {
            case QUERY_PARAM:
                nvps.add(new BasicNameValuePair(p.getKey(), p.getValue()));
                break;
            case HEADER:
                result.addHeader(p.getKey(), p.getValue());
                break;
            case BODY:
                ((HttpPost) result).setEntity(new StringEntity(p.getValue().trim(), StandardCharsets.UTF_8));
                break;
            default:
                break;
            }

        }

    }


    @Override
    public List<RequestMethod> getRequestMode() {

        return Arrays.asList(RequestMethod.HTTP_GET, RequestMethod.HTTP_POST);
    }

}
