package com.jupiter.cloud.upms.sys.service;

import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;

import java.util.List;


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
