package com.ueb.framework.channel.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;


/**
 * 数据字典映射
 */

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.NONE)
@lombok.Data
public class Request {

    /** 签名值 */
    private String signValue;

    @XmlElement(name = "parameter")
    private List<Parameter> parameters;

    @XmlRootElement(name = "parameter")
    @XmlAccessorType(XmlAccessType.NONE)
    @lombok.Data
    public static class Parameter {

        /** 字典属性名字 */
        @XmlAttribute
        private String key;

        /** 字典属性值 */
        private String value;


        @XmlAttribute(name = "value")
        public void setValue(String value) {
            this.value = value;
        }


        @XmlElement(name = "value")
        public void setValueText(Value text) {
            if (text != null)
                this.value = text.getText().trim();

        }

        /** 值转换或解析 */
        @XmlAttribute
        private String resolver;

        /** 是否参与签名运算 */
        @XmlAttribute
        private boolean sign = true;

        @XmlAttribute(name = "request-type")
        private RequestTypeEnum requestType = RequestTypeEnum.QUERY_PARAM;

        @XmlRootElement(name = "value")
        @XmlAccessorType(XmlAccessType.NONE)
        @Data
        public static class Value {
            @XmlValue
            private String text;
        }
    }

    public enum RequestTypeEnum {
        HEADER,
        QUERY_PARAM,
        BODY
    }
}
