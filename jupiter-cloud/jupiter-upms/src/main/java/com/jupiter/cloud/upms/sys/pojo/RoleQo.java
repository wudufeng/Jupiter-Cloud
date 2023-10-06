package com.jupiter.cloud.upms.sys.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


/** 新增或者修改角色，并且数据权限指定为2-自定义的请求参数对象 */
@Data
public class RoleQo {

    private Long id;

    /** 租户编码 */
    @NotNull
    private Long tenantId;

    /** 角色名称 */
    @NotBlank
    private String name;

    /** 角色标识 */
    @NotBlank
    private String code;

    /** 角色描述 */
    @NotBlank
    private String description;

    @NotEmpty
    private List<Long> organizationIds;
}
