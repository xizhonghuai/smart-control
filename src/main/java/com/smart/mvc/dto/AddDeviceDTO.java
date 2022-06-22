package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: xizhonghuai
 * @description: AddDeviceDTO
 * @create: 2022-06-20 10:37
 **/
@Data
@Accessors(chain = true)
public class AddDeviceDTO {
    @NotNull
    private String deviceId;
    private String name;
    private String imei;
    private String description;
}
