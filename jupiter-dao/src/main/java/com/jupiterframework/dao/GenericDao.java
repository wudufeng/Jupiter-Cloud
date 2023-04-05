package com.jupiterframework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jupiterframework.model.Page;


public interface GenericDao<T> extends BaseMapper<T> {

    List<T> selectPageList(Page<T> page, @Param("condition") Map<String, Object> condition);
}