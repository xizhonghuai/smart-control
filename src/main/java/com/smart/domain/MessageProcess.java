package com.smart.domain;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.smart.cache.CacheService;
import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.RegisterMessageAck;
import com.smart.domain.message.s2c.TimingMessageAck;
import com.smart.mvc.service.CommandPoolService;
import com.smart.mvc.service.DeviceServiceImpl;
import com.smart.utils.Utils;
import com.transmission.server.core.IotSession;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xizhonghuai
 * @description: MessageProcess
 * @create: 2022-06-11 09:41
 **/
@Slf4j
public class MessageProcess {
    private final CacheService cacheService = CacheService.getCacheService();
    private final CommandPoolService commandPoolService = CommandPoolService.getCommandPoolService();
    private final DeviceServiceImpl deviceService = DeviceServiceImpl.get();

    public void accept(IotSession iotSession, Object message) {
        Message messageObject = MessageUtil.parse(message);
        if (!StrUtil.isBlank(iotSession.getDeviceId())) {
            String code = messageObject.getCode();
            cacheService.setValue(String.format("%s,%s", iotSession.getDeviceId(), code), messageObject);
            cacheService.setValue(iotSession.getDeviceId(), messageObject);
            commandPoolService.remove(iotSession.getDeviceId(), code);
        }
        if (messageObject instanceof RegisterMessage) {
            RegisterMessage registerMessage = (RegisterMessage) messageObject;
            String deviceId = registerMessage.getDeviceId();
            iotSession.setDeviceId(deviceId);
            deviceService.addDevice(deviceId);
            //ack
            RegisterMessageAck registerMessageAck = new RegisterMessageAck();
            registerMessageAck.setCode(MessageUtil.getMessageCode(RegisterMessageAck.class));
            iotSession.sendMsg(registerMessageAck);
            log.info(deviceId + " 注册");
            return;
        }
        if (messageObject instanceof TimingMessage) {
            TimingMessage timingMessage = (TimingMessage) messageObject;
            TimingMessage.Body params = timingMessage.getParams();
            // TODO: 2022/7/4  
            //ack
            TimingMessageAck timingMessageAck = new TimingMessageAck();
            timingMessageAck.setCode(MessageUtil.getMessageCode(TimingMessageAck.class));
            timingMessageAck.setResult(1);
            timingMessageAck.setDateTime(Utils.getDate());
            iotSession.sendMsg(timingMessageAck);
            return;
        }
        if (messageObject instanceof ParamsConfMessageAck) {
            ParamsConfMessageAck paramsConfMessageAck = (ParamsConfMessageAck) messageObject;
            log.info(JSON.toJSONString(paramsConfMessageAck));
            return;
        }
        if (messageObject instanceof ParamsQueryMessageAck) {
            ParamsQueryMessageAck paramsQueryMessageAck = (ParamsQueryMessageAck) messageObject;
            log.info(JSON.toJSONString(paramsQueryMessageAck));
            return;
        }
        if (messageObject instanceof RevStopMessageAck) {
            RevStopMessageAck revStopMessageAck = (RevStopMessageAck) messageObject;
            log.info(JSON.toJSONString(revStopMessageAck));
            return;
        }
        if (messageObject instanceof KeyEnablingMessageAck) {
            KeyEnablingMessageAck keyEnablingMessageAck = (KeyEnablingMessageAck) messageObject;
            log.info(JSON.toJSONString(keyEnablingMessageAck));
            return;
        }
        if (messageObject instanceof WarningEventMessage) {
            WarningEventMessage warningEventMessage = (WarningEventMessage) messageObject;
            //todo
            log.info(JSON.toJSONString(warningEventMessage));
        }
    }
}
