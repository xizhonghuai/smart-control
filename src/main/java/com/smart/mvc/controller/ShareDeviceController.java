package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.AddShareDTO;
import com.smart.mvc.dto.DeleteShareDTO;
import com.smart.mvc.service.ShareDeviceServiceImpl;
import com.smart.mvc.vo.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * SHARE_DEVICE 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/share-device")
@Api(tags = "共享设备")
public class ShareDeviceController {
    @Autowired
    private ShareDeviceServiceImpl shareDeviceService;

    @GetMapping("share-user-list")
    @ApiOperation("当前设备共享的用户")
    public RestResponse<List<AccountVO>> shareUserList(@RequestParam("deviceId") Long deviceId) {
        return RestResponse.success(shareDeviceService.shareUserList(deviceId));
    }

    @PostMapping("add-share")
    @ApiOperation("新增共享")
    public RestResponse<Boolean> addShare(@RequestBody AddShareDTO addShareDTO) {
        return RestResponse.success(shareDeviceService.addShare(addShareDTO));
    }

    @DeleteMapping("delete-share")
    @ApiOperation("删除共享")
    public RestResponse<Boolean> deleteShare(@RequestBody DeleteShareDTO deleteAddShareDTO) {
        return RestResponse.success(shareDeviceService.deleteShare(deleteAddShareDTO));
    }



}
