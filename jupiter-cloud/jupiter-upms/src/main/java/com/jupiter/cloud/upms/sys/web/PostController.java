package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Post;
import com.jupiter.cloud.upms.sys.manage.PostManage;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 岗位信息 前端控制器
 *
 * @author jupiter
 * @since 2020-04-24
 */
@Tag(name = "岗位信息")
@MicroService
@RequestMapping("/sys/post")
public class PostController extends GenericController<PostManage, Post> {

}
