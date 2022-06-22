package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.AddDeviceDTO;
import com.smart.mvc.dto.EditDeviceDTO;
import com.smart.mvc.dto.QueryDeviceDTO;
import com.smart.mvc.service.DeviceServiceImpl;
import com.smart.mvc.vo.DeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * DEVICE 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/device")
@Api("设备操作")
public class DeviceController {
    @Autowired
    private DeviceServiceImpl deviceService;

    @PostMapping("add-batch")
    @ApiOperation("批量添加")
    public RestResponse<Boolean> addDevice(@RequestBody List<String> deviceIds) {
        deviceService.addDeviceBatch(deviceIds);
        return RestResponse.success(true);
    }

    @PostMapping("add")
    @ApiOperation("添加")
    public RestResponse<Long> addDevice(@RequestBody AddDeviceDTO addDeviceDTO) {
        return RestResponse.success(deviceService.addDevice(addDeviceDTO));
    }

    @PutMapping("edit")
    @ApiOperation("修改")
    public RestResponse<Boolean> editDevice(@RequestBody EditDeviceDTO editDeviceDTO) {
        return RestResponse.success(deviceService.editDevice(editDeviceDTO));
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public RestResponse<Boolean> deleteDevice(@PathVariable("id") Long id) {
        return RestResponse.success(deviceService.deleteDevice(id));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public RestResponse<List<DeviceVO>> list(QueryDeviceDTO queryDeviceDTO) {
        return RestResponse.success(deviceService.list(queryDeviceDTO));
    }
}
