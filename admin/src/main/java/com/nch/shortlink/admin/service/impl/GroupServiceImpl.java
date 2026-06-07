package com.nch.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nch.shortlink.admin.common.convention.exception.ClientException;
import com.nch.shortlink.admin.dao.entity.GroupDO;
import com.nch.shortlink.admin.dao.mapper.GroupMapper;
import com.nch.shortlink.admin.service.GroupService;
import com.nch.shortlink.admin.toolkit.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService{

    @Override
    public void saveGroup(String groupName){
        String gid;
        do {
            gid = RandomGenerator.generateRandomString(6);
        } while (!hasGid(gid));
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }

    private boolean hasGid(String gid){
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid,gid)
                //TODO 设置用户名
                .eq(GroupDO::getUsername,null);
        GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag == null;
    }

}
