package com.jupiterframework.model;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    private static final long serialVersionUID = -7948852378782700840L;


    public Page(long current, long size) {
        super(current, size, 0);
    }
}
