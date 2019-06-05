package com.jupiterframework.autoconfig.mybatis.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zaxxer.hikari.HikariDataSource;


/**
 * 表生成java类
 *
 */
public class GeneratorController {
	private GeneratorProcessor generatorProcessor;

	@Autowired
	private HikariDataSource dataSource;

	public GeneratorController(GeneratorProcessor generatorProcessor) {
		this.generatorProcessor = generatorProcessor;
	}

	@GetMapping("/mybatis/generator")
	public void generatorJavaModule(@RequestParam("packageName") String packageName,
			@RequestParam(value = "trancateTablePrefix", required = false) String trancateTablePrefix,
			@RequestParam(value = "tableNames", required = false) String[] tableNames, HttpServletResponse response) {

		String destPath = System.getenv("temp") + File.separatorChar + UUID.randomUUID().toString();
		generatorProcessor.executeCreateJava(packageName, trancateTablePrefix, destPath, tableNames);

		try {
			Runtime.getRuntime().exec("cmd.exe /c start " + destPath);
		} catch (Exception e) {

		}

	}

	// @ApiOperation("生成java module")
	// @PostMapping("/mybatis/generator2")
	public void generator(@RequestParam("packageName") String packageName,
			@RequestParam(value = "trancateTablePrefix", required = false) String trancateTablePrefix,
			@RequestParam(value = "tableNames", required = false) String[] tableNames, HttpServletResponse response) {

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		String outputDir = System.getenv("temp") + File.separatorChar + UUID.randomUUID().toString();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(outputDir);
		gc.setAuthor("AutoGenerator");
		gc.setOpen(true);
		gc.setEnableCache(false);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(dataSource.getJdbcUrl());
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername(dataSource.getUsername());
		dsc.setPassword(dataSource.getPassword());
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(packageName.substring(packageName.lastIndexOf('.') + 1, packageName.length()));
		pc.setParent(packageName.substring(0, packageName.lastIndexOf('.')));
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		// 如果模板引擎是 freemarker
		String templatePath = "/templates/mapper.xml.ftl";

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				String file = gc.getOutputDir() + "/mapper/" + pc.getModuleName() + "/" + tableInfo.getEntityName()
						+ "Mapper.xml";
				new File(file).getParentFile().mkdirs();
				return file;
			}
		});

		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 配置模板
		// TemplateConfig templateConfig = new TemplateConfig();

		// // 配置自定义输出模板
		// templateConfig.setEntity( );
		// templateConfig.setService();
		// templateConfig.setController();

		// templateConfig.setXml(null);
		// mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		// strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
		strategy.setInclude(tableNames);
		// strategy.setSuperEntityColumns("id");
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setTablePrefix(trancateTablePrefix == null ? "" : trancateTablePrefix.trim());
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();

	}
}
