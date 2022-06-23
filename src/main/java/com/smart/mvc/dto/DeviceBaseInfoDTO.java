package com.smart.mvc.dto;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: DeviceBaseInfoVO
 * @create: 2022-06-23 09:12
 **/
@Data
public class DeviceBaseInfoDTO {
    private String deviceId;
    private String deviceName;
    private String imei;
    private Integer netStatus;
    private Integer isBind;
    private String agentName;
    private String ownerName;

}
