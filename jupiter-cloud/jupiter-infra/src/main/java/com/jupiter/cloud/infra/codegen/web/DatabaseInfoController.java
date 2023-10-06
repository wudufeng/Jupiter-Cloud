package com.jupiter.cloud.infra.codegen.web;

import com.jupiter.cloud.infra.codegen.entity.DatabaseInfo;
import com.jupiter.cloud.infra.codegen.manage.DatabaseInfoManage;
import com.jupiterframework.web.GenericController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 数据源信息 前端控制器
 *
 * @author jupiter
 * @since 2019-08-02
 */
@Tag(name = "数据源信息")
//@MicroService
@RequestMapping("/database-info")
public class DatabaseInfoController extends GenericController<DatabaseInfoManage, DatabaseInfo> {

}
