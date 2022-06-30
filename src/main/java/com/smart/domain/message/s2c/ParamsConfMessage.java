package com.smart.domain.message.s2c;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: xizhonghuai
 * @description: ParamsConfMessage
 * @create: 2022-06-10 10:32
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamsConfMessage extends Message implements S2cMessage {
    private String deviceId;
    private List<Body> params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer channel;
        private Integer motorDelay;
        private List<Time> timeList;

    }

    @Data
    @Accessors(chain = true)
    public static class Time {
        private Integer id;
        private String start;
        private Integer delay;
        private Integer onTime;
        private Integer offTime;
    }

    @Override
    public String toString() {
        return pack();
    }
}
