package com.smart.mvc.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.mvc.dto.AddDeviceDTO;
import com.smart.mvc.dto.EditDeviceDTO;
import com.smart.mvc.dto.QueryDeviceDTO;
import com.smart.mvc.entity.Device;
import com.smart.mvc.mapper.DeviceMapper;
import com.smart.mvc.vo.DeviceVO;
import com.smart.utils.Utils;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * DEVICE 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IService<Device> {
    @Autowired
    private AccountDeviceXrefServiceImpl accountDeviceXrefService;
    @Autowired
    private DeviceControlService deviceControlService;

    public Long addDevice(AddDeviceDTO addDeviceDTO) {
        Device device = BeanUtil.toBean(addDeviceDTO, Device.class);
        Utils.insertBeforeAction(device);
        save(device);
        return device.getId();
    }

    public Boolean editDevice(EditDeviceDTO editDeviceDTO) {
        Long id = editDeviceDTO.getId();
        accountDeviceXrefService.deviceAuthVerify(id);
        Device device = BeanUtil.toBean(editDeviceDTO, Device.class);
        Utils.updateBeforeAction(device);
        return update(Utils.updateWrapper(device));
    }

    public Boolean deleteDevice(Long id) {
        accountDeviceXrefService.deviceAuthVerify(id);
        return removeById(id);
    }


    public List<DeviceVO> list(QueryDeviceDTO queryDeviceDTO) {
        List<Device> list = list(Utils.queryWrapper(BeanUtil.toBean(queryDeviceDTO, Device.class)));
        List<Device> devices = accountDeviceXrefService.authFilter(list, true);
        List<DeviceVO> deviceVOS = BeanUtil.copyToList(devices, DeviceVO.class);
        List<ConnectProperty> connectProperties = deviceControlService.onlineDevice();
        List<String> olineRegIds = connectProperties.stream().map(ConnectProperty::getRegId).collect(Collectors.toList());
        List<Long> shareIds = accountDeviceXrefService.shareToMe();
        return deviceVOS.stream().peek(deviceVO -> {
            deviceVO.setNetStatus(olineRegIds.contains(deviceVO.getDeviceId()) ? 1 : 0);
            deviceVO.setIsShare(shareIds.contains(deviceVO.getId()) ? 1 : 0);
        }).collect(Collectors.toList());
    }


}
