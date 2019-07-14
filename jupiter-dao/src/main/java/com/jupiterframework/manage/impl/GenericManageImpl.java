package com.jupiterframework.manage.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


public class GenericManageImpl<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T>
		implements GenericManage<T> {

	@Override
	public PageResult<T> selectPage(PageQuery<T> query) {

		Page<T> page = new Page<>(query.getCurrent(), query.getSize());
		this.selectPage(page, new EntityWrapper<>(query.getParam()));
		return new PageResult<>(page.getTotal(), page.getRecords());

	}

}
