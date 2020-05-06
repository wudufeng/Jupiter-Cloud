package com.jupiterframework.manage.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.Page;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


public class GenericManageImpl<M extends GenericDao<T>, T> extends ServiceImpl<M, T>
        implements GenericManage<T> {

    @Override
    public boolean add(T data) {
        return super.save(data);
    }


    @Override
    public boolean addBatch(Collection<T> entityList) {
        return super.saveBatch(entityList);
    }


    @Override
    public T get(Long id) {
        return super.getById(id);
    }


    @Override
    public boolean update(T data) {
        return super.updateById(data);
    }


    @Override
    public boolean remove(Long id) {
        return super.removeById(id);
    }


    @Override
    public PageResult<T> selectPage(PageQuery<T> query) {

        Map<String, Object> condition = query.getExtra();
        if (condition == null)
            condition = new HashMap<>();
        condition.put("ew", query.getCondition());
        condition.put("queryBeginTime", query.getQueryBeginTime());
        condition.put("queryEndTime", query.getQueryEndTime());

        Page<T> page = new Page<>(query.getCurrent(), query.getSize());
        List<T> records = this.baseMapper.selectPageList(page, condition);
        return new PageResult<>(page.getTotal(), records);

    }

}
