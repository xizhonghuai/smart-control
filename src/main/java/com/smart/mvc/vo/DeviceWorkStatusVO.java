package com.smart.mvc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: DeviceWorkStatus
 * @create: 2022-07-21 13:59
 **/
@Data
@Accessors(chain = true)
public class DeviceWorkStatusVO {
    private Long secondsRemaining;
    private String planName;
    private String timeDesc;
}
