package com.jupiter.upms.sys.pojo;

import java.util.List;

import com.jupiter.upms.sys.entity.Menu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MenuTreeVo extends Menu {

    private static final long serialVersionUID = 2237616346792136177L;

    private List<MenuTreeVo> children;

}
