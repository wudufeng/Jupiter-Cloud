package com.jupiter.upms.sys.manage.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jupiter.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.upms.sys.dao.OrganizationDao;
import com.jupiter.upms.sys.entity.Organization;
import com.jupiter.upms.sys.manage.OrganizationManage;
import com.jupiter.upms.sys.pojo.OrganizationTreeVo;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.StringUtils;


/**
 * 组织架构 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-09
 */
@Service
public class OrganizationManageImpl extends GenericManageImpl<OrganizationDao, Organization> implements OrganizationManage {

    @Override
    public boolean insert(Organization param) {
        Organization org = new Organization(param.getTenantId(), param.getParentCode());
        org.setName(param.getName());
        org.setSort(param.getSort());

        if (StringUtils.isNotBlank(org.getParentCode())) {
            Organization parent = this.selectOne(new EntityWrapper<>(new Organization(org.getParentCode(), org.getTenantId())));
            if (parent == null)
                throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_NOT_EXISTS, org.getParentCode());
            org.setLevel(parent.getLevel() + 1);

            if (org.getLevel() > 9) {
                throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_ILLIGAL_LEVEL, 9);
            }

        } else {
            org.setLevel(1);
            org.setParentCode("");
        }

        Wrapper<Organization> wrapper = new EntityWrapper<>(new Organization(org.getTenantId(), org.getParentCode()));
        int count = this.selectCount(wrapper);
        org.setCode(String.format("%s%02x", org.getParentCode(), count + 1));

        if (org.getSort() == null)
            org.setSort(1);

        return super.insert(org);
    }


    @Override
    public boolean updateById(Organization entity) {
        Organization updater = new Organization();
        updater.setId(entity.getId());

        // 仅允许修改名称和排序
        boolean changed = false;
        if (entity.getName() != null) {
            updater.setName(entity.getName());
            changed = true;
        }
        if (entity.getSort() != null) {
            updater.setSort(entity.getSort());
            changed = true;

        }
        if (changed)
            return super.updateById(entity);

        return false;
    }


    @Override
    public boolean deleteById(Serializable id) {
        Organization org = super.selectById(id);
        if (org == null || org.getDel())
            return true;

        Wrapper<Organization> wrapper = new EntityWrapper<>(new Organization(org.getTenantId()));
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.like("parent_code", org.getCode(), SqlLike.RIGHT);
        wrapper.ne("id", id);

        int count = this.selectCount(wrapper);

        if (count > 0) {
            throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_EXIST_CHILD);
        }

        Organization delete = new Organization();
        delete.setId((Long) id);
        delete.setDel(Boolean.TRUE);

        return super.updateById(delete);
    }


    @Override
    public List<OrganizationTreeVo> trees(Long tenantId) {
        Wrapper<Organization> wrapper = new EntityWrapper<>(new Organization(tenantId));
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.orderBy("level,sort");
        List<Organization> all = super.selectList(wrapper);

        Map<Long, List<Organization>> tenantMap = all.stream().collect(Collectors.groupingBy(Organization::getTenantId));

        List<OrganizationTreeVo> result = new ArrayList<>();

        tenantMap.entrySet().forEach(entry -> {
            Map<String, List<Organization>> map = entry.getValue().stream().collect(Collectors.groupingBy(Organization::getParentCode));
            // 筛选出顶级节点
            List<Organization> top = entry.getValue().stream().filter(a -> a.getLevel() == 1).collect(Collectors.toList());
            // 将每个节点递归设置子节点
            top.forEach(o -> result.add(this.convert(o, map)));
        });

        return result;
    }


    /** 设置子节点 */
    private OrganizationTreeVo convert(Organization o, Map<String, List<Organization>> map) {
        OrganizationTreeVo treeVo = new OrganizationTreeVo(o.getId(), o.getCode(), o.getName());
        List<Organization> childrens = map.get(o.getCode());
        if (childrens == null)
            return treeVo;

        List<OrganizationTreeVo> organizationTreeVos = new ArrayList<>(childrens.size());
        childrens.forEach(child -> organizationTreeVos.add(this.convert(child, map)));

        treeVo.setChildren(organizationTreeVos);
        return treeVo;
    }
}
