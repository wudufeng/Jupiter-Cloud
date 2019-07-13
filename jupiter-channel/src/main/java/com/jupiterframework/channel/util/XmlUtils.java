package com.jupiterframework.channel.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class XmlUtils {
    private XmlUtils() {
    }


    @SuppressWarnings("unchecked")
    public static <D> D parse(File xmlFilePath, Class<D> clazz) {

        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();

            return (D) unmarshaller.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFilePath.getPath()));
        } catch (JAXBException e) {
            throw new IllegalArgumentException("解析xml文件" + xmlFilePath.getAbsolutePath() + "错误!", e);
        }
    }


    @SuppressWarnings("unchecked")
    public static <D> D parse(String xml, Class<D> clazz) {

        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();

            return (D) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            log.error("解析xml [{}] 错误!", xml);
            throw new IllegalArgumentException("解析xml错误!", e);
        }

    }


    public static XMLConfiguration readxml(byte[] bytes) {
        XMLConfiguration xmlcfg = new XMLConfiguration();
        try {
            xmlcfg.load(new ByteArrayInputStream(bytes));
        } catch (ConfigurationException e) {
            throw new IllegalArgumentException("非法的xml文本!", e);
        }
        return xmlcfg;
    }

}
