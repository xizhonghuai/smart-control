package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: RevStopDTO
 * @create: 2022-07-04 14:54
 **/
@Data
@Accessors(chain = true)
public class RevStopDTO {
    private String deviceId;
    private Boolean power;
}
