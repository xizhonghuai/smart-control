package com.smart.mvc.vo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.utils.Utils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author: xizhonghuai
 * @description: ConfPlanVO
 * @create: 2022-07-13 16:44
 **/
@Data
@Accessors(chain = true)
public class ConfPlanVO {
    private Integer id;
    private String name;
    private Boolean status;
    private ParamsConfMessage.Body body;

    public void process() {
        status = false;
        Date date = new Date();
        DateTime startDate = DateUtil.parse(body.getStartDate(), "yyyy-MM-dd");
        DateTime endDate = DateUtil.parse(body.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        if ((date.getTime() - startDate.getTime() > 0L) && (date.getTime() - endDate.getTime() < 0L)) {
            status = checkTime();
        }
    }

    private boolean checkTime() {
        List<ParamsConfMessage.Time> timeList = body.getTimeList();
        long count = timeList.stream().filter(v -> {
            DateTime dateTime1 = DateUtil.parse(Utils.getDate() + " " + v.getTime(), "yyyy-MM-dd HH:mm:ss");
            long endTime = dateTime1.getTime() + v.getDuration() * 1000L;
            long currentTimeMillis = System.currentTimeMillis();
            return (currentTimeMillis < endTime) && (currentTimeMillis > dateTime1.getTime());
        }).count();
        return count > 0;
    }

    public static void main(String[] args) {

        String Str = "{\n" +
                "\t\t\t\"body\":{\n" +
                "\t\t\t\t\"days_map\":[\n" +
                "\t\t\t\t\t1\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"drag_pump\":{\n" +
                "\t\t\t\t\t\"enable\":false,\n" +
                "\t\t\t\t\t\"off_delay\":10,\n" +
                "\t\t\t\t\t\"on_delay\":30,\n" +
                "\t\t\t\t\t\"speed\":0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"end_date\":\"2022-09-03\",\n" +
                "\t\t\t\t\"excludeDates\":[],\n" +
                "\t\t\t\t\"id\":1,\n" +
                "\t\t\t\t\"relays\":[\n" +
                "\t\t\t\t\t\"off\",\n" +
                "\t\t\t\t\t\"off\",\n" +
                "\t\t\t\t\t\"off\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"start_date\":\"2022-09-03\",\n" +
                "\t\t\t\t\"time_list\":[\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"duration\":10800,\n" +
                "\t\t\t\t\t\t\"mode\":0,\n" +
                "\t\t\t\t\t\t\"off_time\":60,\n" +
                "\t\t\t\t\t\t\"on_time\":60,\n" +
                "\t\t\t\t\t\t\"time\":\"01:30:30\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"valid\":1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"id\":1,\n" +
                "\t\t\t\"name\":\"计划1\",\n" +
                "\t\t\t\"status\":true\n" +
                "\t\t}";

        ConfPlanVO confPlanVO = JSON.parseObject(Str, ConfPlanVO.class);
        confPlanVO.process();
        System.out.println(confPlanVO.getStatus());
    }
}
