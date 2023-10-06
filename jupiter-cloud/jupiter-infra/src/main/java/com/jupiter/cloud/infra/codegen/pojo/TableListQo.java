package com.jupiter.cloud.infra.codegen.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class TableListQo {

    /** 选择数据源*/
    @NotNull
    private Long databaseId;

    /** 表名 */
    private String tableName;
}
