package com.jupiter.codegen.pojo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;


/**
 * 代码生成
 *
 * @author WUDUFENG
 * @since 2019-08-05
 */
@lombok.Data
@NoArgsConstructor
public class CodeGeneratorVo {

    /** 表名 */
    @ApiModelProperty(value = "表名")
    private String tableName;

    /** 表注释 */
    @ApiModelProperty(value = "表注释")
    private String tableComment;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private Date tableCreateTime;

    /** 包名 */
    @ApiModelProperty(value = "包名")
    private String packageName;

    /** 作者 */
    @ApiModelProperty(value = "作者")
    private String author;

    /** 模块名称 */
    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    /** 表前缀 */
    @ApiModelProperty(value = "表前缀")
    private String tablePrefix;


    public CodeGeneratorVo(String tableName, String tableComment, Date tableCreateTime) {
        super();
        this.tableName = tableName;
        this.tableComment = tableComment;
        this.tableCreateTime = tableCreateTime;
    }

}
