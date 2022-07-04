package com.smart.domain.message.c2s;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    public static class Body {
        private Integer csq;
        private String dragAmount;//药量信息，百分比
        private Integer waterLevel;//1正常，0缺水
        private Integer runningTime;//累计运行时间
        private String state;//运行状态 （自动、常开、常关）
        private String errorMessage; //异常信息
    }

}

