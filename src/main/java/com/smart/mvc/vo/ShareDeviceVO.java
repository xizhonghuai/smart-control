package com.smart.mvc.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xizhonghuai
 * @description: ShareDeviceVO
 * @create: 2022-06-20 14:25
 **/
@Data
public class ShareDeviceVO {
    private Long id;
    private LocalDateTime createdDate;
    private Long fromAccountId;
    private Long toAccountId;
    private String toAccountName;
    private String deviceId;
    private Long deviceTableId;
    private Long deviceName;
    private int netStatus;
}