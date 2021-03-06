package com.jupiterframework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;

import io.swagger.annotations.ApiOperation;


public class GenericController<M extends GenericManage<T>, T> {
    @Autowired
    protected M manage;


    @ApiOperation(value = "新增数据")
    @PostMapping
    public boolean add(T data) {
        return manage.add(data);
    }


    @ApiOperation(value = "根据主键获取数据")
    @GetMapping(value = "{id}")
    public T get(@PathVariable Long id) {
        return manage.get(id);
    }


    @ApiOperation(value = "根据ID更新数据")
    @PutMapping
    public boolean update(T data) {
        return manage.update(data);
    }


    @ApiOperation(value = "根据主键删除数据")
    @DeleteMapping(value = "{id}")
    public boolean remove(@PathVariable Long id) {
        return manage.remove(id);
    }


    @ApiOperation(value = "分页查询数据")
    @PostMapping("/list")
    public PageResult<T> queryPage(@RequestBody PageQuery<T> query) {
        return manage.selectPage(query);
    }
}
