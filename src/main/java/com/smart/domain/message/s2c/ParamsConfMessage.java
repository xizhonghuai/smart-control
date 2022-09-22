package com.smart.domain.message.s2c;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: xizhonghuai
 * @description: ParamsConfMessage
 * @create: 2022-06-10 10:32
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamsConfMessage extends Message implements S2cMessage {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer id;//模式编号1-6
        private Integer valid;
        @JSONField(name = "start_date")
        private String startDate;
        @JSONField(name = "end_date")
        private String endDate;//预约日期 YYYYMMDD
        private List<String> excludeDates;
        @JSONField(name = "days_map")
        private List<Integer> daysMap;

        @JSONField(name = "drag_pump")
        private DragPump dragPump;
        private List<String> relays;
        @JSONField(name = "time_list")
        private List<Time> timeList;

    }

    @Data
    @Accessors(chain = true)
    public static class DragPump {
        private Integer speed;
        private Boolean enable;
        @JSONField(name = "on_delay")
        private Integer onDelay;
        @JSONField(name = "off_delay")
        private Integer offDelay;
    }

    @Data
    @Accessors(chain = true)
    public static class Time {
        private String time;
        private Integer duration;
//        private Integer repeat;//0：不循环，1：循环
        private Integer mode;//0：喷雾，1：喷药
        @JSONField(name = "on_time")
        private Integer onTime;//65535
        @JSONField(name = "off_time")
        private Integer offTime;//65535
    }

    public void process() {
        List<String> excludeDates = this.getParams().getExcludeDates();
        if (StrUtil.isNotBlank(this.getParams().startDate)
                && StrUtil.isNotBlank(this.getParams().endDate)) {
            DateTime startDate = DateUtil.parse(this.getParams().startDate, "yyyy-MM-dd");
            DateTime endDate = DateUtil.parse(this.getParams().endDate, "yyyy-MM-dd");
            long dif = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
            List<String> dates = new ArrayList<>();
            Date temp = startDate;
            dates.add(this.getParams().startDate);
            for (int i = 0; i < dif; i++) {
                DateTime dateTime = DateUtil.offsetDay(temp, 1);
                dates.add(DateUtil.format(dateTime, "yyyy-MM-dd"));
                temp = dateTime;
            }
            List<Integer> collect = dates.stream()
                    .map(date -> CollectionUtil.isEmpty(excludeDates) ? 1 : (excludeDates.contains(date) ? 0 : 1))
                    .collect(Collectors.toList());
            this.getParams().setDaysMap(collect);
            this.getParams().setExcludeDates(null);
        }
        List<Time> timeList = getParams().getTimeList();
        if (CollectionUtil.isNotEmpty(timeList)) {
            timeList.stream().filter(Objects::nonNull).forEach(v -> {
                String time = v.getTime();
                if (time != null) {
                    v.setTime(time.replace(":", ""));
                }
            });
        }
        String startDateStr = getParams().getStartDate();
        String endDateStr = getParams().getEndDate();
        if (StrUtil.isNotBlank(startDateStr)) {
            getParams().setStartDate(startDateStr.substring(2));
        }
        if (StrUtil.isNotBlank(endDateStr)) {
            getParams().setEndDate(endDateStr.substring(2));
        }
    }

    @Override
    public String toString() {
        return pack();
    }

    public static void main(String[] args) {
        ParamsConfMessage paramsConfMessage = new ParamsConfMessage();
        paramsConfMessage.setParams(new Body().setStartDate("22-07-01")
                .setEndDate("22-07-09"));
        ;
        System.out.println(paramsConfMessage);
    }
}
