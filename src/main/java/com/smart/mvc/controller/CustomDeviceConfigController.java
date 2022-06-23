package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.CustomDeviceConfigDTO;
import com.smart.mvc.dto.SendCustomDeviceConfigDTO;
import com.smart.mvc.entity.CustomDeviceConfig;
import com.smart.mvc.service.impl.CustomDeviceConfigServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * CUSTOM_DEVICE_CONFIG 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-23
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/custom-device-config")
@Api(tags = "场景模式")
public class CustomDeviceConfigController {
    @Autowired
    private CustomDeviceConfigServiceImpl customDeviceConfigService;

    @GetMapping("list")
    @ApiOperation("场景模式列表")
    public RestResponse<List<CustomDeviceConfig>> list(@RequestParam("name") String name) {
        return RestResponse.success(customDeviceConfigService.list(name));
    }

    @PostMapping("add")
    @ApiOperation("新增")
    public RestResponse<Boolean> add(@RequestBody CustomDeviceConfigDTO dto) {
        return RestResponse.success(customDeviceConfigService.add(dto));
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public RestResponse<Boolean> delete(@PathVariable("id") Long id) {
        return RestResponse.success(customDeviceConfigService.delete(id));
    }

    @PostMapping("send-custom-device-config")
    @ApiOperation("发送配置到设备")
    public RestResponse<Boolean> sendCustomDeviceConfig(@RequestBody SendCustomDeviceConfigDTO dto) {
        return RestResponse.success(customDeviceConfigService.sendCustomDeviceConfig(dto));
    }


}
