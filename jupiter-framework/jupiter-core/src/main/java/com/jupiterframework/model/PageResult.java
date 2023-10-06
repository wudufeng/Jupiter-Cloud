package com.jupiterframework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 分页查询结果
 * @author jupiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -1325097077210906969L;

    /** 总记录数 */
    private long total;

    /** 结果集 */
    private List<T> records;

}
