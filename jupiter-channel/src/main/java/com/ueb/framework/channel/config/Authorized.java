package com.ueb.framework.channel.config;

/**
 * 授权签名配置
 *
 */
@lombok.Data
public class Authorized {
    private String signatureAlgorith = "";
    private String paramName = "";
    /** 是否将签名参数设置到header */
    private boolean header = false;
    /** 需要忽略的属性签名 */
    private String[] ignoreProperties = new String[] {};
    /**
     * 签名串的前缀<br/>
     * 如果存在@URL@，则使用完整url替换，<br/>
     * 如果存在@PATH@，则使用service配置的path替换 ，<br/>
     * 存在@HOST@，则替换为new URI（“serviceUrl”）.getHost()
     */
    private String prefix;
    private String pairs = ""; // key=value的等号
    private String split = ""; // key=value&key2=val2的&符号
    private boolean upperCase = false;
    private boolean encodeValue = false;

    /** 加密后的字节数字转换成字符串的方式 */
    private ByteConvertEnum byteConvert = ByteConvertEnum.HEX;

    public enum ByteConvertEnum {
        HEX,
        BASE64
    }

}
