package com.smart.controller;

import com.smart.dto.RelayContrDTO;
import com.smart.model.RestResponse;
import com.smart.service.DeviceControlService;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping("debug")
public class DebugController {

    @Autowired
    private DeviceControlService service;

    @PostMapping("control-relay")
    public RestResponse<Boolean> controlRelay(@RequestBody RelayContrDTO dto) {
        return RestResponse.success(service.controlRelay(dto));
    }

    @GetMapping("online-device")
    public RestResponse<List<ConnectProperty>> onlineDevice() {
        return RestResponse.success(service.onlineDevice());
    }

    @GetMapping("device-message")
    public RestResponse<String> deviceMessage(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceMessage(deviceId));
    }

    @GetMapping("device-status")
    public RestResponse<String> deviceStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.deviceStatus(deviceId));
    }


}
