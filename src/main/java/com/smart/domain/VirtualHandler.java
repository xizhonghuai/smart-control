package com.smart.domain;

import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.ParamsConfMessageAck;
import com.smart.domain.message.c2s.ParamsQueryMessageAck;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.domain.message.s2c.ParamsQueryMessage;
import com.smart.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author: xizhonghuai
 * @description: VirtualHandler
 * @create: 2022-06-30 09:58
 **/
@Slf4j
public class VirtualHandler extends IoHandlerAdapter {

    //会话创建时触发
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("session  Created");
        ParamsQueryMessageAck params = new ParamsQueryMessageAck();
        params.setCode(MessageUtil.getMessageCode(ParamsQueryMessageAck.class));
        params.setDeviceId(VirtualDevice.deviceId);
        params.setData(new ParamsConfMessage.Body()
                .setId(1)
                .setStartDate(Utils.getDateV2())
                .setEndDate(Utils.getDateV2())
                .setDaysMap(new ArrayList<Integer>() {{
                    add(1);
                }})
                .setDragPump(new ParamsConfMessage.DragPump()
                        .setEnable(true)
                        .setOnDelay(2)
                        .setOffDelay(3)
                        .setSpeed(50))

                .setRelays(new ArrayList<String>() {{
                    add("off");
                    add("off");
                    add("on");
                }})
                .setTimeList(new ArrayList<ParamsConfMessage.Time>() {{
                    add(new ParamsConfMessage.Time().setTime("140000").setOnTime(10).setOffTime(10).setDuration(500).setRepeat(1));
                    add(new ParamsConfMessage.Time().setTime("000000").setOnTime(3600).setOffTime(0).setDuration(3600).setRepeat(0));
                    add(new ParamsConfMessage.Time().setTime("162533").setOnTime(10).setOffTime(50).setDuration(180).setRepeat(1));
                    add(new ParamsConfMessage.Time().setTime("172533").setOnTime(90).setOffTime(20).setDuration(180).setRepeat(1));
                }})
        );
        session.setAttribute("params_" + params.getData().getId(), params);
    }

    @Override
    //会话打开时触发（第一次连接时先触发sessionCreated函数，后触发本函数）
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("虚拟设备收到数据:\r\n" + message);
        MessageUtil.verify(message);
        Message rec = MessageUtil.parse(message);
        String code = rec.getCode();
        if (Objects.equals(MessageUtil.getMessageCode(ParamsConfMessage.class), code)) {
            ParamsConfMessage paramsConfMessage = (ParamsConfMessage) rec;
            ParamsQueryMessageAck params = (ParamsQueryMessageAck) session.getAttribute("params_" + paramsConfMessage.getParams().getId());
            if (params == null) {
                params = new ParamsQueryMessageAck();
            }
            params.setData(paramsConfMessage.getParams());
            session.setAttribute("params_" + paramsConfMessage.getParams().getId());

            ParamsConfMessageAck ack = new ParamsConfMessageAck();
            ack.setCode(MessageUtil.getMessageCode(ParamsConfMessageAck.class));
            ack.setDeviceId(VirtualDevice.deviceId);
            ack.setResult(0);
            session.write(VirtualDevice.toPack(ack));
            return;
        }

        if (Objects.equals(MessageUtil.getMessageCode(ParamsQueryMessage.class), code)) {
            ParamsQueryMessage queryMessage = (ParamsQueryMessage) rec;
            ParamsQueryMessageAck params = (ParamsQueryMessageAck) session.getAttribute("params_" + queryMessage.getParams().getId());
            session.write(VirtualDevice.toPack(params));
        }


    }
}
