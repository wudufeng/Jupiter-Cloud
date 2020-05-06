package com.jupiterframework.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GenericPo implements Serializable {
    private static final long serialVersionUID = 3513396245165567182L;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    /** 最后更新时间 */
    @ApiModelProperty(value = "最后更新时间")
    @TableField(value = "update_time")
    private Date updateTime;

}
