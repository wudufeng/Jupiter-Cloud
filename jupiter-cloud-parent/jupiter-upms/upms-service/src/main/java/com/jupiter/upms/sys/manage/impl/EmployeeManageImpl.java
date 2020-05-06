package com.jupiter.upms.sys.manage.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.upms.sys.dao.EmployeeDao;
import com.jupiter.upms.sys.entity.Employee;
import com.jupiter.upms.sys.manage.EmployeeManage;
import com.jupiterframework.manage.impl.GenericManageImpl;


/**
 * 员工管理 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
@Service
public class EmployeeManageImpl extends GenericManageImpl<EmployeeDao, Employee> implements EmployeeManage {

    @Override
    public Employee selectByUserId(Long userId) {
        return this.getOne(new QueryWrapper<>(new Employee()));
    }

}
