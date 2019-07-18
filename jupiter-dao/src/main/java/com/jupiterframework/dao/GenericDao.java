package com.jupiterframework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;


public interface GenericDao<T> extends BaseMapper<T> {

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 */
	Integer insertList(@Param("data") List<T> data);

	List<T> selectPageList(Pagination page, Map<String, Object> condition);
}