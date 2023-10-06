package com.jupiter.cloud.upms.sys.service.impl;

import com.jupiter.cloud.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.cloud.upms.sys.constant.RelativeType;
import com.jupiter.cloud.upms.sys.constant.RoleDataType;
import com.jupiter.cloud.upms.sys.entity.Role;
import com.jupiter.cloud.upms.sys.manage.DataRelativeManage;
import com.jupiter.cloud.upms.sys.manage.RoleManage;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiter.cloud.upms.sys.pojo.RoleQo;
import com.jupiter.cloud.upms.sys.service.RoleService;
import com.jupiterframework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleManage roleManage;

    @Autowired
    private DataRelativeManage dataRelativeManage;


    @Override
    @Transactional
    public Integer saveRoleOrganization(RoleQo roleQo) {
        Long roleId = roleQo.getId();
        List<Long> organizationIds = roleQo.getOrganizationIds();

        if (roleId == null) {
            // 新增
            Role role = new Role();
            BeanUtils.copyProperties(roleQo, role);
            role.setDataType(RoleDataType.SPECIAL);
            roleManage.save(role);
            roleId = role.getId();

            return dataRelativeManage.save(new DataRelativeQo(roleId, organizationIds),
                RelativeType.ROLE_ORGANIZATION) + 1;
        } else {
            // 修改
            Role role = null;
            if ((role = roleManage.getById(roleId)) == null) {
                throw new BusinessException(UpmsExceptionCodeEnum.ROLE_NOT_EXISTS, roleId);
            }

            Role updateRole = new Role();
            updateRole.setId(roleId);
            updateRole.setDataType(RoleDataType.SPECIAL);
            if (!roleQo.getDescription().equals(role.getDescription())) {
                updateRole.setDescription(roleQo.getDescription());
            }
            updateRole.setName(roleQo.getName());
            updateRole.setCode(roleQo.getCode());
            roleManage.updateById(updateRole);
        }

        log.debug("修改角色[{}]数据权限", roleQo.getName());

        return dataRelativeManage.save(new DataRelativeQo(roleId, organizationIds),
            RelativeType.ROLE_ORGANIZATION);
    }


    @Override
    @Transactional
    public Integer saveRoleMenu(Long roleId, List<Long> menuIds) {
        Role role = null;
        if ((role = roleManage.getById(roleId)) == null) {
            throw new BusinessException(UpmsExceptionCodeEnum.ROLE_NOT_EXISTS, roleId);
        }

        log.debug("修改角色[{}]菜单权限", role.getName());

        return dataRelativeManage.save(new DataRelativeQo(roleId, menuIds), RelativeType.ROLE_MENU);
    }


    @Override
    public List<Long> getRoleOrganizations(Long roleId) {
        return dataRelativeManage.getDataRelativeList(RelativeType.ROLE_ORGANIZATION, roleId);
    }


    @Override
    public List<Long> getRoleMenus(Long roleId) {
        return dataRelativeManage.getDataRelativeList(RelativeType.ROLE_MENU, roleId);
    }
}
