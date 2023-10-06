package com.jupiter.cloud.upms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.cloud.upms.sys.constant.RelativeType;
import com.jupiter.cloud.upms.sys.entity.Organization;
import com.jupiter.cloud.upms.sys.entity.Role;
import com.jupiter.cloud.upms.sys.manage.DataRelativeManage;
import com.jupiter.cloud.upms.sys.manage.OrganizationManage;
import com.jupiter.cloud.upms.sys.manage.RoleManage;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiter.cloud.upms.sys.service.OrganizationService;
import com.jupiterframework.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationManage organizationManage;
    @Autowired
    private RoleManage roleManage;

    @Autowired
    private DataRelativeManage dataRelativeManage;


    @Override
    public Integer saveRole(DataRelativeQo qo) {

        Long organizationId = qo.getScopeId();
        List<Long> roleIds = qo.getInstanceIds() == null ? Collections.emptyList() : qo.getInstanceIds();

        Organization org = organizationManage.getById(organizationId);
        if (org == null) {
            throw new BusinessException(UpmsExceptionCodeEnum.ORGANIZATION_NOT_EXISTS, organizationId);
        }

        long roleList = roleIds.isEmpty() ? 0
                : roleManage.count(new QueryWrapper<>(new Role()).in("id", roleIds).select("id"));

        if (roleIds.size() != roleList) {
            throw new BusinessException(UpmsExceptionCodeEnum.ROLE_NOT_EXISTS, roleIds.toString());
        }

        return dataRelativeManage.save(qo, RelativeType.ORGANIZATION_ROLE);
    }


    @Override
    public List<Long> getOrganizationRole(Long organizationId) {
        return dataRelativeManage.getDataRelativeList(RelativeType.ORGANIZATION_ROLE, organizationId);
    }
}
