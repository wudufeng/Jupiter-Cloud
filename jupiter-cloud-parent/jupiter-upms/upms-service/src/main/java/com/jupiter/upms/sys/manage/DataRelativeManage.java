package com.jupiter.upms.sys.manage;

import com.jupiter.upms.sys.entity.DataRelative;
import com.jupiter.upms.sys.pojo.DataRelativeQo;
import com.jupiterframework.manage.GenericManage;


/**
 * 一对多关系映射表 管理服务类
 *
 * @author WUDUFENG
 * @since 2020-04-27
 */
public interface DataRelativeManage extends GenericManage<DataRelative> {

    Integer save(DataRelativeQo qo, Integer refType);

}
