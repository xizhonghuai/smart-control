package com.smart.domain.message.s2c;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author: xizhonghuai
 * @description: S2cMessage
 * @create: 2022-06-10 14:07
 **/
public interface S2cMessage {
    byte[] s1 = {0x02};
    byte[] s2 = {0x03};

    default String pack() {
//        Map<String, Object> map = BeanUtil.beanToMap(this, true, false);
        return new String(s1) + JSON.toJSONString(this) + new String(s2);
    }
}
