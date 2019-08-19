package com.jupiter.upms.user.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiter.upms.user.entity.UserInfo;
import com.jupiter.upms.user.manage.UserInfoManage;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 用户信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-12
 */
@Api(tags = "用户信息")
@MicroService
@RequestMapping("/user-info")
public class UserInfoController extends GenericController<UserInfoManage, UserInfo> {

}
