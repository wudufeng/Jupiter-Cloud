package com.jupiterframework.manage;

import java.util.Collection;

import org.springframework.web.bind.annotation.PathVariable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


/**
 * 通用的增删改查 接口
 * 
 * @author WUDUFENG
 *
 * @param <T>
 */
public interface GenericManage<T> extends IService<T> {
    boolean add(T data);


    boolean addBatch(Collection<T> entityList);


    T get(@PathVariable Long id);


    boolean update(T data);


    boolean remove(Long id);


    PageResult<T> selectPage(PageQuery<T> query);
}
