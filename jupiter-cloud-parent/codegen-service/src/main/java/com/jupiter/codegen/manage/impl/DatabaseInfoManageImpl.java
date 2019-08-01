package com.jupiter.codegen.manage.impl;

import com.jupiter.codegen.entity.DatabaseInfo;
import com.jupiter.codegen.dao.DatabaseInfoDao;
import com.jupiter.codegen.manage.DatabaseInfoManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 数据源信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-02
 */
@Service
public class DatabaseInfoManageImpl extends GenericManageImpl<DatabaseInfoDao, DatabaseInfo> implements DatabaseInfoManage {

}
