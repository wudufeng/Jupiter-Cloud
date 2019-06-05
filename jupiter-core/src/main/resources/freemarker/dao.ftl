<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import ${basepackage}.entity.${className};


public interface ${className}Dao extends BaseMapper<${className}> {

}