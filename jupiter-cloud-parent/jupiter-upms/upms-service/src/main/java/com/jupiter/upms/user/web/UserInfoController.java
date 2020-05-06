package com.jupiter.upms.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiter.upms.user.entity.UserInfo;
import com.jupiter.upms.user.manage.UserInfoManage;
import com.jupiter.upms.user.pojo.UserDto;
import com.jupiter.upms.user.pojo.UserQo;
import com.jupiter.upms.user.service.UserService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.util.BeanUtils;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 用户信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-12
 */
@Api(tags = "用户信息")
@MicroService
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserService service;

    @Autowired
    private UserInfoManage manage;


    @ApiOperation(value = "新增用户")
    @PostMapping
    public Long add(UserQo user) {
        return service.save(BeanUtils.copy(user, UserDto.class));
    }


    @ApiOperation(value = "根据ID更新数据")
    @PutMapping
    public boolean update(UserInfo data) {
        return manage.updateById(data);
    }


    @ApiOperation(value = "分页查询数据")
    @PostMapping("/list")
    public PageResult<UserInfo> queryPage(@RequestBody PageQuery<UserInfo> query) {
        return manage.selectPage(query);
    }
}
