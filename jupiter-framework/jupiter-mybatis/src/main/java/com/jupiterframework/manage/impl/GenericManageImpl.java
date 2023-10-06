package com.jupiterframework.manage.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


/**
 * @author jupiter
 */
public class GenericManageImpl<M extends GenericDao<T>, T> extends ServiceImpl<M, T>
        implements GenericManage<T> {
    @Override
    public PageResult<T> selectPage(PageQuery<T> query) {
        return null;

    }

}
