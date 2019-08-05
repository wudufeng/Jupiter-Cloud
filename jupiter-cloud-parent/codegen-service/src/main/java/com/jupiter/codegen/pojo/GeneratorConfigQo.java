package com.jupiter.codegen.pojo;

import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GeneratorConfigQo {

    @ApiModelProperty(value = "选择数据源")
    private Long databaseId;

    /** 包名 */
    @NotBlank
    @ApiModelProperty(value = "包名")
    private String packageName;

    /** 作者 */
    @ApiModelProperty(value = "作者")
    private String author;

    /** 模块名称 */
    @NotBlank
    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    /** 表前缀 */
    @ApiModelProperty(value = "表前缀")
    private String tablePrefix;

    /** 表名 */
    @ApiModelProperty(value = "表名")
    private String[] tableName;

    @ApiModelProperty(value = "生成的目录")
    private String outputDir;

    private Map<String, Object> initMap;
}
