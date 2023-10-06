package com.jupiter.cloud.infra.codegen.pojo;

import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 代码生成
 *
 * @author jupiter
 * @since 2019-08-05
 */
@lombok.Data
@NoArgsConstructor
public class CodeGeneratorVo {

    /** 表名 */
    private String tableName;

    /** 表注释 */
    private String tableComment;

    /** 创建时间 */
    private Date tableCreateTime;

    /** 包名 */
    private String packageName;

    /** 作者 */
    private String author;

    /** 模块名称 */
    private String moduleName;

    /** 表前缀 */
    private String tablePrefix;


    public CodeGeneratorVo(String tableName, String tableComment, Date tableCreateTime) {
        super();
        this.tableName = tableName;
        this.tableComment = tableComment;
        this.tableCreateTime = tableCreateTime;
    }

}
