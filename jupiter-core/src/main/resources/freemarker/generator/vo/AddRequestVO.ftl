<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.vo;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * ${table.tableName}${table.comment}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ${className} {

    @ApiModelProperty(value = "分页参数")
    private Pagination page = new Pagination();

    <#list table.columns as column>
	<#if !column.primaryKey>
	<#if column.fieldName != "deleted">
	<#if column.fieldName != "createTime" && column.fieldName != "updateTime">
    @ApiModelProperty(value = "${column.comment!}")
    private ${column.javaType} ${column.fieldName};
    </#if>
    </#if>
    </#if>

    </#list>
}