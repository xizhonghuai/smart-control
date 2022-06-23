package com.smart.mvc.vo;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: DeviceBaseInfoVO
 * @create: 2022-06-23 09:12
 **/
@Data
public class DeviceBaseInfoVO {
    private Long id;
    private String deviceId;
    private String deviceName;
    private String imei;
    private String description;
    private int netStatus;

    private Long agentId;
    private String agentName;
    private Long ownerId;
    private String ownerName;
}
