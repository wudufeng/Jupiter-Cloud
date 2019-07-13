package com.ueb.framework.channel.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ueb.framework.channel.ChannelApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChannelApplication.class)
public class ChannelPropertiesTest {
    @Autowired
    private ChannelProperties properties;


    @Test
    public void testPrint() {
        System.out.println("测试开始");
        // 简单验证结果集是否正确
        System.out.println("========" + properties);

    }
}
