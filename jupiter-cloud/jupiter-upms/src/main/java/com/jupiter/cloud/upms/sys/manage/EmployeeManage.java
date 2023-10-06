package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.Employee;
import com.jupiterframework.manage.GenericManage;


/**
 * 员工管理 管理服务类
 *
 * @author jupiter
 * @since 2020-04-25
 */
public interface EmployeeManage extends GenericManage<Employee> {

    Employee selectByUserId(Long userId);
}
