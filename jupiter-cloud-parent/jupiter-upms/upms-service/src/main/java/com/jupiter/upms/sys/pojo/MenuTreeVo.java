package com.jupiter.upms.sys.pojo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MenuTreeVo {

    private Long id;
    private String code;
    private String name;
    private String parentCode;
    private List<MenuTreeVo> children;


    public MenuTreeVo(Long id, String code, String name, String parentCode) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

}
