package com.jupiterframework.channel.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class FreemarkerUtils {

	private static final String RESOURCE = "channel";

	// Configuration 缓存 Template 实例
	private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

	static {

		// 增加自定义函数，支持Date转换为时间戳
		TemplateMethodModelEx methodModel = new TimestampTemplateMethodModel();

		Map<String, Object> variables = new HashMap<>(1);
		variables.put("time_long", methodModel);
		try {
			cfg.setAllSharedVariables(new SimpleHash(variables, cfg.getObjectWrapper()));
		} catch (TemplateModelException e) {
			log.warn("", e);
		}

		cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), RESOURCE);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setNumberFormat("#");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setTemplateUpdateDelayMilliseconds(10000);
	}

	private FreemarkerUtils() {
	}

	public static String createTemplate(String templateName, Object dataModel) {

		Template template = null;

		try {
			template = cfg.getTemplate(templateName, Locale.getDefault(), "UTF-8");
		} catch (IOException e) {
			throw new IllegalArgumentException("加载模板文件失败" + templateName, e);
		}

		try (Writer w = new StringWriter();) {
			template.process(dataModel, w);
			return w.toString();
		} catch (TemplateException | IOException e) {
			throw new IllegalArgumentException("生成模板数据失败", e);
		}

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
			log.debug("create file success > {}", file.getAbsolutePath());
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
