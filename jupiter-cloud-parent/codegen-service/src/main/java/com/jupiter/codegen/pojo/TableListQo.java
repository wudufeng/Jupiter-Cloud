package com.jupiter.codegen.pojo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class TableListQo {

    @ApiModelProperty(value = "选择数据源")
    @NotNull
    private Long databaseId;

    /** 表名 */
    @ApiModelProperty(value = "表名")
    private String tableName;
}
