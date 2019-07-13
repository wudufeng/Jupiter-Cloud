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
public class AmazonTest {

    @Autowired
    private CommunicationProcessor http;

    Authorization auth = new Authorization(); // 602
    {
        auth.setAccessToken("");
        auth.setAppKey("AUSSFJ9YQUVHC");// Merchant=
        auth.setSecuretKey("6fVGHoJeFrjCJuCvEX65tPfxDlMHSyAhA0u80jJI");
        auth.setAccessKey("AKIAJ3RSTD4J25YX2FBQ");
        auth.setUrl("https://mws.amazonservices.in");
    }


    @Test
    public void testHttpAmazonGetFeedSubmitList() {

        Map<String, Object> params = new HashMap<>();

        List<String> fsis = new ArrayList<>();
        fsis.add("111453018079");

        params.put("feedSubmissionIdList", fsis);

        http.request(new MessageRequest("AMAZON", "GetFeedSubmissionList", auth, params));
    }


    @Test
    public void testHttpAmazonGetFeedSubmitResult() {

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> feedSubmissionId = new HashMap<>(2);
        // feedSubmissionId.put("feedSubmissionId", "53154018075");
        // feedSubmissionId.put("feedSubmissionId", "119377018071");
        // feedSubmissionId.put("feedSubmissionId", "119382018071");
        feedSubmissionId.put("feedSubmissionId", "111453018079");

        params.put("p", feedSubmissionId);

        http.request(new MessageRequest("AMAZON", "GetFeedSubmissionResult", auth, params));
    }


    @Test
    public void testHttpAmazonGetOrder() {

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> feedSubmissionId = new HashMap<>(2);
        feedSubmissionId.put("orderId", "53154018075");
        feedSubmissionId.put("lastUpdateAfter", DateUtils.addDays(new Date(), -3));

        params.put("p", feedSubmissionId);

        http.request(new MessageRequest("AMAZON", "GetOrder", auth, params));
    }


    @Test
    public void testHttpAmazonSubmit() {

        Map<String, Object> params = new HashMap<>();

        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> updateItem = new HashMap<>(3);
        updateItem.put("messageId", "1");
        updateItem.put("platformSku", "2rr2ar8nm0ey3wz7");
        updateItem.put("updateStock", "0");
        datas.add(updateItem);

        Map<String, Object> updateItem2 = new HashMap<>(3);
        updateItem2.put("messageId", "2");
        updateItem2.put("platformSku", "2ww2gn7lv7gg6at8");
        updateItem2.put("updateStock", "0");
        datas.add(updateItem2);

        params.put("datas", datas);
        params.put("p", params);

        http.request(new MessageRequest("AMAZON", "update_stock", auth, params));
    }

}
