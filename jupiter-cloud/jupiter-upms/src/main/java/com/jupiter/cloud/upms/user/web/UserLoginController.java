package com.jupiter.cloud.upms.user.web;

import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户登录信息 前端控制器
 *
 * @author jupiter
 * @since 2020-04-25
 */
@Tag(name = "用户登录信息")
@MicroService
@RequestMapping("/user/login")
public class UserLoginController {

}
