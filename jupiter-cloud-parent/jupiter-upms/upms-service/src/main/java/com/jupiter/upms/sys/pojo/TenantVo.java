package com.jupiter.upms.sys.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TenantVo {

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    private Long tenantId;

    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    private String name;

}
