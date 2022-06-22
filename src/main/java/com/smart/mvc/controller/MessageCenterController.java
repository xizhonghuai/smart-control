package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.service.MessageCenterServiceImpl;
import com.smart.mvc.vo.MessageCenterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Api("消息中心")
public class MessageCenterController {
    @Autowired
    private MessageCenterServiceImpl messageCenterService;

    @GetMapping("list")
    @ApiOperation("列表")
    public RestResponse<List<MessageCenterVO>> list(@RequestParam(value = "message", required = false) String message) {
        return RestResponse.success(messageCenterService.list(message));
    }

}
