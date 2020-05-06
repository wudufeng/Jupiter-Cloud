package com.jupiter.upms.sys.manage;

import com.jupiter.upms.sys.entity.Employee;
import com.jupiterframework.manage.GenericManage;


/**
 * 员工管理 管理服务类
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
public interface EmployeeManage extends GenericManage<Employee> {

    Employee selectByUserId(Long userId);
}
