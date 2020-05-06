package com.jupiter.upms.sys.service;

import com.jupiter.upms.sys.pojo.DataRelativeQo;


public interface EmployeeService {

    /** 绑定角色 */
    Integer saveRole(DataRelativeQo dataRelativeQo);
}
