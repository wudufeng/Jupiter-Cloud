package com.jupiter.upms.sys.service;

import java.util.List;

import com.jupiter.upms.sys.pojo.DataRelativeQo;


public interface OrganizationService {

    /**
     * 设置保存机构对应的角色
     */
    Integer saveRole(DataRelativeQo qo);


    /**
     * 获取机构绑定的角色ID
     * 
     */
    List<Long> getOrganizationRole(Long organizationId);

}
