package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.DataRelative;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiterframework.manage.GenericManage;


/**
 * 一对多关系映射表 管理服务类
 *
 * @author jupiter
 * @since 2020-04-27
 */
public interface DataRelativeManage extends GenericManage<DataRelative> {

    Integer save(DataRelativeQo qo, Integer refType);


    java.util.List<Long> getDataRelativeList(Integer relativeType, Long... scopeIds);
}
