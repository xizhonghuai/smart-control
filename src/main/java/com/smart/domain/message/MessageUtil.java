package com.smart.domain.message;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
            put("20004", StatusQueryMessage.class);
            put("10004", StatusQueryMessageAck.class);
        }
    };

    public static String getMessageCode(Class<?> classz) {
        Optional<Map.Entry<String, Class<?>>> optional = messageTypeMap.entrySet()
                .stream().filter(entry -> Objects.equals(entry.getValue(), classz)).findAny();
        return optional.map(Map.Entry::getKey).orElse(null);
    }


    public static Class<?> getMessageClass(String text) {
        Map<String, Object> map = BeanUtil.beanToMap(JSON.parse(text), false, false);

        Object code = map.get("code");
        return code == null ? null : messageTypeMap.get(code.toString());
    }

    public static Message parse(Object message) {
        verify(message);
        String jsonString = message.toString().substring(1, message.toString().length() - 2);
        Class<?> messageClass = getMessageClass(jsonString);
        if (messageClass == null) {
            throw new RuntimeException("Unsupported protocols");
        }
        Map<String, Object> map = BeanUtil.beanToMap(JSON.parse(jsonString));
        return (Message) BeanUtil.mapToBean(map, messageClass, true, null);
    }

    public static void verify(Object message) {
        String toString = message.toString();
        if (toString.length() > 2 && toString.charAt(0) == 0x02 && toString.charAt(toString.length() - 2) == 0x03) {
            return;
        }
        throw new RuntimeException("message error");
    }
}
