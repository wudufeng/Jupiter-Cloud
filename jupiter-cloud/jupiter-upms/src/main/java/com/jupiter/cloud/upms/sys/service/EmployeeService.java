package com.jupiter.cloud.upms.sys.service;

import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;

import java.util.List;


public interface EmployeeService {

    /** 绑定角色 */
    Integer saveRole(DataRelativeQo dataRelativeQo);


    /** 获取员工绑定的角色 */
    List<Long> getEmployeeRole(Long userId);
}
