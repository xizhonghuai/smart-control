package com.smart.domain.message.c2s;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.smart.domain.message.Message;
import com.smart.domain.message.s2c.ParamsConfMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: xizhonghuai
 * @description: ParamsQueryMessageAck
 * @create: 2022-07-04 14:03
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParamsQueryMessageAck extends Message {
    private ParamsConfMessage.Body data;

    public void process() {
        List<Integer> daysMap = getData().getDaysMap();
        String s1 = getData().getStartDate();
        String s2 = getData().getEndDate();
        if (StrUtil.isNotBlank(s1)) {
            getData().setStartDate("20" + s1);
        }
        if (StrUtil.isNotBlank(s2)) {
            getData().setEndDate("20" + s2);
        }
        String startDateStr = getData().getStartDate();
        String endDateStr = getData().getEndDate();
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
        getData().setExcludeDates(exDates);
        ParamsConfMessage.Body body = getData();
        List<ParamsConfMessage.Time> timeList = body.getTimeList();
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
