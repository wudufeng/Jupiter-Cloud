package com.jupiterframework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;


public interface GenericDao<T> extends BaseMapper<T> {

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @return
	 */
	Integer insertList(@Param("data") List<T> data);
}