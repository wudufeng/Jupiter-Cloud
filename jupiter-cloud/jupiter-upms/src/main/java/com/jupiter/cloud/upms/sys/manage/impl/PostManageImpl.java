package com.jupiter.cloud.upms.sys.manage.impl;

import com.jupiter.cloud.upms.sys.dao.PostDao;
import com.jupiter.cloud.upms.sys.entity.Post;
import com.jupiter.cloud.upms.sys.manage.PostManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 岗位信息 管理服务实现类
 *
 * @author jupiter
 * @since 2020-04-24
 */
@Service
public class PostManageImpl extends GenericManageImpl<PostDao, Post> implements PostManage {

}
