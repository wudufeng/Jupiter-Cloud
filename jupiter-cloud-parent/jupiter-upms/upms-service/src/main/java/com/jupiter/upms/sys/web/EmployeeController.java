package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Employee;
import com.jupiter.upms.sys.manage.EmployeeManage;
import com.jupiter.upms.sys.pojo.DataRelativeQo;
import com.jupiter.upms.sys.service.EmployeeService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 员工管理 前端控制器
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
@Api(tags = "员工管理")
@MicroService
@RequestMapping("/sys/employee")
public class EmployeeController extends GenericController<EmployeeManage, Employee> {
    @Autowired
    private EmployeeService employeeService;


    @ApiOperation("保存机构默认角色")
    @PostMapping("/role")
    public void saveRole(@RequestParam List<Long> userIds, @RequestParam List<Long> roleIds) {
        for (Long userId : userIds) {
            employeeService.saveRole(new DataRelativeQo(userId, roleIds));
        }
    }
}
