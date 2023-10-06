package com.jupiter.cloud.upms.sys.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.upms.sys.dao.DataRelativeDao;
import com.jupiter.cloud.upms.sys.entity.DataRelative;
import com.jupiter.cloud.upms.sys.manage.DataRelativeManage;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 一对多关系映射表 管理服务实现类
 *
 * @author jupiter
 * @since 2020-04-27
 */
@Service
public class DataRelativeManageImpl extends GenericManageImpl<DataRelativeDao, DataRelative>
        implements DataRelativeManage {

    @Override
    public Integer save(DataRelativeQo qo, Integer refType) {

        Long scopeId = qo.getScopeId();
        List<Long> instanceIds = qo.getInstanceIds() == null ? Collections.emptyList() : qo.getInstanceIds();

        // 查询出已关联了该范围的数据
        List<DataRelative> originList = this.list(new QueryWrapper<>(new DataRelative(scopeId)));

        List<Long> enableIds = new ArrayList<>();// 要将其设置为启用的id
        List<Long> disableIds = new ArrayList<>();// 要将其设置禁用的id
        List<Long> ignoreIds = new ArrayList<>();// 忽略不做处理的id，因为原已经设置为可用，并且状态不用改变
        originList.forEach(ori -> {
            if (Boolean.TRUE.equals(ori.getEnable())) {
                if (!instanceIds.contains(ori.getInstanceId())) {
                    disableIds.add(ori.getInstanceId());
                } else {
                    ignoreIds.add(ori.getInstanceId());
                }
            } else if (instanceIds.contains(ori.getInstanceId())) {
                enableIds.add(ori.getInstanceId());
            }
        });

        int rowAffected = 0;
        if (!enableIds.isEmpty()) {
            rowAffected += baseMapper.updateEnable(refType, scopeId, enableIds, Boolean.TRUE);
            instanceIds.removeAll(enableIds);
        }
        if (!disableIds.isEmpty()) {
            baseMapper.updateEnable(refType, scopeId, disableIds, Boolean.FALSE);
            instanceIds.removeAll(disableIds);
        }

        instanceIds.removeAll(ignoreIds);

        if (!instanceIds.isEmpty()) {
            rowAffected += baseMapper.save(refType, scopeId, instanceIds);
        }

        return rowAffected;
    }


    @Override
    public List<Long> getDataRelativeList(Integer relativeType, Long... scopeIds) {
        return baseMapper.getDataRelativeList(relativeType, Arrays.asList(scopeIds));
    }
}
