package com.smart.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.config.AuthContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author: xizhonghuai
 * @description: Utils
 * @create: 2022-06-11 10:08
 **/
public class Utils {
    public static String getDate() {
        return DateUtil.format(new Date(), "yyyy-MM-dd");
    }

    public static String getDateV2() {
        return DateUtil.format(new Date(), "yy-MM-dd");
    }

    public static String getDateTime() {
        return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static <T> Wrapper<T> queryWrapper(T t) {
        return Wrappers.query(t);
    }

    public static <T> LambdaQueryWrapper<T> lambdaQuery(Class<T> tClass) {
        return Wrappers.lambdaQuery(tClass);
    }

    public static <T> Wrapper<T> updateWrapper(T t) {
        return Wrappers.update(t);
    }

    public static <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> tClass) {
        return Wrappers.lambdaUpdate(tClass);
    }


    public static Long createSnowflakeId() {
        return IdUtil.getSnowflakeNextId();
    }

    public static <T> void insertBeforeAction(T t) {
        BeanUtil.setProperty(t, "id", createSnowflakeId());
        updateBeforeAction(t);
    }

    public static <T> void insertBeforeAction(List<T> list) {
        list.forEach(Utils::insertBeforeAction);
    }

    public static <T> void updateBeforeAction(T t) {
        Long operationId = AuthContext.get().getLoginUserId();
        BeanUtil.setProperty(t, "createdBy", operationId);
        BeanUtil.setProperty(t, "updatedBy", operationId);
        BeanUtil.setProperty(t, "createdDate", new Date());
        BeanUtil.setProperty(t, "updatedDate", new Date());
    }


    public static class SyncResult<T> {
        private volatile int breakFlag = 0;
        private long timeOut = 10000L;

        public SyncResult() {
        }

        public SyncResult(long timeOut) {
            this.timeOut = timeOut;
        }

        private Thread timeMeter() {
            Thread thread = new Thread(() -> {
                long number = timeOut / 10;
                for (int i = 0; i < number; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                breakFlag = 1;
            });
            thread.start();
            return thread;
        }

        public T get(Supplier<T> supplier) {
            T result;
            Thread thread = timeMeter();
            do {
                result = supplier.get();
            } while (result == null && breakFlag == 0);
            thread.stop();
            if (result == null) {
                throw new RuntimeException("Get results timeout");
            }
            return result;
        }
    }

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
    public static long getWorkerIdByIP(int bits)  {
        int shift = 64 - bits;
        try {
            InetAddress address = InetAddress.getLocalHost();
            long ip = IpUtils.ipV4ToLong(address.getHostAddress());
            long workerId = (ip << shift) >>> shift;
            return workerId;
        } catch (UnknownHostException e) {
          //  LOGGER.error("Generate unique id exception. ", e);
         throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {


        System.out.println(getWorkerIdByIP(16));

    }

}
