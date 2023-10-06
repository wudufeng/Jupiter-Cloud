package com.jupiter.cloud.infra.codegen.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;


/**
 * @author jupiter
 */
@Data
public class GeneratorConfigQo {

    /**选择数据源*/
    private Long databaseId;

    /** 包名 */
    @NotBlank
    private String packageName;

    /** 作者 */
    private String author;

    /** 模块名称 */
    @NotBlank
    private String moduleName;

    /** 表前缀 */
    private String tablePrefix;

    /** 表名 */
    private String[] tableName;

    private String outputDir;

    private Map<String, Object> initMap;
}
