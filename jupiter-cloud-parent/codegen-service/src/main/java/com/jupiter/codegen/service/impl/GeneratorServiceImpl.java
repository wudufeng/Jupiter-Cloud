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

import com.baomidou.mybatisplus.annotation.TableField;
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
import com.mysql.jdbc.Driver;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static final String PATH = "/template/codegen/";

    @Autowired
    private DatabaseInfoManage databaseInfoManage;


    @Override
    public boolean generateCurdCode(GeneratorConfigQo qo) {
        DatabaseInfo db = databaseInfoManage.get(qo.getDatabaseId());
        if (db == null) {
            return false;
        }

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        this.createGlobalConfig(qo, mpg);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(db.getJdbcUrl());
        dsc.setDbType(com.baomidou.mybatisplus.generator.config.rules.DbType
            .valueOf(com.baomidou.mybatisplus.toolkit.JdbcUtils.getDbType(db.getJdbcUrl()).name()));
        dsc.setDriverName(Driver.class.getName());
        dsc.setUsername(db.getUserName());
        dsc.setPassword(db.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        this.configPackage(qo, mpg);

        // 自定义配置
        this.userDefinitionFile(qo, mpg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(PATH + "entity.java");
        templateConfig.setService(PATH + "manage.java");
        templateConfig.setServiceImpl(PATH + "manageImpl.java");
        templateConfig.setController(PATH + "controller.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        this.createStrategyConfig(qo, mpg);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        log.info("code generate to path : {}", qo.getOutputDir());

        mpg.execute();

        return true;
    }


    private void configPackage(GeneratorConfigQo qo, AutoGenerator mpg) {
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
    }


    private void createGlobalConfig(GeneratorConfigQo qo, AutoGenerator mpg) {
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
        mpg.setGlobalConfig(gc);
    }


    private void createStrategyConfig(GeneratorConfigQo qo, AutoGenerator mpg) {
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
        strategy.setTablePrefix(qo.getTablePrefix() == null ? "" : qo.getTablePrefix());
        mpg.setStrategy(strategy);
    }


    private void userDefinitionFile(GeneratorConfigQo qo, AutoGenerator mpg) {
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(qo.getInitMap());
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        String moduleName = mpg.getPackageInfo().getModuleName();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(PATH + "mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = mpg.getGlobalConfig().getOutputDir() + "/mapper/" + moduleName + "/"
                        + tableInfo.getEntityName() + ".xml";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/index.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = mpg.getGlobalConfig().getOutputDir() + "/web/views/" + moduleName + "/"
                        + tableInfo.getEntityName() + "-index.vue";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/crud.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = mpg.getGlobalConfig().getOutputDir() + "/web/views/" + moduleName + "/"
                        + tableInfo.getEntityName().toLowerCase() + ".vue";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/api.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file = mpg.getGlobalConfig().getOutputDir() + "/web/api/" + moduleName + ".js";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });
        focList.add(new FileOutConfig(PATH + "view/router.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String file =
                        mpg.getGlobalConfig().getOutputDir() + "/web/router/modules/" + moduleName + ".js";
                new File(file).getParentFile().mkdirs();
                return file;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }


    @Override
    public PageResult<CodeGeneratorVo> getTableList(PageQuery<TableListQo> pageQuery) {
        Object object = databaseInfoManage.get(pageQuery.getCondition().getDatabaseId());
        log.debug("{}", object);
        DatabaseInfo db = databaseInfoManage.get(pageQuery.getCondition().getDatabaseId());
        PageResult<CodeGeneratorVo> result = new PageResult<>();
        if (db == null)
            return result;

        String tableName = pageQuery.getCondition().getTableName();
        if (tableName == null)
            tableName = "";

        List<CodeGeneratorVo> records = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(db.getJdbcUrl(), db.getUserName(), db.getPassword());
            String condition =
                    "from information_schema.tables where table_schema = (select database()) and table_name like ? order by create_time desc";

            // 总记录
            stmt = conn.prepareStatement("select count(*) " + condition);
            stmt.setString(1, String.format("%%%s%%", tableName));
            rs = stmt.executeQuery();
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

        } catch (SQLException e) {
            throw new IllegalArgumentException("无法连接数据库!", e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }

        return result;
    }

}
