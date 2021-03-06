package com.smart.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringUtil
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContextParam) throws BeansException {
        applicationContext=applicationContextParam;
    }

    public static <T> T getObject(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String id) {
        return applicationContext.getBean(id);
    }

    public <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}