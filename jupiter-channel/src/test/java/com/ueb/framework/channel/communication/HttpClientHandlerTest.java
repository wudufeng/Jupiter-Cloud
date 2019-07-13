package com.ueb.framework.channel.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ueb.framework.channel.ChannelApplication;
import com.ueb.framework.channel.core.CommunicationProcessor;
import com.ueb.framework.channel.pojo.Authorization;
import com.ueb.framework.channel.pojo.MessageRequest;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChannelApplication.class)
public class HttpClientHandlerTest {

    @Autowired
    private CommunicationProcessor http;


    @Test
    public void testHttpFormData() {

        Authorization auth = new Authorization();
        auth.setAccessToken("50000000a39M827jENYvygA2oWiJpDxweYeAqePyvHhILo4E4Hc17953e03gS");// 42
        auth.setAppKey("101909");
        auth.setSecuretKey("BalhqadjOh7yne8rmP31BD4voIjXexUW");
        auth.setUrl("https://api.lazada.co.id/rest");

        Map<String, Object> params = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> updateStock = new HashMap<>(2);
        updateStock.put("platformSku", "2ZC2XV7NN6UF4XB5");// 168177.02
        updateStock.put("updateStock", 40);
        list.add(updateStock);
        params.put("p", list);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        http.request(new MessageRequest("LAZADA", "update_stock", auth, params));
    }


    @Test
    public void testHttpJSON() {

        Authorization auth = new Authorization(); // 1460
        auth.setAccessToken("841887");
        auth.setAppKey("116705955");
        auth.setSecuretKey("be82e8b8b23aaad79c82ac0b2008b153608fb03c80ceaf153e161d9a51911f92");

        Map<String, Object> params = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> updateStock = new HashMap<>(2);
        updateStock.put("platformProductId", "2267170889");
        updateStock.put("platformSku", "2267170889");// 251570.04
        updateStock.put("updateStock", 0);
        list.add(updateStock);
        params.put("p", list);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        http.request(new MessageRequest("SHOPEE", "update_stock", auth, params));
    }


    @Test
    public void testHttpJSONGetOrder() {

        Authorization auth = new Authorization(); // 1460
        auth.setAccessToken("841887");
        auth.setAppKey("116705955");
        auth.setSecuretKey("be82e8b8b23aaad79c82ac0b2008b153608fb03c80ceaf153e161d9a51911f92");

        Map<String, Object> params = new HashMap<>();

        List<String> list = new ArrayList<>();
        list.add("19011523565GFGA");
        list.add("190117143453D70");
        params.put("p", list);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        http.request(new MessageRequest("SHOPEE", "GetOrder", auth, params));
    }


    @Test
    public void testHttpUrlParam() {
    }


    @Test
    public void testHttpAmazonGetFeedSubmitList() {

        Authorization auth = new Authorization(); // 672
        auth.setAccessToken("");
        auth.setAppKey("A1J3Y18POFUR7L");// Merchant=
        auth.setSecuretKey("33Ll/RvEPWS2EFVnO8RcUyxFQYxc5vRBdRrELcCi");
        auth.setAccessKey("AKIAJRVYZCJILZK4YP4A");
        auth.setUrl("https://mws.amazonservices.in");

        Map<String, Object> params = new HashMap<>();

        List<String> fsis = new ArrayList<>();
        fsis.add("120156018073");
        fsis.add("116369018062");
        fsis.add("116368018062");
        fsis.add("116367018062");

        params.put("feedSubmissionIdList", fsis);

        http.request(new MessageRequest("AMAZON", "GetFeedSubmissionList", auth, params));
    }


    @Test
    public void testHttpAmazonGetFeedSubmitResult() {

        Authorization auth = new Authorization(); // 672
        auth.setAccessToken("");
        auth.setAppKey("A1J3Y18POFUR7L");// Merchant=
        auth.setSecuretKey("33Ll/RvEPWS2EFVnO8RcUyxFQYxc5vRBdRrELcCi");
        auth.setAccessKey("AKIAJRVYZCJILZK4YP4A");
        auth.setUrl("https://mws.amazonservices.in");

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> feedSubmissionId = new HashMap<>(2);
        // feedSubmissionId.put("feedSubmissionId", "53154018075");
        // feedSubmissionId.put("feedSubmissionId", "119377018071");
        // feedSubmissionId.put("feedSubmissionId", "119382018071");
        feedSubmissionId.put("feedSubmissionId", "116368018062");

        params.put("p", feedSubmissionId);

        http.request(new MessageRequest("AMAZON", "GetFeedSubmissionResult", auth, params));
    }


    @Test
    public void testHttpAmazonGetOrder() {

        Authorization auth = new Authorization(); // 672
        auth.setAccessToken("");
        auth.setAppKey("A1J3Y18POFUR7L");// Merchant=
        auth.setSecuretKey("33Ll/RvEPWS2EFVnO8RcUyxFQYxc5vRBdRrELcCi");
        auth.setAccessKey("AKIAJRVYZCJILZK4YP4A");
        auth.setUrl("https://mws.amazonservices.in");

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> feedSubmissionId = new HashMap<>(2);
        feedSubmissionId.put("orderId", "53154018075");
        feedSubmissionId.put("lastUpdateAfter", DateUtils.addDays(new Date(), -3));

        params.put("p", feedSubmissionId);

        http.request(new MessageRequest("AMAZON", "GetOrder", auth, params));
    }


    @Test
    public void testHttpAmazonSubmit() {

        Authorization auth = new Authorization(); // 672
        auth.setAccessToken("");
        auth.setAppKey("A1J3Y18POFUR7L");// Merchant=
        auth.setSecuretKey("33Ll/RvEPWS2EFVnO8RcUyxFQYxc5vRBdRrELcCi");
        auth.setAccessKey("AKIAJRVYZCJILZK4YP4A");
        auth.setUrl("https://mws.amazonservices.in");

        Map<String, Object> params = new HashMap<>();

        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> updateItem = new HashMap<>(3);
        updateItem.put("messageId", "1");
        updateItem.put("platformSku", "2pu7js0qc0rq9wm7D01");
        updateItem.put("updateStock", "0");
        datas.add(updateItem);

        params.put("datas", datas);
        params.put("p", params);

        http.request(new MessageRequest("AMAZON", "update_stock", auth, params));
    }

}
