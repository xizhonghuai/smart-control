package com.smart.mvc.controller;

import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.domain.message.s2c.ParamsDeleteMessage;
import com.smart.mvc.dto.ConfPlanAddDTO;
import com.smart.mvc.dto.ConfPlanNameEditDTO;
import com.smart.mvc.service.DeviceConfigService;
import com.smart.mvc.vo.ConfPlanVO;
import com.smart.mvc.vo.DeviceWorkStatusVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "device-config")
@Api(tags = "设备配置")
public class DeviceConfigController {
    @Autowired
    private DeviceConfigService service;

    @GetMapping("plan-list")
    public RestResponse<List<ConfPlanVO>> planList(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.planList(deviceId));
    }

    @PostMapping("add-plan")
    public RestResponse<Boolean> addPlan(@RequestBody ConfPlanAddDTO dto) {
        return RestResponse.success(service.addPlan(dto));
    }

    @PostMapping("delete-plan")
    public RestResponse<Boolean> deletePlan(@RequestBody ParamsDeleteMessage paramsDeleteMessage) {
        return RestResponse.success(service.deletePlan(paramsDeleteMessage));
    }

    @PostMapping("edit-plan-name")
    public RestResponse<Boolean> editPlanName(@RequestBody ConfPlanNameEditDTO dto) {
        return RestResponse.success(service.editPlanName(dto));
    }


    @GetMapping("work_status")
    public RestResponse<DeviceWorkStatusVO> workStatus(@RequestParam("deviceId") String deviceId) {
        return RestResponse.success(service.workStatus(deviceId));
    }


}
