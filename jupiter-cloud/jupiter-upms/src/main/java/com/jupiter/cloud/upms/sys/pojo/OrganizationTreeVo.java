package com.jupiter.cloud.upms.sys.pojo;

import com.jupiter.cloud.upms.sys.entity.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author jupiter
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrganizationTreeVo extends Organization {
    private static final long serialVersionUID = -5855187438173162912L;

    private List<OrganizationTreeVo> children;


    public OrganizationTreeVo(Long id, String code, String name) {
        super();
        this.setId(id);
        this.setCode(code);
        this.setName(name);
    }

}
