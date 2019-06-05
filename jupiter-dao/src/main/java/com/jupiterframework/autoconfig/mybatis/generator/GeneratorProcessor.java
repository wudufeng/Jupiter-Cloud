package com.jupiterframework.autoconfig.mybatis.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jupiterframework.util.FreemarkerUtils;

import lombok.AllArgsConstructor;


/**
 * 根据指定的数据库ID（表名）生成对应的java文件和mybatis 映射文件
 *
 */
@AllArgsConstructor
public class GeneratorProcessor {
    @Autowired
    private DatabaseMetaDataProvider databaseMetaDataProvider;


    public void executeCreateJava(String packagePath, String trancateTablePrefix, String destDir, String... tableNames) {
        this.execute(packagePath, trancateTablePrefix, destDir, true, false, tableNames);
    }


    public void execute(String packagePath, String trancateTablePrefix, String destDir, boolean java, boolean xml, String... tableNames) {

        List<Table> tables = databaseMetaDataProvider.getTables(tableNames);

        for (Table table : tables) {
            Map<String, Object> dataModel = new HashMap<>();
            if (StringUtils.isNotBlank(trancateTablePrefix)) {
                table.setClassName(table.getClassName().replaceFirst(ConverterUtil.translate(trancateTablePrefix, false), ""));
            }
            dataModel.put("table", table);
            dataModel.put("basepackage", packagePath);

            if (java) {
                FreemarkerUtils.createTemplateFile("entity.ftl",
                    new StringBuilder(destDir).append(File.separatorChar).append("entity").append(File.separatorChar).append(table.getClassName()).append(".java").toString(),
                    dataModel);
                FreemarkerUtils.createTemplateFile("dao.ftl",
                    new StringBuilder(destDir).append(File.separatorChar).append("dao").append(File.separatorChar).append(table.getClassName()).append("Dao.java").toString(),
                    dataModel);
                FreemarkerUtils.createTemplateFile("vo.ftl", new StringBuilder(destDir).append(File.separatorChar).append("vo").append(File.separatorChar)
                    .append(table.getClassName()).append("ListRequestVO.java").toString(), dataModel);
                FreemarkerUtils.createTemplateFile("service.ftl", new StringBuilder(destDir).append(File.separatorChar).append("service").append(File.separatorChar)
                    .append(table.getClassName()).append("Service.java").toString(), dataModel);
                FreemarkerUtils.createTemplateFile("impl.ftl", new StringBuilder(destDir).append(File.separatorChar).append("service/impl").append(File.separatorChar)
                    .append(table.getClassName()).append("ServiceImpl.java").toString(), dataModel);
            }
            if (xml) {
                FreemarkerUtils.createTemplateFile("${className}.xml", new StringBuilder(destDir).append(File.separatorChar).append(table.getClassName()).append(".xml").toString(),
                    dataModel);
            }
        }
    }

}
