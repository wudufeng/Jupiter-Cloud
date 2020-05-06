package com.jupiter.upms.user.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 用户登录信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
@Api(tags = "用户登录信息")
@MicroService
@RequestMapping("/user/login")
public class UserLoginController {

}
