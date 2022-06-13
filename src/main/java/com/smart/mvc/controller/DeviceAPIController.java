package com.smart.mvc.controller;

import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.domain.message.Message;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.mvc.service.DeviceControlService;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "control")
public class DeviceAPIController {
    @Autowired
    private DeviceControlService service;


    @GetMapping("online-device")
    public RestResponse<List<ConnectProperty>> onlineDevice() {
        return RestResponse.success(service.onlineDevice());
    }

    @GetMapping("device-message")
    public RestResponse<Message> deviceMessage(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceMessage(deviceId));
    }

    @GetMapping("device-net-status")
    public RestResponse<String> deviceNetStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceNetStatus(deviceId));
    }

    @PostMapping("device-params-conf")
    public RestResponse<Message> deviceParamsConf(@RequestBody ParamsConfMessage paramsConfMessage) {
        return RestResponse.success(service.deviceParamsConf(0, paramsConfMessage));
    }

    @PostMapping("device-params-conf/sync")
    public RestResponse<Message> deviceParamsConfSync(@RequestBody ParamsConfMessage paramsConfMessage) {
        return RestResponse.success(service.deviceParamsConf(1, paramsConfMessage));
    }

    @GetMapping("device-status")
    public RestResponse<Message> deviceStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceStatus(0, deviceId));
    }

    @GetMapping("device-status/sync")
    public RestResponse<Message> deviceStatusSync(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceStatus(1, deviceId));
    }

    @GetMapping("device-params")
    public RestResponse<Message> deviceParams(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceParams(0, deviceId));
    }

    @GetMapping("device-params/sync")
    public RestResponse<Message> deviceParamsSync(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceParams(1, deviceId));
    }

}
