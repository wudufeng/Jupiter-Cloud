package com.jupiterframework.channel.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class XmlUtils {
    private static final ConcurrentHashMap<Class<?>, JAXBContext> JAXBCONTEXT_CACHE = new ConcurrentHashMap<>(16);

    private XmlUtils() {
    }


    private static JAXBContext getJAXBContext(Class<?> clazz) {
        JAXBContext ctx = JAXBCONTEXT_CACHE.get(clazz);
        if (ctx == null)
            try {
                ctx = JAXBContext.newInstance(clazz);
                JAXBCONTEXT_CACHE.put(clazz, ctx);
            } catch (JAXBException e) {
                throw new IllegalArgumentException("无法转换文件对象" + clazz.getName(), e);
            }

        return ctx;
    }


    @SuppressWarnings("unchecked")
    public static <D> D parse(File xmlFilePath, Class<D> clazz) {
        java.io.InputStream inputStream = null;
        try {
            final JAXBContext context = getJAXBContext(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFilePath.getPath());
            return (D) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("解析xml文件" + xmlFilePath.getAbsolutePath() + "错误!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static <D> D parse(InputStream xml, Class<D> clazz) throws JAXBException {

        final JAXBContext context = getJAXBContext(clazz);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (D) unmarshaller.unmarshal(xml);
    }


    @SuppressWarnings("unchecked")
    public static <D> D parse(String xml, Class<D> clazz) {

        try {
            final JAXBContext context = getJAXBContext(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();

            return (D) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            log.error("解析xml [{}] 错误!", xml);
            throw new IllegalArgumentException("解析xml错误!", e);
        }

    }


    public static XMLConfiguration readxml(byte[] bytes) {
        try {
            XMLConfiguration xmlConf = new XMLConfiguration();
            FileHandler fileHandler = new FileHandler(xmlConf);
            fileHandler.load(new ByteArrayInputStream(bytes));
            return xmlConf;
        } catch (ConfigurationException e) {
            throw new IllegalArgumentException("非法的xml文本!", e);
        }
    }

}
