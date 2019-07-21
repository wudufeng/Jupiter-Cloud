package com.jupiterframework.mybatis.generator;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.model.GenericPo;
import com.jupiterframework.util.ZipUtil;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import com.zaxxer.hikari.HikariDataSource;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;


/**
 * 根据表生成CRUD代码
 *
 */
@Api(tags = "根据表生成CRUD代码")
@MicroService
@Slf4j
public class CodeGeneratorController {

	@Autowired
	private HikariDataSource dataSource;

	@RequestMapping(value = "/code/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.GET)
	public void generator(@RequestParam(name = "dbType", defaultValue = "MYSQL") DbType dbType,
			@RequestParam String parentPackage, @RequestParam String module,
			@RequestParam(value = "trancateTablePrefix", required = false) String trancatePrefix,
			@RequestParam(value = "tableNames", required = false) String[] tableNames,
			@RequestParam(value = "outputDir", required = false) String outputDir,
			@RequestParam(value = "fileOverride", defaultValue = "false") boolean fileOverride,
			@RequestParam(value = "download", required = false, defaultValue = "false") boolean download,
			HttpServletResponse response) {

		// 代码生成器
		if (org.apache.commons.lang3.StringUtils.isBlank(outputDir))
			outputDir = System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID().toString();

		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setFileOverride(fileOverride);
		gc.setOutputDir(outputDir);
		gc.setAuthor(System.getProperty("user.name"));
		gc.setActiveRecord(false);
		gc.setOpen(System.getProperty("os.name").contains("Windows"));
		gc.setEnableCache(false);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		gc.setMapperName("%sDao");
		gc.setXmlName("%s");
		gc.setServiceName("%sManage");
		gc.setServiceImplName("%sManageImpl");
		gc.setControllerName("%sController");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setUrl(dataSource.getJdbcUrl());
		dsc.setDriverName(dataSource.getDriverClassName());
		dsc.setUsername(dataSource.getUsername());
		dsc.setPassword(dataSource.getPassword());
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(parentPackage);
		pc.setModuleName(module);
		pc.setService("manage");
		pc.setServiceImpl("manage.impl");
		pc.setMapper("dao");
		pc.setXml("dao.xml");
		pc.setController("web");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				this.setMap(new HashMap<>());
			}
		};

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		String xmlTemplatePath = "/freemarker/generator/mapper.xml";
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(xmlTemplatePath + ".ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				String file =
						gc.getOutputDir() + "/mapper/" + pc.getModuleName() + "/" + tableInfo.getEntityName() + ".xml";
				new File(file).getParentFile().mkdirs();
				return file;
			}
		});
		focList.add(new FileOutConfig("/freemarker/generator/pageList.vue.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				String file = gc.getOutputDir() + "/web/views/" + pc.getModuleName() + "/" + tableInfo.getEntityName()
						+ ".vue";
				new File(file).getParentFile().mkdirs();
				return file;
			}
		});
		focList.add(new FileOutConfig("/freemarker/generator/api.js.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				String file = gc.getOutputDir() + "/web/api/" + tableInfo.getEntityName() + ".js";
				new File(file).getParentFile().mkdirs();
				return file;
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();
		// // 配置自定义输出模板
		templateConfig.setEntity("/freemarker/generator/entity.java");
		templateConfig.setService("/freemarker/generator/manage.java");
		templateConfig.setServiceImpl("/freemarker/generator/manageImpl.java");
		templateConfig.setController("/freemarker/generator/controller.java");
		templateConfig.setXml(xmlTemplatePath);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setSuperEntityClass(GenericPo.class.getName());
		List<String> superEntityFields = new ArrayList<>();
		for (Field f : GenericPo.class.getDeclaredFields()) {
			superEntityFields.add(f.getName());
			TableField tf = f.getAnnotation(TableField.class);
			if (tf != null)
				superEntityFields.add(tf.value());
		}
		strategy.setSuperEntityColumns(superEntityFields.toArray(new String[superEntityFields.size()]));
		strategy.setSuperServiceClass(GenericManage.class.getName());
		strategy.setSuperServiceImplClass(GenericManageImpl.class.getName());
		strategy.setSuperMapperClass(GenericDao.class.getName());
		strategy.setSuperControllerClass(GenericController.class.getName());

		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		strategy.setInclude(tableNames);
		strategy.setEntityBooleanColumnRemoveIsPrefix(true);
		strategy.entityTableFieldAnnotationEnable(false);
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setTablePrefix(trancatePrefix == null ? "" : trancatePrefix.trim());

		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());

		log.info("code generate to path : {}", outputDir);

		mpg.execute();

		if (download) {
			// response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment;filename=code-generate-" + module + ".zip");
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			try {
				ZipUtil.toZip(outputDir, response.getOutputStream(), true);
			} catch (Exception e) {
				log.warn("", e);
			}
		}
	}

}
