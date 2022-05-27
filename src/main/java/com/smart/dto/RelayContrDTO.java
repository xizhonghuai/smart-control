package com.smart.dto;

import lombok.Data;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Data
public class RelayContrDTO {
    private String deviceId;
    private Integer sensorIndex;
    private String writeData;
}
