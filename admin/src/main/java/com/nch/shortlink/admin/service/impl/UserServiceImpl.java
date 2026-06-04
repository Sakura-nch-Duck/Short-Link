package com.nch.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nch.shortlink.admin.dao.entity.UserDO;
import com.nch.shortlink.admin.dao.mapper.UserMapper;
import com.nch.shortlink.admin.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
