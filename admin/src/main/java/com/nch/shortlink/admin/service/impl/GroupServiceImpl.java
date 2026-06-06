package com.nch.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nch.shortlink.admin.dao.entity.GroupDO;
import com.nch.shortlink.admin.dao.mapper.GroupMapper;
import com.nch.shortlink.admin.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService{


    /**
     *
     * @param groupName 短链接分组名
     */
    @Override
    public void saveGroup(String groupName) {
        GroupDO groupDO = GroupDO.builder().build();
        baseMapper.insert(groupDO);
    }
}
