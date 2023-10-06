package com.jupiter.cloud.upms.sys.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TenantVo {

    /** 租户编码 */
    private Long tenantId;

    /** 租户名称 */
    private String name;

}
