package com.smart.domain.message.c2s;

import com.smart.domain.message.MessageAck;
import com.smart.domain.message.s2c.ParamsConfMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: xizhonghuai
 * @description: ParamsQueryMessageAck
 * @create: 2022-06-10 13:58
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamsQueryMessageAck extends MessageAck {
    private List<ParamsConfMessage.Body> data;
    //STATUS
    private String waterLevel;
    private String dragAmount;
    private Integer runningTime;
    private Integer normallyClosedDuration;
    private Integer normallyOpenDuration;
    private String dateTime;
   /* @Data
    public static class Body {
        //PARAMS
        private Integer channel;
        private Integer motorDelay;
        private List<ParamsConfMessage.Time> timeList;
    }*/
}
