package com.jupiter.cloud.upms.user.web;

import com.jupiter.cloud.upms.user.entity.UserInfo;
import com.jupiter.cloud.upms.user.manage.UserInfoManage;
import com.jupiter.cloud.upms.user.pojo.UserDto;
import com.jupiter.cloud.upms.user.pojo.UserQo;
import com.jupiter.cloud.upms.user.service.UserService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户信息 前端控制器
 *
 * @author jupiter
 * @since 2019-08-12
 */
@Tag(name = "用户信息")
@MicroService
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserService service;

    @Autowired
    private UserInfoManage manage;


    @Operation(summary = "新增用户")
    @PostMapping
    public Long add(UserQo user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return service.save(dto);
    }


    @Operation(summary = "根据ID更新数据")
    @PutMapping
    public boolean update(UserInfo data) {
        return manage.updateById(data);
    }


    @Operation(summary = "分页查询数据")
    @PostMapping("/list")
    public PageResult<UserInfo> queryPage(@RequestBody PageQuery<UserInfo> query) {
        return manage.selectPage(query);
    }
}
