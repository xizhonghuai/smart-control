package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: ConfPlanNameEditDTO
 * @create: 2022-07-19 16:13
 **/
@Data
@Accessors(chain = true)
public class ConfPlanNameEditDTO {
    private String deviceId;
    private Integer planId;
    private String name;
}
