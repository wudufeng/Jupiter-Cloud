package com.jupiter.upms.sys.service;

import com.jupiter.upms.sys.pojo.DataRelativeQo;


public interface OrganizationService {

    /**
     * 设置保存机构对应的角色
     */
    Integer saveRole(DataRelativeQo qo);

}
