package com.jupiter.upms.sys.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiterframework.web.annotation.MicroService;
import com.jupiterframework.web.GenericController;
import com.jupiter.upms.sys.entity.Post;
import com.jupiter.upms.sys.manage.PostManage;

import io.swagger.annotations.Api;

/**
 * 岗位信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2020-04-24
 */
@Api(tags = "岗位信息")
@MicroService
@RequestMapping("/sys/post")
public class PostController extends GenericController<PostManage, Post> {

}
