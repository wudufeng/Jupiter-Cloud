package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Employee;
import com.jupiter.cloud.upms.sys.manage.EmployeeManage;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiter.cloud.upms.sys.service.EmployeeService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 员工管理 前端控制器
 *
 * @author jupiter
 * @since 2020-04-25
 */
@Tag(name = "员工管理")
@MicroService
@RequestMapping("/sys/employee")
public class EmployeeController extends GenericController<EmployeeManage, Employee> {
    @Resource
    private EmployeeService employeeService;


    @Operation(summary = "保存机构默认角色")
    @PostMapping("/role")
    public void saveRole(@RequestParam List<Long> userIds, @RequestParam List<Long> roleIds) {
        for (Long userId : userIds) {
            employeeService.saveRole(new DataRelativeQo(userId, roleIds));
        }
    }


    @Operation(summary = "获取用户绑定的角色")
    @GetMapping(value = "/role")
    public Long[] getEmployeeRole(@RequestParam Long userId) {
        return employeeService.getEmployeeRole(userId).toArray(new Long[] {});
    }
}
