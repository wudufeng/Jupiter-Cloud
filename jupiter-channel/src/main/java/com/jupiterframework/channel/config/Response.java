package com.jupiterframework.channel.config;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;


/**
 * 数据字典映射
 */

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
@lombok.Data
public class Response {
    /** 将源报文设置到map中的key */
    public static final String PAYLOAD_KEY = "_payload";

    /** XML / JSON */
    @XmlAttribute
    private Format format = Format.JSON;

    /** 是否返回源报文 */
    @XmlAttribute
    private boolean payload;

    /** 指定只解析某个结构体作为field */
    @XmlAttribute
    private String indicate = "";

    @XmlElement(name = "field")
    private List<Field> fields;

    private long lastModified;

    @XmlRootElement(name = "field")
    @XmlAccessorType(XmlAccessType.NONE)
    @lombok.Data
    public static class Field {
        /** XPath / JSONPath解析 */
        @XmlAttribute(required = true)
        private String path;

        /** 转换为字典属性名字 */
        @XmlAttribute(required = true)
        private String name;

        @XmlAttribute
        private Class<?> type = java.lang.String.class;

        /** 日期格式, 当type为 java.util.Date时候使用 */
        @XmlAttribute(name = "date-format")
        private String dateFormat;

        /** 值转换或解析 */
        @XmlAttribute
        private String resolver;

        /** 默认值 ,若接口响应返回空，则取此值 */
        @XmlAttribute(name = "default-value")
        private String defaultValue;

        /** 使用表达式计算，将计算值设置为此值,参数为整个map串 */
        private String mvelExpression;
        private String mvelElemment;

        @XmlElement(name = "field")
        private List<Field> fields;

        private Map<String/* 外部值 */, String/* 内部系统值 */> valueMappings;

        @XmlElement(name = "value-mapping", type = ValueMapping.class)
        private List<ValueMapping> valueMapping;

        /** 是否返回源报文仅限在List类型中使用 */
        @XmlAttribute
        private boolean payload;


        @XmlAttribute(name = "mvel-expression")
        public void setExpression(String mvelExpression) {
            if (StringUtils.isNotBlank(mvelExpression) && this.mvelExpression == null) {
                this.mvelExpression = mvelExpression;
            }
        }


        @XmlElement(name = "mvel-expression")
        public void setMvelElemment(String mvel) {
            if (StringUtils.isNotBlank(mvel) && mvelExpression == null) {
                this.mvelExpression = mvel.trim();
            }
        }

        /** 值映射关系 */
        @XmlRootElement(name = "value-mapping")
        @XmlAccessorType(XmlAccessType.NONE)
        @lombok.Data
        public static class ValueMapping {

            @XmlAttribute
            private String inbound;
            @XmlAttribute
            private String outbound;

        }
    }

    public enum Format {
        JSON,
        XML
    }
}
