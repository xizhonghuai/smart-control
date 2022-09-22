package com.smart.mvc.service;

import com.smart.mvc.entity.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: xizhonghuai
 * @description: InitDataService
 * @create: 2022-09-09 14:02
 **/
@Service
@EnableScheduling
@Slf4j
public class DeviceParamsReadJobService implements SchedulingConfigurer {
    private static final String CRON = "0 0/10 * * * ?";//每10分钟读取
    @Autowired
    private DeviceServiceImpl deviceService;
    @Autowired
    private DeviceControlService deviceControlService;

    private void readDeviceParams() {
        List<Device> list = deviceService.list();
        list.forEach(v -> {
            String deviceId = v.getDeviceId();
            if (!deviceControlService.isDeviceOnline(deviceId)) {
                log.error(String.format("device[%s] is off-line", deviceId));
                return;
            }
            for (int i = 1; i <= 6; i++) {
                try {
                    deviceControlService.deviceParamsV2(deviceId, i);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage());
                }
            }
        });
    }

    public void task() {
        log.info("读取设备配置");
        readDeviceParams();
        log.info("读取设备配置结束");
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::task, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(CRON);
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }
}
