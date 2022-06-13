package com.smart.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.function.Supplier;

/**
 * @author: xizhonghuai
 * @description: Utils
 * @create: 2022-06-11 10:08
 **/
public class Utils {
    public static String getDate() {
        return DateUtil.format(new Date(), "yyyyMMddHHmm");
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

    public static void main(String[] args) {
        String[] value = {null};

        new Thread(() -> {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value[0] = "hhh";

        }).start();

        SyncResult<String> syncResult = new SyncResult<>();
        String s = syncResult.get(new Supplier<String>() {
            @Override
            public String get() {
                return value[0];
            }
        });

        System.out.println(s);


    }


}
