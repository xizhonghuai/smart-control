package com.smart.communication;

import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.ConnectProperty;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DeviceManage
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
public class DeviceAPI {
    private static final String serviceId = "smart-control";


    public void sendCmd(Object cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null) {
            WriteMsgUtils.sendMsg(server.getManagedSessions(), cmd);
        }
    }

    public void sendCmd(String redId, Object cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null && cmd != null) {
            WriteMsgUtils.sendMsg(server.getManagedSessions(), cmd, redId);
            log.info(String.format("下发数据:{%s}->{%s}", redId, cmd));
        }
    }


    public void disconnect(String serviceId, String regId) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null) {
            IoSession ioSession = WriteMsgUtils.regIdToSession(server.getManagedSessions(), regId);
            if (ioSession != null) ioSession.close(true);
        }
    }


    public List<ConnectProperty> getOnlineDeviceList(String serviceId) {
        if (null != serviceId) {
            AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
            if (server != null) {
                List<ConnectProperty> connectPropertyList = new ArrayList<>();
                Map<Long, IoSession> managedSessions = server.getManagedSessions();
                managedSessions.forEach((aLong, ioSession) -> {
                    if (ioSession.getAttribute("regId") != null) {
                        ConnectProperty connectProperty = new ConnectProperty();
                        connectProperty.setRegId((String) ioSession.getAttribute("regId"));
                        connectProperty.setAddress(ioSession.getRemoteAddress().toString());
                        connectProperty.setRegisterTime(new Date(ioSession.getCreationTime()));
                        connectProperty.setActivityTime(new Date(ioSession.getLastReadTime()));
                        connectPropertyList.add(connectProperty);
                    }
                });
                return connectPropertyList;
            }
        }
        return null;
    }


    public List<ConnectProperty> getOnlineDeviceList() {
        ConcurrentHashMap<String, Object> servers = (ConcurrentHashMap<String, Object>) ServerUtils.getServers();
        List<ConnectProperty> connectPropertyList = new ArrayList<>();
        servers.forEach((id, o) -> {
            AbstractBootServer server = (AbstractBootServer) o;
            Map<Long, IoSession> managedSessions = server.getManagedSessions();
            managedSessions.forEach((aLong, ioSession) -> {
                ConnectProperty connectProperty = (ConnectProperty) ioSession.getAttribute("connectProperty");
                if (connectProperty != null && connectProperty.getRegId() != null) {
                    connectPropertyList.add(connectProperty);
                }
            });
        });
        return connectPropertyList;
    }


}
