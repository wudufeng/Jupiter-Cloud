package com.jupiterframework.web;

import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * @author jupiter
 */
public class GenericController<M extends GenericManage<T>, T> {
    @Autowired
    protected M manage;


    @Operation(description = "新增数据 ")
    @PostMapping
    public boolean add(T data) {
        return manage.save(data);
    }


    @Operation(description = "根据主键获取数据")
    @GetMapping
    public T get(@RequestParam Serializable id) {
        return manage.getById(id);
    }


    @Operation(description = "根据ID更新数据")
    @PutMapping
    public boolean update(T data) {
        return manage.updateById(data);
    }


    @Operation(description = "根据主键删除数据")
    @DeleteMapping
    public boolean remove(@RequestParam Serializable id) {
        return manage.removeById(id);
    }


    @Operation(description = "分页查询数据")
    @PostMapping("/list")
    public PageResult<T> queryPage(@RequestBody PageQuery<T> query) {
        return manage.selectPage(query);
    }
}
