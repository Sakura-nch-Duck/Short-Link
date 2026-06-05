package com.nch.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nch.shortlink.admin.common.convention.exception.ClientException;
import com.nch.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nch.shortlink.admin.dao.entity.UserDO;
import com.nch.shortlink.admin.dao.mapper.UserMapper;
import com.nch.shortlink.admin.dto.req.UserRegisteReqDTO;
import com.nch.shortlink.admin.dto.resq.UserRespDTO;
import com.nch.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.nch.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.nch.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.nch.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    /**
     * 布隆过滤器快速判重（可能误判，false 则一定不存在）
     */
    @Override
    public Boolean hasUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }


    /**
     * 用户注册
     * 1. 布隆过滤器快速判断用户名是否可能已存在（false则一定不存在，允许注册）
     * 2. 分布式锁防止并发重复注册
     * 3. 加锁成功则插入数据库，失败则抛“用户名已存在”
     * 4. 插入成功后更新布隆过滤器
     * 5. finally中释放锁（注意：先判断当前线程是否还持有这把锁，有则释放）
     *
     * @param requestParam 注册参数
     */
    @Override
    public void register(UserRegisteReqDTO requestParam){
        if (!hasUsername(requestParam.getUsername())){
            throw new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try{
            if (lock.tryLock()){
                int inserted = baseMapper.insert(BeanUtil.toBean(requestParam,UserDO.class));
                if (inserted < 1){
                    throw new ClientException(USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
