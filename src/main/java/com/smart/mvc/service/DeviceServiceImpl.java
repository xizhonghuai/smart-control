package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.mvc.entity.Device;
import com.smart.mvc.mapper.DeviceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
