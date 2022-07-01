package com.smart.communication.hander.businesshandler;

import cn.hutool.core.util.StrUtil;
import com.transmission.server.core.IotSession;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author: xizhonghuai
 * @description: PackProcess
 * @create: 2022-07-01 10:34
 **/
@Slf4j
public class PackProcess {
    private final Function<Object, Boolean> isBeginMessageFunction;
    private final Function<Object, Boolean> isReceiveCompleteFunction;
    private String pack = "";

    private PackProcess(Function<Object, Boolean> isBeginMessageFunction, Function<Object, Boolean> isReceiveCompleteFunction) {
        this.isBeginMessageFunction = isBeginMessageFunction;
        this.isReceiveCompleteFunction = isReceiveCompleteFunction;
    }

    public static PackProcess getPackProcess(IotSession session
            , Function<Object, Boolean> isBeginMessageFunction
            , Function<Object, Boolean> isReceiveCompleteFunction) {
        return (PackProcess) session.getAttribute(packProcessAttributeName, new PackProcess(isBeginMessageFunction, isReceiveCompleteFunction));
    }

    private static final String packProcessAttributeName = "PackProcess";

    public Object getPack() {
        String temp = pack;
        pack = "";
        return temp;
    }

    public Boolean buildPackage(Object message) {
        log.info("pack:"+pack);
        if (isReceiveCompleteFunction.apply(message)) {
            pack = message.toString();
            return true;
        }
        if (isReceiveCompleteFunction.apply(pack + message)) {
            pack = pack + message;
            return true;
        }
        if (isBeginMessageFunction.apply(message)) {
            pack = message.toString();
            return false;
        }
        if (StrUtil.isNotBlank(pack)) {
            pack = pack + message;
        }
        return false;
    }



}
