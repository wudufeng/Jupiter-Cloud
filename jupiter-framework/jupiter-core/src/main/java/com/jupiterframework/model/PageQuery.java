package com.jupiterframework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 分页查询参数
 * @author jupiter
 */
@Data
@NoArgsConstructor
public class PageQuery<T> implements Serializable {

    private static final long serialVersionUID = -5237453367597292495L;

    /** 当前页 */
    private int current = 1;

    /** 每页显示条数 */
    private int size = 10;

    /** 查询开始时间 */
    private Date queryBeginTime;

    /** 查询结束时间 */
    private Date queryEndTime;

    /** 实体查询参数 */
    private T condition;

    /** 额外的查询参数 */
    private Map<String, Object> extra;
}
