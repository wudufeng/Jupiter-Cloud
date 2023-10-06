package com.jupiter.cloud.upms.sys.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.cloud.upms.sys.dao.OrganizationDao;
import com.jupiter.cloud.upms.sys.entity.Organization;
import com.jupiter.cloud.upms.sys.manage.OrganizationManage;
import com.jupiter.cloud.upms.sys.pojo.OrganizationTreeVo;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 组织架构 管理服务实现类
 *
 * @author jupiter
 * @since 2019-08-09
 */
@Service
public class OrganizationManageImpl extends GenericManageImpl<OrganizationDao, Organization>
        implements OrganizationManage {

    private static final String TOP_LEVEL_PARENT_CODE = "";
    private static final Integer TOP_LEVEL = 1;


    private String generateCode(Long tenantId, String parentCode) {

        QueryWrapper<Organization> wrapper = new QueryWrapper<>(new Organization(tenantId, parentCode));
        long count = this.count(wrapper);
        return String.format("%s%02x", parentCode, count + 1);
    }


    @Override
    public boolean save(Organization param) {
        Organization org = new Organization(param.getTenantId(), param.getParentCode());
        org.setName(param.getName());
        org.setFullName(param.getFullName());
        org.setSort(param.getSort());
        org.setType(param.getType());

        if (StringUtils.isNotBlank(org.getParentCode())) {
            Organization parent =
                    this.getOne(new QueryWrapper<>(new Organization(org.getParentCode(), org.getTenantId())));
            if (parent == null) {
                throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_NOT_EXISTS,
                    org.getParentCode());
            }
            org.setLevel(parent.getLevel() + 1);

            if (org.getLevel() > 9) {
                throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_ILLIGAL_LEVEL, 9);
            }

        } else {
            org.setLevel(TOP_LEVEL);
            org.setParentCode(TOP_LEVEL_PARENT_CODE);
        }

        org.setCode(this.generateCode(org.getTenantId(), org.getParentCode()));

        if (org.getSort() == null) {
            org.setSort(1);
        }

        return super.save(org);
    }


    @Override
    @Transactional
    public boolean updateById(Organization param) {
        Assert.notNull(param.getId(), "ID SHOULD NOT BE NULL");

        Organization origin = this.getById(param.getId());
        if (origin == null) {
            throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_NOT_EXISTS, param.getId());
        }

        // 所属租户不允许修改, 其它字段有发生变更则修改
        Organization updater = new Organization();
        updater.setId(param.getId());
        boolean changed = false;
        if (param.getName() != null && !StringUtils.equals(param.getName(), origin.getName())) {
            updater.setName(param.getName());
            changed = true;
        }

        if (param.getFullName() != null && !StringUtils.equals(param.getFullName(), origin.getFullName())) {
            updater.setFullName(param.getFullName());
            changed = true;
        }

        if (param.getType() != null && !param.getType().equals(origin.getType())) {
            updater.setType(param.getType());
            changed = true;
        }

        if (param.getSort() != null && !param.getSort().equals(origin.getSort())) {
            updater.setSort(param.getSort());
            changed = true;
        }

        if (StringUtils.isBlank(param.getParentCode())) {
            param.setParentCode(TOP_LEVEL_PARENT_CODE);
        }

        if (!StringUtils.equals(param.getParentCode(), origin.getParentCode())) {
            changed = true;

            if (!param.getParentCode().equals(TOP_LEVEL_PARENT_CODE)) {
                Organization parent = this.getOne(
                    new QueryWrapper<>(new Organization(param.getParentCode(), origin.getTenantId())));
                if (parent == null) {
                    throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_NOT_EXISTS,
                        param.getParentCode());
                }
                updater.setLevel(parent.getLevel() + 1);
            } else {
                updater.setLevel(TOP_LEVEL);
            }
            updater.setParentCode(param.getParentCode());
            updater.setCode(this.generateCode(origin.getTenantId(), updater.getParentCode()));

            // 所属上级改变了，需要将下级也变更编码
            this.updateChildParentCode(origin.getTenantId(), origin.getCode(), updater.getCode(),
                updater.getLevel() - origin.getLevel());
        }

        if (changed) {
            return super.updateById(updater);
        }

        return false;
    }


    private void updateChildParentCode(Long tenantId, String originParentCode, String updateParentCode,
            Integer increaseLevel) {
        List<Organization> childs = super.list(
            new QueryWrapper<>(new Organization(tenantId)).likeRight("parent_code", originParentCode));

        for (Organization child : childs) {
            Organization update = new Organization();
            update.setId(child.getId());
            update.setLevel(child.getLevel() + increaseLevel);
            update.setCode(child.getCode().replaceFirst(originParentCode, updateParentCode));
            update.setParentCode(child.getParentCode().replaceFirst(originParentCode, updateParentCode));

            super.updateById(update);
        }
    }


    @Override
    public boolean removeById(Serializable id) {
        Organization org = super.getById(id);
        if (org == null || org.getDel()) {
            return true;
        }

        // 查询子节点
        QueryWrapper<Organization> wrapper = new QueryWrapper<>(new Organization(org.getTenantId()));
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.likeRight("parent_code", org.getCode());
        wrapper.ne("id", id);

        long count = this.count(wrapper);

        if (count > 0) {
            // 需要先删除子节点
            throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_EXIST_CHILD);
        }

        Organization delete = new Organization();
        delete.setId((Long)id);
        delete.setDel(Boolean.TRUE);

        return super.updateById(delete);
    }


    @Override
    public List<OrganizationTreeVo> trees(Long tenantId) {
        QueryWrapper<Organization> wrapper = new QueryWrapper<>(new Organization(tenantId));
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.orderByAsc("level,sort");
        List<Organization> all = super.list(wrapper);

        Map<Long, List<Organization>> tenantMap =
                all.stream().collect(Collectors.groupingBy(Organization::getTenantId));

        List<OrganizationTreeVo> result = new ArrayList<>();

        tenantMap.entrySet().forEach(orgList -> {
            Map<String/* 上级机构编码 */, List<Organization>> map =
                    orgList.getValue().stream().collect(Collectors.groupingBy(Organization::getParentCode));
            // 筛选出顶级节点
            List<Organization> topList = orgList.getValue().stream()
                .filter(a -> a.getParentCode().equals(TOP_LEVEL_PARENT_CODE)).collect(Collectors.toList());
            // 将每个节点递归设置子节点
            topList.forEach(top -> result.add(this.convert(top, map)));
        });

        return result;
    }


    /** 设置子节点 */
    private OrganizationTreeVo convert(Organization o, Map<String, List<Organization>> map) {
        OrganizationTreeVo treeVo = new OrganizationTreeVo();
        BeanUtils.copyProperties(o, treeVo);
        List<Organization> childrens = map.get(o.getCode());
        if (childrens == null) {
            return treeVo;
        }

        List<OrganizationTreeVo> organizationTreeVos = new ArrayList<>(childrens.size());
        childrens.forEach(child -> organizationTreeVos.add(this.convert(child, map)));

        treeVo.setChildren(organizationTreeVos);
        return treeVo;
    }
}
