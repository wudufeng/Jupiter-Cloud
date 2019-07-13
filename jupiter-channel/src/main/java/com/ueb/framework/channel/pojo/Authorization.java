package com.ueb.framework.channel.pojo;

import lombok.Data;


/**
 * 授权信息
 *
 */
@Data
public class Authorization {

    private String url;
    private String appKey;
    private String accessKey;
    private String accessToken;
    private String securetKey;
}
