package com.smart.domain.message;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.*;

import java.nio.charset.StandardCharsets;
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


            put("20006", RevStopMessage.class);
            put("10006", RevStopMessageAck.class);
            put("20007", SolenoidValveMessage.class);
            put("10007", SolenoidValveMessageAck.class);
            put("10008", WarningEventMessage.class);
            put("20008", WarningEventMessageAck.class);
            put("20009", ImmediateParamsConfMessage.class);
            put("10009", ImmediateParamsConfMessageAck.class);
            put("20010", ParamsDeleteMessage.class);
            put("10010", ParamsDeleteMessageAck.class);
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

    public static ParamsConfMessage defaultConfig(Integer id, String deviceId, Date date) {
        ParamsConfMessage confMessage = new ParamsConfMessage();
        confMessage.setDeviceId(deviceId);
        confMessage.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        ParamsConfMessage.Body body = new ParamsConfMessage.Body()
                .setId(id)
                .setValid(1)
                .setStartDate(DateUtil.format(date, "yyyy-MM-dd"))
                .setEndDate(DateUtil.format(date, "yyyy-MM-dd"))
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
                    add(new ParamsConfMessage.Time().setTime("08:00:00").setOnTime(60).setOffTime(60).setDuration(3600).setMode(0));
                }});
        confMessage.setParams(body);
        return confMessage;
    }


    public static void c2sMsgProcess(ParamsConfMessage.Body data) {
        List<Integer> daysMap = data.getDaysMap();
        String s1 = data.getStartDate();
        String s2 = data.getEndDate();
        if (StrUtil.isNotBlank(s1)) {
            data.setStartDate("20" + s1);
        }
        if (StrUtil.isNotBlank(s2)) {
            data.setEndDate("20" + s2);
        }
        String startDateStr = data.getStartDate();
        String endDateStr = data.getEndDate();
        if (StrUtil.isBlank(startDateStr) || StrUtil.isBlank(endDateStr) || CollectionUtil.isEmpty(daysMap)) {
            return;
        }
        DateTime startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
        DateTime endDate = DateUtil.parse(endDateStr, "yyyy-MM-dd");

        long dif = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
        List<String> dates = new ArrayList<>();
        Date temp = startDate;
        dates.add(startDateStr);
        for (int i = 0; i < dif; i++) {
            DateTime dateTime = DateUtil.offsetDay(temp, 1);
            dates.add(DateUtil.format(dateTime, "yyyy-MM-dd"));
            temp = dateTime;
        }
        List<String> exDates = new ArrayList<>();
        for (int i = 0; i < daysMap.size(); i++) {
            if (daysMap.get(i) == 0) {
                exDates.add(dates.get(i));
            }
        }
        data.setExcludeDates(exDates);
        List<ParamsConfMessage.Time> timeList = data.getTimeList();
        timeList.stream().filter(Objects::nonNull).forEach(v -> {
            String time = v.getTime();
            byte[] bytes = time.getBytes(StandardCharsets.UTF_8);
            String s = new String(new byte[]{bytes[0], bytes[1], 0x3A
                    , bytes[2], bytes[3], 0x3A
                    , bytes[4], bytes[5]});
            v.setTime(s);
        });
    }


}
