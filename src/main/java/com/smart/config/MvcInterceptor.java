package com.smart.config;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.smart.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MvcInterceptor
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/

public class MvcInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        String token = request.getHeader("token");
        //todo
        if (token == null) {
            return true;
        }
        String failMessage;
        try {
            JWTUtils.verify(token);
            AuthContext.get().setObj(JWTUtils.getJWT(token).getClaims());
            return true;
        } catch (TokenExpiredException e) {
            failMessage = "token已过期";
        } catch (SignatureVerificationException e) {
            failMessage = "签名错误!";
        } catch (AlgorithmMismatchException e) {
            failMessage = "加密算法不匹配!";
        } catch (Exception e) {
            e.printStackTrace();
            failMessage = "无效token";
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(RestResponse.fail(failMessage)));

        return false;


//        if ("/".equals(url)) {
//            response.sendRedirect(Initialization.webUrl + "/treaweb/webmis/login.html");
//            return false;
//        }
//        if (url.indexOf("api/account/reg") > 0) {
//            return true;
//        }
//       response.sendRedirect(Initialization.webUserUrl + "/webuser/login.html?code=1001");
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.get().clear();
    }

}
