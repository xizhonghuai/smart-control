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
    private String deviceId;
    private Body params;

    @Data
    public static class Body {
        private Integer csq;
        private String dragAmount;
        private Integer waterLevel;
        private Integer runningTime;
        private String state;
        private String errorMessage;
    }

}

