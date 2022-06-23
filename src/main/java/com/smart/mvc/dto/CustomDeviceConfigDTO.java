package com.smart.mvc.dto;

import com.smart.domain.message.s2c.ParamsConfMessage;
import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: CustomDeviceConfigDTO
 * @create: 2022-06-23 13:20
 **/
@Data
public class CustomDeviceConfigDTO {
    private String configName;
    private ParamsConfMessage confingCmd;
}
