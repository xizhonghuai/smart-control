package com.smart.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.SpringUtil;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.mvc.dto.CustomDeviceConfigDTO;
import com.smart.mvc.dto.SendCustomDeviceConfigDTO;
import com.smart.mvc.entity.CustomDeviceConfig;
import com.smart.mvc.mapper.CustomDeviceConfigMapper;
import com.smart.mvc.service.DeviceControlService;
import com.smart.mvc.service.ICustomDeviceConfigService;
import com.smart.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * CUSTOM_DEVICE_CONFIG 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-23
 */
@Service
public class CustomDeviceConfigServiceImpl extends ServiceImpl<CustomDeviceConfigMapper, CustomDeviceConfig> implements ICustomDeviceConfigService {

    public List<CustomDeviceConfig> list(String name) {
        return list(Utils.queryWrapper(new CustomDeviceConfig()
                .setConfigName(name)
                .setAccountId(AuthContext.get().getLoginUserId())));
    }

    public Boolean add(CustomDeviceConfigDTO dto) {
        CustomDeviceConfig customDeviceConfig = new CustomDeviceConfig();
        customDeviceConfig.setConfigName(dto.getConfigName());
        customDeviceConfig.setConfigCmd(dto.getConfigCmd().toString());
        customDeviceConfig.setAccountId(AuthContext.get().getLoginUserId());
        Utils.insertBeforeAction(customDeviceConfig);
        return save(customDeviceConfig);
    }

    public Boolean delete(Long id) {
        return removeById(id);
    }

    public Boolean sendCustomDeviceConfig(SendCustomDeviceConfigDTO dto) {
        Long configId = dto.getCustomDeviceConfigId();
        CustomDeviceConfig deviceConfig = getById(configId);
        String configCmd = deviceConfig.getConfigCmd();
        ParamsConfMessage paramsConfMessage = JSON.parseObject(configCmd, ParamsConfMessage.class);
        paramsConfMessage.setDeviceId(dto.getDeviceId());
        DeviceControlService deviceControlService = SpringUtil.getObject(DeviceControlService.class);
        deviceControlService.deviceParamsConf(0, paramsConfMessage);
        return true;
    }
}
