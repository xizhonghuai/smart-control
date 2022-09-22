package com.smart.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.smart.config.SpringUtil;
import com.smart.mvc.service.DeviceConfigService;
import com.smart.mvc.vo.ConfPlanVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: xizhonghuai
 * @description: VerificationUtils
 * @create: 2022-09-08 17:34
 **/
public class VerificationUtils {

    public static void planDateCheck(String deviceId, DateTime startDate, DateTime endDate, Integer planId) {
        DeviceConfigService deviceConfigService = SpringUtil.getObject(DeviceConfigService.class);
        List<ConfPlanVO> srcPlans = deviceConfigService.planList(deviceId)
                .stream().filter(v -> !Objects.equals(planId, v.getId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(srcPlans)) {
            return;
        }
        for (ConfPlanVO srcPlan : srcPlans) {
            DateTime date1 = DateUtil.parse(srcPlan.getBody().getStartDate(), "yyyy-MM-dd");
            DateTime date2 = DateUtil.parse(srcPlan.getBody().getEndDate(), "yyyy-MM-dd");
            if (startDate.getTime() > date1.getTime() && startDate.getTime() < date2.getTime()) {
                throw new RuntimeException("计划结束日期与已有计划有重叠,请修改");
            }
            if (endDate.getTime() > date1.getTime() && endDate.getTime() < date2.getTime()) {
                throw new RuntimeException("计划结束日期与已有计划有重叠,请修改");
            }
            if (date1.getTime() > startDate.getTime() && date1.getTime() < endDate.getTime()) {
                throw new RuntimeException("计划结束日期与已有计划有重叠,请修改");
            }
            if (date2.getTime() > startDate.getTime() && date2.getTime() < endDate.getTime()) {
                throw new RuntimeException("计划结束日期与已有计划有重叠,请修改");
            }
            if (date1.getTime() == startDate.getTime() && date2.getTime() == endDate.getTime()) {
                throw new RuntimeException("计划结束日期与已有计划有重叠,请修改");
            }
        }
    }

}
