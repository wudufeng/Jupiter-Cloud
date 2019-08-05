package com.jupiter.codegen.service.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

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
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.jupiter.codegen.entity.DatabaseInfo;
import com.jupiter.codegen.manage.DatabaseInfoManage;
import com.jupiter.codegen.pojo.CodeGeneratorVo;
import com.jupiter.codegen.pojo.GeneratorConfigQo;
import com.jupiter.codegen.pojo.TableListQo;
import com.jupiter.codegen.service.GeneratorService;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.model.GenericPo;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.util.StringUtils;
import com.jupiterframework.web.GenericController;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static final String PATH = "/template/codegen/";

    @Autowired
    private DatabaseInfoManage databaseInfoManage;


    @Override
    public void generateCurdCode(GeneratorConfigQo qo) {
        DatabaseInfo db = databaseInfoManage.selectById(qo.getDatabaseId());
        if (db == null)
            return;

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = this.createGlobalConfig(qo);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(db.getJdbcUrl());
        dsc.setUsername(db.getUserName());
        dsc.setPassword(db.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(qo.getPackageName());
        pc.setEntity("entity");
        pc.setModuleName(qo.getModuleName());
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
                this.setMap(qo.getInitMap());
            }
        };

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        userDefinitionFile(gc, pc, focList);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // // 配置自定义输出模板
        templateConfig.setEntity(PATH + "entity.java");
        templateConfig.setService(PATH + "manage.java");
        templateConfig.setServiceImpl(PATH + "manageImpl.java");
        templateConfig.setController(PATH + "controller.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = this.createStrategyConfig(qo);

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        log.info("code generate to path : {}", qo.getOutputDir());

        mpg.execute();
    }


    private GlobalConfig createGlobalConfig(GeneratorConfigQo qo) {
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setFileOverride(true);
        gc.setOutputDir(qo.getOutputDir());
        gc.setAuthor(StringUtils.isBlank(qo.getAuthor()) ? System.getProperty("user.name") : qo.getAuthor());
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
        return gc;
    }


    private StrategyConfig createStrategyConfig(GeneratorConfigQo qo) {
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
        strategy.setInclude(qo.getTableName());
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.entityTableFieldAnnotationEnable(false);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(qo.getTablePrefix());
        return strategy;
    }


    private void userDefinitionFile(GlobalConfig gc, PackageConfig pc, List<FileOutConfig> focList) {
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(PATH + "mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = gc.getOutputDir() + "/mapper/" + pc.getModuleName() + "/"
                        + tableInfo.getEntityName() + ".xml";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/pageList.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = gc.getOutputDir() + "/web/views/" + pc.getModuleName() + "/"
                        + tableInfo.getEntityName() + "List.vue";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/api.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = gc.getOutputDir() + "/web/api/" + pc.getModuleName() + ".js";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/router.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = gc.getOutputDir() + "/web/router/modules/" + pc.getModuleName() + ".js";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
    }


    @Override
    public PageResult<CodeGeneratorVo> getTableList(PageQuery<TableListQo> pageQuery) {
        Object object = databaseInfoManage.selectById(pageQuery.getCondition().getDatabaseId());
        log.debug("{}", object);
        DatabaseInfo db = databaseInfoManage.selectById(pageQuery.getCondition().getDatabaseId());
        PageResult<CodeGeneratorVo> result = new PageResult<>();
        if (db == null)
            return result;

        String tableName = pageQuery.getCondition().getTableName();
        if (tableName == null)
            tableName = "";

        List<CodeGeneratorVo> records = new ArrayList<>();
        try {
            Connection conn =
                    DriverManager.getConnection(db.getJdbcUrl(), db.getUserName(), db.getPassword());
            String condition =
                    "from information_schema.tables where table_schema = (select database()) and table_name like ? order by create_time desc";

            // 总记录
            PreparedStatement stmt = conn.prepareStatement("select count(*) " + condition);
            stmt.setString(1, String.format("%%%s%%", tableName));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            result.setTotal(rs.getLong(1));
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);

            // 明细
            stmt = conn.prepareStatement(
                "select table_name, table_comment, create_time, engine " + condition + " limit ?, ?");
            stmt.setString(1, String.format("%%%s%%", tableName));
            stmt.setInt(2, (pageQuery.getCurrent() - 1) * pageQuery.getSize());
            stmt.setInt(3, pageQuery.getSize());
            rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(new CodeGeneratorVo(rs.getString(1), rs.getString(2), rs.getDate(3)));
            }
            result.setRecords(records);

            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);

        } catch (SQLException e) {
            throw new IllegalArgumentException("无法连接数据库!", e);
        }

        return result;
    }

}
