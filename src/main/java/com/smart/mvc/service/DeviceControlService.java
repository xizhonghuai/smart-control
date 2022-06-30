package com.smart.mvc.service;

import cn.hutool.core.util.StrUtil;
import com.smart.cache.CacheService;
import com.smart.communication.DeviceAPI;
import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.domain.message.s2c.ParamsQueryMessage;
import com.smart.domain.message.s2c.StatusQueryMessage;
import com.smart.utils.Utils;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Service
public class DeviceControlService {
    private final DeviceAPI deviceAPI = new DeviceAPI();
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CommandPoolService commandPoolService;

    public List<ConnectProperty> onlineDevice() {
        return deviceAPI.getOnlineDeviceList();
    }

    public Message deviceMessage(String deviceId, String code) {
        String key = StrUtil.isBlank(code) ? deviceId : String.format("%s,%s", deviceId, code);
        return (Message) cacheService.getValue(key);
    }

    public String deviceNetStatus(String deviceId) {
        if (deviceId == null) {
            throw new RuntimeException("device is not null");
        }
        List<ConnectProperty> connectProperties = onlineDevice();
        boolean b = connectProperties.stream().map(ConnectProperty::getRegId).anyMatch(v -> Objects.equals(deviceId, v));
        return b ? "online" : "off-line";
    }

    private void checkDevice(String deviceId) {
        if (Objects.equals(deviceNetStatus(deviceId), "off-line")) {
            throw new RuntimeException("device is off-line");
        }
    }

    public Message deviceParamsConf(int isSync, ParamsConfMessage paramsConfMessage) {
        String deviceId = paramsConfMessage.getDeviceId();
        checkDevice(deviceId);
        paramsConfMessage.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        deviceAPI.sendCmd(deviceId, paramsConfMessage);
        commandPoolService.add(deviceId, paramsConfMessage);
        if (isSync <= 0) {
            return null;
        }
        cacheService.invalid(deviceId);
        return new Utils.SyncResult<Message>().get(() -> (Message) cacheService.getValue(deviceId));
    }

    public Message deviceStatus(int isSync, String deviceId) {
        checkDevice(deviceId);
        StatusQueryMessage queryMessage = new StatusQueryMessage();
        queryMessage.setDeviceId(deviceId);
        queryMessage.setCode(MessageUtil.getMessageCode(StatusQueryMessage.class));
        deviceAPI.sendCmd(deviceId, queryMessage);
        commandPoolService.add(deviceId, queryMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(deviceId);
        return new Utils.SyncResult<Message>().get(() -> (Message) cacheService.getValue(deviceId));
    }

    public Message deviceParams(int isSync, String deviceId) {
        checkDevice(deviceId);
        ParamsQueryMessage paramsQueryMessage = new ParamsQueryMessage();
        paramsQueryMessage.setDeviceId(deviceId);
        paramsQueryMessage.setCode(MessageUtil.getMessageCode(ParamsQueryMessage.class));
        deviceAPI.sendCmd(deviceId, paramsQueryMessage);
        commandPoolService.add(deviceId, paramsQueryMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(deviceId);
        return new Utils.SyncResult<Message>().get(() -> (Message) cacheService.getValue(deviceId));
    }

    public List<CommandPoolService.Command> commandPoolList() {
        return commandPoolService.getCommands();
    }
}
