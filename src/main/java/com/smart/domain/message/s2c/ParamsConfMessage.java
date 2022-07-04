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
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer id;//模式编号1-6
        private String date;//预约日期 YYYYMMDD
        private DragPump dragGump;
        private List<String> relays;
        private List<Time> timeList;

    }

    @Data
    @Accessors(chain = true)
    public static class DragPump {
        private String speed;
        private Boolean enable;
        private Integer onDelay;
        private Integer offDelay;
    }

    @Data
    @Accessors(chain = true)
    public static class Time {
        private Integer id;
        private String start;
        private Integer duration;
        private Integer onTime;
        private Integer offTime;
    }

    @Override
    public String toString() {
        return pack();
    }
}
