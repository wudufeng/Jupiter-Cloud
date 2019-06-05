<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service;

import ${basepackage}.entity.${className};
import ${basepackage}.vo.${className}ListRequestVO;


/** ${table.comment}管理 */
public interface ${className}Service {
	/** 新增${table.comment} */
    Long add${className}(${className} ${classNameLower});

    /** 根据ID修改${table.comment} */
    void update${className}(${className} ${classNameLower});

    /** 根据ID删除${table.comment} */
    void delete${className}(Long id);

    /** 根据ID获取${table.comment} */
    ${className} get${className}(Long id);

    /** 分页查询${table.comment} */
    com.ueb.framework.domain.Page<${className}> get${className}List(${className}ListRequestVO ${classNameLower});

}
