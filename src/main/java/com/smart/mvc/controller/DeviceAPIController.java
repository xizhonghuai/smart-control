package com.smart.mvc.controller;

import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.domain.message.Message;
import com.smart.domain.message.c2s.ParamsQueryMessageAck;
import com.smart.domain.message.c2s.TimingMessage;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.domain.message.s2c.WarningEventMessageAck;
import com.smart.mvc.dto.KeyEnDTO;
import com.smart.mvc.dto.RevStopDTO;
import com.smart.mvc.dto.SolenoidValveSwitchDTO;
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
    public RestResponse<Message> deviceMessage(@RequestParam("deviceId") String deviceId, @PathVariable("code") String code) {
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

    @GetMapping("device-params")
    @ApiOperation("查询设备运行参数数据")
    public RestResponse<Message> deviceParams(@RequestParam("deviceId") String deviceId
            , @RequestParam("id") Integer id) {
        return RestResponse.success(service.deviceParams(0, deviceId, id));
    }

    @GetMapping("device-params/sync")
    @ApiOperation("查询设备运行参数数据(同步方式)")
    public RestResponse<Message> deviceParamsSync(@RequestParam("deviceId") String deviceId
            , @RequestParam("id") Integer id) {
        Message message = service.deviceParams(1, deviceId, id);
        return RestResponse.success(message);
    }

    @PostMapping("power")
    @ApiOperation("电源")
    public RestResponse<Message> power(@RequestBody RevStopDTO dto) {
        return RestResponse.success(service.power(0, dto));
    }

    @PostMapping("power/sync")
    @ApiOperation("电源(同步方式)")
    public RestResponse<Message> powerSync(@RequestBody RevStopDTO dto) {
        return RestResponse.success(service.power(1, dto));
    }

    @PostMapping("key-en")
    @ApiOperation("按键使能")
    public RestResponse<Message> keyEn(@RequestBody KeyEnDTO dto) {
        return RestResponse.success(service.keyEn(0, dto));
    }

    @PostMapping("key-en/sync")
    @ApiOperation("按键使能(同步方式)")
    public RestResponse<Message> keyEnSync(@RequestBody KeyEnDTO dto) {
        return RestResponse.success(service.keyEn(1, dto));
    }


    @PostMapping("solenoid-valve-switch")
    @ApiOperation("按键使能")
    public RestResponse<Message> solenoidValveSwitch(@RequestBody SolenoidValveSwitchDTO dto) {
        return RestResponse.success(service.solenoidValveSwitch(0, dto));
    }

    @PostMapping("solenoid-valve-switch/sync")
    @ApiOperation("按键使能(同步方式)")
    public RestResponse<Message> solenoidValveSwitchSync(@RequestBody SolenoidValveSwitchDTO dto) {
        return RestResponse.success(service.solenoidValveSwitch(1, dto));
    }


    @GetMapping("command-list")
    @ApiOperation("待发送命令")
    public RestResponse<List<CommandPoolService.Command>> commandPoolList() {
        return RestResponse.success(service.commandPoolList());
    }

    //use
    @GetMapping("device-data")
    @ApiOperation("查询设备数据")
    public RestResponse<TimingMessage> deviceData(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceData(deviceId));
    }

    @PostMapping("device-params-conf-v2")
    @ApiOperation("配置运行参数-v2")
    public RestResponse<Message> deviceParamsConfV2(@RequestBody ParamsConfMessage paramsConfMessage) {
        return RestResponse.success(service.deviceParamsConfV2(paramsConfMessage));
    }

    @GetMapping("device-params-v2")
    @ApiOperation("查询设备运行参数数据v2")
    public RestResponse<ParamsQueryMessageAck> deviceParamsV2(@RequestParam("deviceId") String deviceId
            , @RequestParam("id") Integer id) {
        return RestResponse.success(service.deviceParamsV2(deviceId, id));
    }

    @GetMapping("device-warning")
    @ApiOperation("查询异常事件")
    public RestResponse<WarningEventMessageAck> deviceWarningEvent(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceWarningEvent(deviceId));
    }


}
