package com.nch.shortlink.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.nch.shortlink.admin.common.convention.result.Result;
import com.nch.shortlink.admin.common.convention.result.Results;
import com.nch.shortlink.admin.dto.req.UserRegisteReqDTO;
import com.nch.shortlink.admin.dto.resq.UserActualRespDTO;
import com.nch.shortlink.admin.dto.resq.UserRespDTO;
import com.nch.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/shortlink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
            return Results.success(userService.getUserByUsername(username));
        }


    /**
     * 根据用户名查询用户真实信息
     */
    @GetMapping("/api/shortlink/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
            return Results.success(BeanUtil.toBean(userService.getUserByUsername(username),UserActualRespDTO.class));
    }

    /**
     *查询用户名是否存在
     */
    @GetMapping("/api/shortlink/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }
    /**
     * 注册用户
     * Result<Void>：注册成功后不需要返回额外的业务数据
     * @RequestBody：告诉 Spring 从 HTTP 请求的 body 中读取 JSON 数据，并自动转换成 UserRegisteReqDTO 对象。
     *
     * UserRegisteReqDTO：数据传输对象，包含注册所需的字段
     */
    @PostMapping("/api/shortlink/v1/user")
    public Result<Void> register(@RequestBody UserRegisteReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }
}
