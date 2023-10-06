package com.jupiter.cloud.upms.sys.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.upms.sys.dao.EmployeeDao;
import com.jupiter.cloud.upms.sys.entity.Employee;
import com.jupiter.cloud.upms.sys.manage.EmployeeManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;


/**
 * 员工管理 管理服务实现类
 *
 * @author jupiter
 * @since 2020-04-25
 */
@Service
public class EmployeeManageImpl extends GenericManageImpl<EmployeeDao, Employee> implements EmployeeManage {

    @Override
    public Employee selectByUserId(Long userId) {
        return this.getOne(new QueryWrapper<>(new Employee()));
    }

}
