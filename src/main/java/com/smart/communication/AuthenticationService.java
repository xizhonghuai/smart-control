package com.smart.communication;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.smart.config.AuthContext;
import com.smart.config.RestResponse;
import com.smart.mvc.entity.Account;
import com.smart.utils.JWTUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author: xizhonghuai
 * @description: AuthenticationService
 * @create: 2022-06-17 10:01
 **/
public class AuthenticationService {
    public boolean authentication(String token, HttpServletResponse response) throws IOException {
     /*   if (token == null) {
            return true;
        }*/
        String failMessage;
        try {
            JWTUtils.verify(token);
            AuthContext.get().setLoginUser(deCodeToken(token));
            return true;
        } catch (TokenExpiredException e) {
            failMessage = "token已过期";
        } catch (SignatureVerificationException e) {
            failMessage = "签名错误";
        } catch (AlgorithmMismatchException e) {
            failMessage = "加密算法不匹配";
        } catch (Exception e) {
            e.printStackTrace();
            failMessage = "无效token";
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(RestResponse.fail(failMessage)));
        return false;
    }

    public String createToken(Account account) {
        return JWTUtils.createToken(account);
    }
    public Account deCodeToken(String token) {
        Map<String, Claim> claims = JWTUtils.getJWT(token).getClaims();
        Claim claim = claims.get("account");
        if (claim != null) {
            return JSON.parseObject(claim.asString(), Account.class);
        }
        throw new RuntimeException("Failed to resolve the load in token. Procedure");
    }

}
