package com.smart.mvc.service;

import com.smart.cache.CacheService;
import com.smart.communication.DeviceAPI;
import com.smart.config.AuthContext;
import com.smart.config.SpringUtil;
import com.smart.domain.message.Message;
import com.smart.utils.Utils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: xizhonghuai
 * @description: CommandTempService
 * @create: 2022-06-20 13:01
 **/
@Service
public class CommandPoolService implements SchedulingConfigurer {
    public static CommandPoolService getCommandPoolService() {
        return SpringUtil.getObject(CommandPoolService.class);
    }

    private final static String CRON = "*/5 * * * * ?";//每5秒
    private final static String COMMAND_TEMP_TABLE = "command_temp_table";
    @Autowired
    private CacheService cacheService;
    private final DeviceAPI deviceAPI = new DeviceAPI();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::task, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(CRON);
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }

    @Data
    public static class Command {
        private Long id;
        private String deviceId;
        private Message message;
        private int numberRetries;
        private Long operationId;
        private Date createdDate;
    }

    public synchronized void add(String deviceId, Message message) {
        if (message == null) {
            return;
        }
        List<Command> commands = getCommands();
        Command command = new Command();
        command.setId(Utils.createSnowflakeId());
        command.setDeviceId(deviceId);
        command.setOperationId(AuthContext.get().getLoginUserId());
        command.setMessage(message);
        command.setCreatedDate(new Date());
        command.setNumberRetries(0);
        commands.add(command);
    }

    public synchronized void remove(String deviceId, String code) {
        List<Command> commands = getCommands();
        commands = commands.stream().filter(v -> !(Objects.equals(v.getDeviceId(), deviceId) && Objects.equals(v.getMessage().getCode(), code)))
                .collect(Collectors.toList());
        cacheService.setValues(COMMAND_TEMP_TABLE, commands);
    }


    public synchronized List<Command> getCommands() {
        List<Command> commands = cacheService.getValues(COMMAND_TEMP_TABLE, Command.class);
        if (commands == null) {
            List<Command> commandArrayList = new ArrayList<>();
            cacheService.setValues(COMMAND_TEMP_TABLE, commandArrayList);
            return commandArrayList;
        }
        return commands;
    }

    private void task() {
        List<Command> commands = getCommands();
        commands.forEach(command -> {
            command.setNumberRetries(command.getNumberRetries() + 1);
            if (command.getNumberRetries() > 10) {
                remove(command.deviceId, command.getMessage().getCode());
                return;
            }
            deviceAPI.sendCmd(command.getDeviceId(), command.getMessage());
        });
    }


}
