package com.smart.domain.message;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.*;
import com.smart.utils.Utils;

import java.util.*;

/**
 * @author xizhonghuai
 * @date 2022/05/23
 */
public class MessageUtil {
    public static Map<String, Class<?>> messageTypeMap = new HashMap<String, Class<?>>() {
        {
            put("10000", RegisterMessage.class);
            put("20000", RegisterMessageAck.class);
            put("10001", TimingMessage.class);
            put("20001", TimingMessageAck.class);
            put("20002", ParamsConfMessage.class);
            put("10002", ParamsConfMessageAck.class);
            put("20003", ParamsQueryMessage.class);
            put("10003", ParamsQueryMessageAck.class);
          /*  put("20004", ParamsQueryMessage.class);
            put("10004", ParamsQueryMessageAck.class);*/

            put("20006", RevStopMessage.class);
            put("10006", RevStopMessageAck.class);
            put("20007", SolenoidValveMessage.class);
            put("10007", SolenoidValveMessageAck.class);
            put("20008", WarningEventMessage.class);
            put("10008", WarningEventMessageAck.class);
        }
    };

    public static String getMessageCode(Class<?> classz) {
        Optional<Map.Entry<String, Class<?>>> optional = messageTypeMap.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), classz)).findAny();
        return optional.map(Map.Entry::getKey).orElse(null);
    }


    public static Class<?> getMessageClass(String text) {
        Map<String, Object> map = BeanUtil.beanToMap(JSON.parse(text), false, false);

        Object code = map.get("code");
        return code == null ? null : messageTypeMap.get(code.toString());
    }

    public static Message parse(Object message) {
        verify(message);
        String jsonString = message.toString().substring(1, message.toString().length() - 1);
        Class<?> messageClass = getMessageClass(jsonString);
        if (messageClass == null) {
            throw new RuntimeException("Unsupported protocols");
        }
        Map<String, Object> map = BeanUtil.beanToMap(JSON.parse(jsonString));
        return (Message) BeanUtil.mapToBean(map, messageClass, true, null);
    }

    public static void verify(Object message) {
        String toString = message.toString();
        if (toString.length() > 2 && toString.charAt(0) == 0x02 && toString.charAt(toString.length() - 1) == 0x03) {
            return;
        }
        throw new RuntimeException("message error");
    }

    public static boolean isHeader(Object message) {
        String toString = message.toString();
        return toString.length() >= 2 && toString.charAt(0) == 0x02;
    }

    public static boolean isIntact(Object message) {
        String toString = message.toString();
        return (toString.length() > 2 && toString.charAt(0) == 0x02 && toString.charAt(toString.length() - 1) == 0x03);
    }

    public static ParamsConfMessage defaultConfig(Integer id, String deviceId) {
        ParamsConfMessage confMessage = new ParamsConfMessage();
        confMessage.setDeviceId(deviceId);
        confMessage.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        ParamsConfMessage.Body body = new ParamsConfMessage.Body()
                .setId(id)
                .setStartDate(Utils.getDate())
                .setEndDate(Utils.getDate())
                .setDaysMap(new ArrayList<Integer>() {{
                    add(1);
                }})
                .setDragPump(new ParamsConfMessage.DragPump()
                        .setEnable(false)
                        .setOnDelay(30)
                        .setOffDelay(10)
                        .setSpeed(0))
                .setRelays(new ArrayList<String>() {{
                    add("off");
                    add("off");
                    add("off");
                }})
                .setTimeList(new ArrayList<ParamsConfMessage.Time>() {{
                    add(new ParamsConfMessage.Time().setTime("08:00:00").setOnTime(60).setOffTime(60).setDuration(3600).setRepeat(1));
                }});
        confMessage.setParams(body);
        return confMessage;
    }
}
