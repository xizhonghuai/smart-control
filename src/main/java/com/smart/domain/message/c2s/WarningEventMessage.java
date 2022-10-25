package com.smart.domain.message.c2s;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: EventMessage
 * @create: 2022-07-04 14:10
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WarningEventMessage extends Message {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        @JSONField(name = "event_id")
        private Integer eventId;
        private String msg;
    }

    /**
     * 0：药量低于5%
     * 1：水位低
     * 2：药量恢复正常
     * 3：水位恢复正常
     * @return
     */
    public String getWarningMsg() {
        if (getParams().eventId == 0) {
            return "药量低于5%";
        }
        if (getParams().eventId == 1) {
            return "低水位告警，请检查水位！";
        }
        if (getParams().eventId == 2) {
            return "药量已恢复正常";
        }
        if (getParams().eventId == 3) {
            return "水位已恢复正常";
        }
        return null;
    }
}
