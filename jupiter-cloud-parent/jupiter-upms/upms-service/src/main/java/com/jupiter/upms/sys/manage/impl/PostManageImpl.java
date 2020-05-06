package com.jupiter.upms.sys.manage.impl;

import com.jupiter.upms.sys.entity.Post;
import com.jupiter.upms.sys.dao.PostDao;
import com.jupiter.upms.sys.manage.PostManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 岗位信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2020-04-24
 */
@Service
public class PostManageImpl extends GenericManageImpl<PostDao, Post> implements PostManage {

}
