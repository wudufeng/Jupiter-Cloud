package com.jupiter.upms.sys.pojo;

import com.jupiter.upms.sys.entity.Menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MenuVo extends Menu {

    private static final long serialVersionUID = -8179707084154582948L;
    /** 是否还有子菜单 */
    private boolean hasChildren;
}
