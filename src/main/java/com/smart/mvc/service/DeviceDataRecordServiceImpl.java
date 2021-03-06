package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.mvc.entity.DeviceDataRecord;
import com.smart.mvc.mapper.DeviceDataRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * DEVICE_DATA_RECORD 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class DeviceDataRecordServiceImpl extends ServiceImpl<DeviceDataRecordMapper, DeviceDataRecord> implements IService<DeviceDataRecord> {

}
