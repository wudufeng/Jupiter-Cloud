package com.jupiter.upms.sys.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.upms.sys.constant.RelativeType;
import com.jupiter.upms.sys.entity.Employee;
import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.manage.DataRelativeManage;
import com.jupiter.upms.sys.manage.EmployeeManage;
import com.jupiter.upms.sys.manage.RoleManage;
import com.jupiter.upms.sys.pojo.DataRelativeQo;
import com.jupiter.upms.sys.service.EmployeeService;
import com.jupiterframework.exception.BusinessException;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeManage employeeManage;
    @Autowired
    private RoleManage roleManage;
    @Autowired
    private DataRelativeManage dataRelativeManage;


    @Override
    public Integer saveRole(DataRelativeQo qo) {

        Long userId = qo.getScopeId();
        List<Long> roleIds = qo.getInstanceIds() == null ? Collections.emptyList() : qo.getInstanceIds();

        Employee org = employeeManage.selectByUserId(userId);
        if (org == null) {
            throw new BusinessException(UpmsExceptionCodeEnum.USER_NOT_EXISTS, userId);
        }

        int roleList = roleIds.isEmpty() ? 0
                : roleManage.count(new QueryWrapper<>(new Role()).in("id", roleIds).select("id"));

        if (roleIds.size() != roleList) {
            throw new BusinessException(UpmsExceptionCodeEnum.ROLE_NOT_EXISTS, roleIds.toString());
        }

        return dataRelativeManage.save(qo, RelativeType.USER_ROLE);
    }


    @Override
    public List<Long> getEmployeeRole(Long userId) {
        return dataRelativeManage.getDataRelativeList(RelativeType.USER_ROLE, userId);
    }
}
