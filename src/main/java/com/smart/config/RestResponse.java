package com.smart.config;

import lombok.Data;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Data
public class RestResponse<T> {
    private T data;
    private String message;
    private Boolean status;

    public RestResponse() {
    }

    public RestResponse(T data, String message, Boolean status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public static <T> RestResponse<T> success(T t) {
        return new RestResponse<>(t, null, true);
    }

    public static RestResponse<Object> success() {
        return success(null);
    }

    public static RestResponse<Object> fail(String err) {
        return new RestResponse<>(null, err, false);
    }

}
