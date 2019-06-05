<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.ueb.framework.domain.BasePO;

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
public class ${className} extends BasePO {
	private static final long serialVersionUID = 1L;

	<#list table.columns as column>
	<#if column.primaryKey>
	@TableId
	</#if>
	<#if column.fieldName != "creater" && column.fieldName != "createTime" && column.fieldName != "updater" && column.fieldName != "updateTime" && column.fieldName != "deleted">
	@ApiModelProperty(value = "${column.comment!}")
	private ${column.javaType} ${column.fieldName};
	</#if>
	
	</#list>
}