package com.jupiterframework.manage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


public class GenericManageImpl<M extends GenericDao<T>, T> extends ServiceImpl<GenericDao<T>, T> implements GenericManage<T> {

    @Override
    public PageResult<T> selectPage(PageQuery<T> query) {

        Map<String, Object> condition = query.getExtra();
        if (condition == null)
            condition = new HashMap<>();
        condition.put("ew", query.getCondition());
        condition.put("queryBeginTime", query.getQueryBeginTime());
        condition.put("queryEndTime", query.getQueryEndTime());

        Pagination page = new Pagination(query.getCurrent(), query.getSize());
        List<T> records = this.baseMapper.selectPageList(page, condition);
        return new PageResult<>(page.getTotal(), records);

    }

}
