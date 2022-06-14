package com.smart.utils;

import cn.hutool.core.bean.BeanUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @author: xizhonghuai
 * @description: JWTUtils
 * @create: 2022-06-13 17:29
 **/
public class JWTUtils {

    /**
     * 盐值
     */
    private static final String SING = JWTUtils.class.getName();

    /**
     * 生成令牌
     *
     * @param obj payload载荷声明参数
     * @return
     */
    public static String createToken(Object obj) {
        Map<String, Object> map = BeanUtil.beanToMap(obj);
        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //默认7天过期
        calendar.add(Calendar.DATE, 7);
        //新建一个JWT的Builder对象
        JWTCreator.Builder builder = JWT.create();
        //将map集合中的数据设置进payload
        map.forEach((k, v) -> {
            if (v != null) {
                builder.withClaim(k, v.toString());
            }
        });
        //设置过期时间和签名
        return builder
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验签
     *
     * @param token 令牌
     */
    public static void verify(String token) {
        getJWT(token);
    }

    public static DecodedJWT getJWT(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}

