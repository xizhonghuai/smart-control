package com.smart.config;

import lombok.Data;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Data
public class RestResponse<T> {
    private T data;
    private Integer code;
    private String message;

    public RestResponse() {
    }

    public RestResponse(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> RestResponse<T> success(T t) {
        return new RestResponse<>(t, 20000, null);
    }

    public static RestResponse<Object> success() {
        return success(null);
    }

    public static RestResponse<?> fail(String err) {
        return new RestResponse<>(null, 40000, err);
    }

    public static RestResponse<?> fail(String err, Integer code) {
        return new RestResponse<>(null, code, err);
    }

}
