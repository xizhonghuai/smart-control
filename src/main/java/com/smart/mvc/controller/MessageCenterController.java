package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.MessageCenterDTO;
import com.smart.mvc.entity.MessageCenter;
import com.smart.mvc.service.MessageCenterServiceImpl;
import com.smart.mvc.vo.MessageCenterVO;
import com.smart.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public RestResponse<List<MessageCenterVO>> myMessage(@RequestParam(value = "message", required = false) String message) {
        List<MessageCenter> messageCenters = messageCenterService.myMessage(message);
        List<MessageCenter> collect = messageCenters.stream().sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate())).collect(Collectors.toList());

        return RestResponse.success(collect.stream().map(v -> {
            MessageCenterVO vo = new MessageCenterVO();
            vo.setMessage(v.getMessage());
            vo.setCreatedDate(Utils.fromLocalDateTime(v.getCreatedDate()));
            vo.setFromAccountId(v.getFromAccountId());
            vo.setReadFlag(v.getReadFlag());
            vo.setToAccountId(v.getToAccountId());
            vo.setId(v.getId());
            return vo;
        }).collect(Collectors.toList()));

        //   Optional<MessageCenter> first = messageCenters.stream().min((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));
        //   return RestResponse.success(first.isPresent() ? CollectionUtil.newArrayList(first.get()) : Collections.emptyList());
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
    public RestResponse<Boolean> updateReadFlag(@PathVariable("id") Long id) {
        return RestResponse.success(messageCenterService.updateReadFlag(id));
    }
}
