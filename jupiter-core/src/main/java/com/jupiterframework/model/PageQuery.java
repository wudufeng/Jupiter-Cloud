package com.jupiterframework.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 分页查询参数
 */
@Data
@NoArgsConstructor
public class PageQuery<T> implements Serializable {

    private static final long serialVersionUID = -5237453367597292495L;

    /** 当前页 */
    private int current = 1;

    /** 每页显示条数，默认 10 */
    private int size = 10;

    /** 查询开始时间 */
    private Date beginTime;

    /** 查询结束时间 */
    private Date endTime;

    /** 查询参数 */
    private T param;
}
