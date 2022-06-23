package com.smart.mvc.dto;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: QueryDeciveDTO
 * @create: 2022-06-20 10:56
 **/
@Data
public class QueryDeviceDTO {
    private String deviceId;
    private String name;
    private String imei;
    private Integer netStatus;
}
