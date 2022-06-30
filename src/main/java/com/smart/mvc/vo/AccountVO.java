package com.smart.mvc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: AccountVO
 * @create: 2022-06-29 09:57
 **/
@Data
@Accessors(chain = true)
public class AccountVO {
    private Long id;
    private String name;
}
