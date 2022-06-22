package com.smart.mvc.controller;

import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.domain.message.Message;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.mvc.service.CommandPoolService;
import com.smart.mvc.service.DeviceControlService;
import com.transmission.server.core.ConnectProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "control")
@Api(tags = "设备API")
public class DeviceAPIController {
    @Autowired
    private DeviceControlService service;


    @GetMapping("online-device")
    @ApiOperation("获取在线设备")
    public RestResponse<List<ConnectProperty>> onlineDevice() {
        return RestResponse.success(service.onlineDevice());
    }

    @GetMapping("device-message")
    @ApiOperation("获取设备最新消息")
    public RestResponse<Message> deviceMessage(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceMessage(deviceId, null));
    }

    @GetMapping("device-message/{code}")
    @ApiOperation("根据消息code获取设备最新消息")
    public RestResponse<Message> deviceMessage(@RequestParam("deviceId") String deviceId
            , @PathVariable("code") String code) {
        return RestResponse.success(service.deviceMessage(deviceId, code));
    }

    @GetMapping("device-net-status")
    @ApiOperation("查询设备网络状态")
    public RestResponse<String> deviceNetStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceNetStatus(deviceId));
    }

    @PostMapping("device-params-conf")
    @ApiOperation("配置运行参数")
    public RestResponse<Message> deviceParamsConf(@RequestBody ParamsConfMessage paramsConfMessage) {
        return RestResponse.success(service.deviceParamsConf(0, paramsConfMessage));
    }

    @PostMapping("device-params-conf/sync")
    @ApiOperation("配置运行参数(同步方式)")
    public RestResponse<Message> deviceParamsConfSync(@RequestBody ParamsConfMessage paramsConfMessage) {
        return RestResponse.success(service.deviceParamsConf(1, paramsConfMessage));
    }

    @GetMapping("device-status")
    @ApiOperation("查询设备状态数据")
    public RestResponse<Message> deviceStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceStatus(0, deviceId));
    }

    @GetMapping("device-status/sync")
    @ApiOperation("查询设备状态数据(同步方式)")
    public RestResponse<Message> deviceStatusSync(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceStatus(1, deviceId));
    }

    @GetMapping("device-params")
    @ApiOperation("查询设备运行参数数据")
    public RestResponse<Message> deviceParams(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceParams(0, deviceId));
    }

    @GetMapping("device-params/sync")
    @ApiOperation("查询设备运行参数数据(同步方式)")
    public RestResponse<Message> deviceParamsSync(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceParams(1, deviceId));
    }

    @GetMapping("command-list")
    @ApiOperation("待发送命令")
    public RestResponse<List<CommandPoolService.Command>> commandPoolList() {
        return RestResponse.success(service.commandPoolList());
    }

}
