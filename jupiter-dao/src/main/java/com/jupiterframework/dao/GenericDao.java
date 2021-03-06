package com.jupiterframework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jupiterframework.model.Page;


public interface GenericDao<T> extends BaseMapper<T> {

    /**
     * 批量插入
     * 
     * @param list
     * @return
     */
    Integer insertList(@Param("data") List<T> data);


    List<T> selectPageList(Page<T> page, @Param(Constants.WRAPPER) Map<String, Object> condition);
}