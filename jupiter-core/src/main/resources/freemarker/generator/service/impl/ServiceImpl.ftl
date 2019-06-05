<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ueb.framework.util.BeanUtil;
import com.ueb.framework.util.DateUtil;
import com.ueb.framework.web.annotation.MicroService;
import ${basepackage}.entity.${className};
import ${basepackage}.vo.${className}ListRequestVO;
import ${basepackage}.dao.${className}Dao;
import ${basepackage}.service.${className}Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = { "${table.comment}管理" })
@MicroService
public class ${className}ServiceImpl implements ${className}Service {

    @Resource
    private ${className}Dao ${classNameLower}Dao;


    @ApiOperation("添加${table.comment},返回主键")
    @PostMapping("/add${className}")
    public Long add${className}(@RequestBody ${className} ${classNameLower}) {
        ${classNameLower}.setDefaultValue();
        ${classNameLower}Dao.insert(${classNameLower});
        return ${classNameLower}.getId();
    }


    @ApiOperation("根据ID修改${table.comment}")
    @PostMapping("/update${className}")
    public void update${className}(@RequestBody ${className} ${classNameLower}) {
        ${classNameLower}.setUpdater(com.ueb.stars.util.SessionUtil.getUserId());
        ${classNameLower}.setUpdateTime(DateUtil.currentTime());
        ${classNameLower}Dao.updateById(${classNameLower});
    }


    @ApiOperation("根据ID删除${table.comment}")
    @PostMapping("/remove${className}")
    public void remove${className}(Long id) {
        ${className} ${classNameLower} = new ${className}();
        ${classNameLower}.setUpdater(com.ueb.stars.util.SessionUtil.getUserId());
        ${classNameLower}.setUpdateTime(DateUtil.currentTime());
        ${classNameLower}.setDeleted(Boolean.TRUE);
        ${classNameLower}.setId(id);
        ${classNameLower}Dao.updateById(${classNameLower});
    }


    @ApiOperation("根据ID获取${table.comment}")
    @PostMapping("/get${className}")
    public ${className} get${className}(@RequestParam(name = "id") Long id) {
        return ${classNameLower}Dao.selectById(id);
    }


    @ApiOperation("分页查询${table.comment}")
    @PostMapping("/get${className}Page")
    public com.ueb.framework.domain.Page<${className}> get${className}List(@RequestBody ${className}ListRequestVO ${classNameLower}) {

        Wrapper<${className}> w = new EntityWrapper<>();
        w.like(${classNameLower}.getCreater() != null, "creater", "" + ${classNameLower}.getCreater(), SqlLike.DEFAULT);
        w.like(${classNameLower}.getUpdater() != null, "updater", "" + ${classNameLower}.getUpdater(), SqlLike.DEFAULT);

        if (${classNameLower}.getCreateTime() != null)
            w.between("createTime", ${classNameLower}.getCreateTime()[0], ${classNameLower}.getCreateTime()[1]);
        if (${classNameLower}.getUpdateTime() != null)
            w.between("updateTime", ${classNameLower}.getUpdateTime()[0], ${classNameLower}.getUpdateTime()[1]);

        List<${className}> list = ${classNameLower}Dao.selectPage(${classNameLower}.getPage(), w);
        return new com.ueb.framework.domain.Page<>(${classNameLower}.getPage().getPages(), ${classNameLower}.getPage().getSize(), list);
    }


}
