package com.nch.shortlink.admin.controller;


import com.nch.shortlink.admin.common.convention.result.Result;
import com.nch.shortlink.admin.common.convention.result.Results;
import com.nch.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.nch.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;



    @PostMapping("/api/shortlink/v1/group")
    public Result<Void> sava(@RequestBody ShortLinkGroupSaveReqDTO requsetParam){
        groupService.saveGroup(requsetParam.getName());
        return Results.success();
    }
}
