package com.smart.domain;

import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.ParamsConfMessageAck;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: xizhonghuai
 * @description: VirtualHandler
 * @create: 2022-06-30 09:58
 **/
@Slf4j
public class VirtualHandler extends IoHandlerAdapter {

    private static ParamsConfMessage defaultConfig = new ParamsConfMessage();

    /*static {
        defaultConfig.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        List<ParamsConfMessage.Body> bodies = new ArrayList<>();
        bodies.add(new ParamsConfMessage.Body().setChannel(1)
                .setMotorDelay(20)
                .setTimeList(new ArrayList<ParamsConfMessage.Time>() {{
                    add(new ParamsConfMessage.Time().setId(1)
                            .setOnTime(20)
                            .setOffTime(30)
                            .setDelay(20)
                            .setStart(Utils.getDate()));
                }}));
        bodies.add(new ParamsConfMessage.Body().setChannel(2)
                .setMotorDelay(10)
                .setTimeList(new ArrayList<ParamsConfMessage.Time>() {{
                    add(new ParamsConfMessage.Time().setId(1)
                            .setOnTime(20)
                            .setOffTime(30)
                            .setDelay(20)
                            .setStart(Utils.getDate()));
                }}));
        defaultConfig.setParams(bodies);
    }*/

    //会话创建时触发
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("session  Created");
        session.setAttribute("paramsConfig", defaultConfig);
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
            session.setAttribute("paramsConfig", rec);
            ParamsConfMessageAck ack = new ParamsConfMessageAck();
            ack.setCode(MessageUtil.getMessageCode(ParamsConfMessageAck.class));
            ack.setResult(1);
            session.write(VirtualDevice.toPack(ack));
            return;
        }

     /*   if (Objects.equals(MessageUtil.getMessageCode(ParamsQueryMessage.class), code)) {
            ParamsQueryMessageAck ack = new ParamsQueryMessageAck();
            ack.setCode(MessageUtil.getMessageCode(ParamsQueryMessageAck.class));
            ack.setResult(1);
            ParamsConfMessage paramsConfig = (ParamsConfMessage) session.getAttribute("paramsConfig");
            ack.setData(paramsConfig.getParams());
            session.write(VirtualDevice.toPack(ack));
            return;
        }*/
/*
        if (Objects.equals(MessageUtil.getMessageCode(StatusQueryMessage.class), code)) {
            StatusQueryMessageAck ack = new StatusQueryMessageAck();
            ack.setCode(MessageUtil.getMessageCode(StatusQueryMessageAck.class));
            ack.setResult(1);
            ack.setDateTime(Utils.getDate());
            ack.setDragAmount("30");
            ack.setWaterLevel("normal");
            ack.setRunningTime(9);
            session.write(VirtualDevice.toPack(ack));
        }*/


    }
}
