package com.jupiter.upms.sys.pojo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrganizationTreeVo {

    private Long id;
    private String code;
    private String name;

    private List<OrganizationTreeVo> children;


    public OrganizationTreeVo(Long id, String code, String name) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
    }

}
