package com.smart.domain.message.c2s;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: TimingMessage
 * @create: 2022-06-10 10:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TimingMessage extends Message {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer csq;
        @JSONField(name = "total_drag_amount")
        private Integer totalDragAmount;
        @JSONField(name = "total_work_time")
        private Integer totalWorkTime;
        @JSONField(name = "drag_amount")
        private String dragAmount;//药量信息，百分比
        @JSONField(name = "water_level")
        private Integer waterLevel;//1正常，0缺水
        @JSONField(name = "running_time")
        private Integer runningTime;//累计运行时间
        private String state;//运行状态 （自动、常开、常关）
        @JSONField(name = "error_message")
        private String errorMessage; //异常信息

        private String mode;//0自动1手动

    }

}

