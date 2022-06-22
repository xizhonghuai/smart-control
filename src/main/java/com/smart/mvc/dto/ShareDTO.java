package com.smart.mvc.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: xizhonghuai
 * @description: AddShareDTO
 * @create: 2022-06-20 14:51
 **/
@Data
public class ShareDTO {
    @NotNull
    private Long toAccountId;
    private List<Long> deviceIds;
}
