package com.jupiter.cloud.infra.codegen.manage.impl;

import com.jupiter.cloud.infra.codegen.dao.DatabaseInfoDao;
import com.jupiter.cloud.infra.codegen.entity.DatabaseInfo;
import com.jupiter.cloud.infra.codegen.manage.DatabaseInfoManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 数据源信息 管理服务实现类
 *
 * @author jupiter
 * @since 2019-08-02
 */
@Service
public class DatabaseInfoManageImpl extends GenericManageImpl<DatabaseInfoDao, DatabaseInfo> implements DatabaseInfoManage {

}
