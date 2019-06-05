<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.entity;

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
public class ${className}DO {
    <#list table.columns as column>
    <#if column.primaryKey>
    @TableId
    </#if>
    @ApiModelProperty(value = "${column.comment!}")
	private ${column.javaType} ${column.fieldName};

    </#list>
}