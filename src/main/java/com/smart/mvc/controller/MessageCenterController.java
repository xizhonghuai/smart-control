package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.MessageCenterDTO;
import com.smart.mvc.entity.MessageCenter;
import com.smart.mvc.service.MessageCenterServiceImpl;
import com.smart.mvc.vo.MessageCenterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * MESSAGE_CENTER 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/message-center")
@Api(tags = "消息中心")
public class MessageCenterController {
    @Autowired
    private MessageCenterServiceImpl messageCenterService;

    @GetMapping("my-message")
    @ApiOperation("我的消息")
    public RestResponse<List<MessageCenter>> myMessage(@RequestParam(value = "message", required = false) String message) {
        return RestResponse.success(messageCenterService.myMessage(message));
    }

    @GetMapping("list")
    @ApiOperation("消息列表")
    public RestResponse<List<MessageCenterVO>> list(MessageCenterDTO dto) {
        return RestResponse.success(messageCenterService.list(dto));
    }

    @PostMapping("feedback")
    @ApiOperation("消息反馈")
    public RestResponse<Boolean> feedback(@RequestBody MessageCenterDTO dto) {
        return RestResponse.success(messageCenterService.feedback(dto));
    }

    @PutMapping("update-read-flag/{id}")
    @ApiOperation("标记为已读")
    public RestResponse<Boolean> updateReadFlag(@PathVariable("id")Long id) {
        return RestResponse.success(messageCenterService.updateReadFlag(id));
    }
}
