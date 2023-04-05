package com.jupiter.upms.sys.service;

import java.util.List;

import com.jupiter.upms.sys.pojo.DataRelativeQo;


public interface EmployeeService {

    /** 绑定角色 */
    Integer saveRole(DataRelativeQo dataRelativeQo);


    /** 获取员工绑定的角色 */
    List<Long> getEmployeeRole(Long userId);
}
