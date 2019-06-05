package com.jupiterframework.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


public abstract class FreemarkerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerUtils.class);

    private static final String RESOURCE = "freemarker";

    // Configuration 缓存 Template 实例
    private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);


    private FreemarkerUtils() {
    }

    static {
        cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), RESOURCE);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setTemplateUpdateDelayMilliseconds(10000);

    }


    public static void createTemplateFile(String templateName, String destFile, Object dataModel) {

        Template template = null;

        try {
            template = cfg.getTemplate(templateName, Locale.CHINESE, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException("加载模板文件失败" + templateName, e);
        }

        File file = new File(destFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Writer w = null;
        try {
            w = new FileWriter(file);
            template.process(dataModel, w);
            LOGGER.debug("create file success > {}", file.getAbsolutePath());
        } catch (TemplateException | IOException e) {
            throw new IllegalArgumentException("生成模板文件失败", e);
        } finally {
            if (w != null)
                try {
                    w.close();
                } catch (IOException e) {
                }
        }

    }
}
