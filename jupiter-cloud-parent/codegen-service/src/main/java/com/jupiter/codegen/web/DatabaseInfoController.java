package com.jupiter.codegen.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiter.codegen.entity.DatabaseInfo;
import com.jupiter.codegen.manage.DatabaseInfoManage;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 数据源信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-02
 */
@Api(tags = "数据源信息")
@MicroService
@RequestMapping("/database-info")
public class DatabaseInfoController extends GenericController<DatabaseInfoManage, DatabaseInfo> {

}
