package com.smart.mvc.vo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.smart.domain.message.s2c.ParamsConfMessage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: xizhonghuai
 * @description: ConfPlanVO
 * @create: 2022-07-13 16:44
 **/
@Data
@Accessors(chain = true)
public class ConfPlanVO {
    private Integer id;
    private String name;
    private Boolean status;
    private ParamsConfMessage.Body body;

    public void process() {
        status = false;
        Date date = new Date();
        DateTime startDate = DateUtil.parse(body.getStartDate(), "yyyy-MM-dd");
        DateTime endDate = DateUtil.parse(body.getEndDate(), "yyyy-MM-dd");
        if ((date.getTime() - startDate.getTime() > 0L) && (date.getTime() - endDate.getTime() < 0L)) {
            status = true;
        }
    }
}
