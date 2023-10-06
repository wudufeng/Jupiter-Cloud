package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.Organization;
import com.jupiter.cloud.upms.sys.pojo.OrganizationTreeVo;
import com.jupiterframework.manage.GenericManage;

import java.util.List;


/**
 * 组织架构 管理服务类
 *
 * @author jupiter
 * @since 2019-08-09
 */
public interface OrganizationManage extends GenericManage<Organization> {

    /**
     * 以树状结构返回指定租户的组织架构
     * 
     * @param tenantId
     * @return
     */
    List<OrganizationTreeVo> trees(Long tenantId);
}
