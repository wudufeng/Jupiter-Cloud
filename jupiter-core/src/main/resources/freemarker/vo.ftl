<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.vo;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.annotations.TableId;

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
public class ${className}ListRequestVO {

    @ApiModelProperty(value = "分页参数")
    private Pagination page = new Pagination();

    <#list table.columns as column>
    <#if column.primaryKey>
	@TableId
	</#if>
	<#if column.fieldName != "deleted">
    @ApiModelProperty(value = "${column.comment!}")
    private ${column.javaType}<#if column.fieldName == "createTime" || column.fieldName == "updateTime">[]</#if> ${column.fieldName};
    </#if>

    </#list>
}