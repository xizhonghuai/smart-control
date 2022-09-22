package com.smart.domain;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.RegisterMessage;
import com.smart.domain.message.c2s.TimingMessage;
import com.transmission.server.core.CodecFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: xizhonghuai
 * @description: VirtualDevice
 * @create: 2022-06-30 09:31
 **/
//@Service
public class VirtualDevice implements SchedulingConfigurer {
    private static final String CRON = "0 0/1 * * * ?";//每1分钟
    public static final String deviceId = "100001";
    private static String ip = "127.0.0.1";
    //      private static String ip = "8.131.57.109";
    private static final int port = 8889;
    private IoSession ioSession;
    public IoConnector ioConnector;


    public void connect() {
        // 创建一个非阻塞的客户端
        ioConnector = new NioSocketConnector();
        // 设置超时时间
        ioConnector.setConnectTimeoutMillis(30000);
        // 设置编码解码器
        ioConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new CodecFactory(StandardCharsets.ISO_8859_1, StandardCharsets.ISO_8859_1)));
        // 绑定逻辑处理类
        ioConnector.setHandler(new VirtualHandler());
        // 创建连接
        ConnectFuture future = ioConnector.connect(new InetSocketAddress(ip, port));
        // 等待连接创建完成
        future.awaitUninterruptibly();
        // 获取连接session
        ioSession = future.getSession();
        //reg
        RegisterMessage registerMessage = new RegisterMessage();
        registerMessage.setCode(MessageUtil.getMessageCode(RegisterMessage.class));
        registerMessage.setDeviceId(deviceId);
        registerMessage.setParams(new RegisterMessage.Body().setIccid("121325452265").setVer("1.0"));
        ioSession.write(toPack(registerMessage));
    }

    public void close() {
        ioSession.getCloseFuture().awaitUninterruptibly();
        ioConnector.dispose();
    }

    public static <T> String toPack(T t) {
        Map<String, Object> map = BeanUtil.beanToMap(t, true, false);
        byte[] s1 = {0x02};
        byte[] s2 = {0x03};
        return new String(s1) + JSON.toJSONString(map) + new String(s2);
    }


    public void timing() {
        if (ioSession == null) {
            connect();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        TimingMessage timingMessage = new TimingMessage();
        timingMessage.setCode(MessageUtil.getMessageCode(TimingMessage.class));
        timingMessage.setDeviceId(deviceId);
        timingMessage.setParams(new TimingMessage.Body()
                .setCsq(12)
                .setDragAmount("5")
                .setState("running")
                .setWaterLevel(1)
                .setTotalDragAmount(123234)
                .setTotalWorkTime(234551)
                .setRunningTime(56));
        ioSession.write(VirtualDevice.toPack(timingMessage));
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::timing, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(CRON);
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }
}
