package com.smart.mvc.vo;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: DeviceVO
 * @create: 2022-06-20 10:49
 **/
@Data
public class DeviceVO {
    private Long id;
    private String deviceId;
    private String name;
    private String imei;
    private String description;
    private int netStatus;
    private int isWarning;
    private int isShare;
    private String workStatus;
    private String workDescription;
    private String partition;
}
