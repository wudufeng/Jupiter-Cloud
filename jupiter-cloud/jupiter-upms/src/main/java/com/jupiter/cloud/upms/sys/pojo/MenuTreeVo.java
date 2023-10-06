package com.jupiter.cloud.upms.sys.pojo;

import com.jupiter.cloud.upms.sys.entity.Menu;
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
public class MenuTreeVo extends Menu {

    private static final long serialVersionUID = 2237616346792136177L;

    private List<MenuTreeVo> children;

}
