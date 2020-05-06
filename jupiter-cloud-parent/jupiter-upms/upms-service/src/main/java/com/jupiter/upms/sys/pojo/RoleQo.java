package com.jupiter.upms.sys.pojo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/** 新增或者修改角色，并且数据权限指定为2-自定义的请求参数对象 */
@Data
public class RoleQo {

    private Long id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @NotNull
    private Long tenantId;

    /** 角色名称 */
    @ApiModelProperty(value = "角色名称")
    @NotBlank
    private String name;

    /** 角色标识 */
    @ApiModelProperty(value = "角色标识")
    @NotBlank
    private String code;

    /** 角色描述 */
    @ApiModelProperty(value = "角色描述")
    @NotBlank
    private String description;

    @NotEmpty
    private List<Long> organizationIds;
}
