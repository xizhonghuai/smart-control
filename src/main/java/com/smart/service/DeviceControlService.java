package com.smart.service;

import com.smart.cache.CacheService;
import com.smart.debug.DeviceDebug;
import com.smart.domain.Message;
import com.smart.dto.RelayContrDTO;
import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.ConnectProperty;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Service
public class DeviceControlService {

    @Autowired
    private DeviceDebug deviceDebug;
    @Autowired
    private CacheService cacheService;


    public boolean controlRelay(RelayContrDTO dto) {
        String status = deviceStatus(dto.getDeviceId());
        if ("off-line".equals(status)) {
            throw new RuntimeException("The device is offline");
        }
        Message message = new Message((char) 0x71, Message.WRITE, (char) 0x01, (char) dto.getSensorIndex().intValue());
        message.setPayLoad(dto.getWriteData());
        deviceDebug.sendCmd("smart-control", dto.getDeviceId(), message);
        return true;
    }

    public List<ConnectProperty> onlineDevice() {
        return deviceDebug.getOnlineDeviceList("smart-control");
    }

    public String deviceMessage(String deviceId) {
        return cacheService.isEmpty(deviceId) ? "no data" : (String) cacheService.getValue(deviceId);
    }

    public String deviceStatus(String deviceId) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer("smart-control");
        Map<Long, IoSession> managedSessions = server.getManagedSessions();
        IoSession ioSession = WriteMsgUtils.regIdToSession(managedSessions, deviceId);
        return ioSession == null ? "off-line" : "on-line";
    }
}
