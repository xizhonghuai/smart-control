package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: QueryDeciveDTO
 * @create: 2022-06-20 10:56
 **/
@Data
@Accessors(chain = true)
public class QueryDeviceDTO {
    private String deviceId;
    private String name;
    private String imei;
    private Integer netStatus;
}
