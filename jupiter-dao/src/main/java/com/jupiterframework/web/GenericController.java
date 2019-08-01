package com.jupiterframework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;

import io.swagger.annotations.ApiOperation;


public class GenericController<M extends GenericManage<T>, T> {
    @Autowired
    private M manage;


    @ApiOperation(value = "新增数据")
    @PostMapping
    public boolean add(T data) {
        return manage.insert(data);
    }


    @ApiOperation(value = "根据ID获取数据")
    @GetMapping
    public T get(@RequestParam Long id) {
        return manage.selectById(id);
    }


    @ApiOperation(value = "根据ID更新数据")
    @PutMapping
    public boolean update(T data) {
        return manage.updateById(data);
    }


    @ApiOperation(value = "根据ID删除数据")
    @DeleteMapping
    public boolean remove(@RequestParam Long id) {
        return manage.deleteById(id);
    }


    @ApiOperation(value = "分页查询数据")
    @PostMapping("/list")
    public PageResult<T> selectPage(@RequestBody PageQuery<T> query) {
        return manage.selectPage(query);
    }
}
