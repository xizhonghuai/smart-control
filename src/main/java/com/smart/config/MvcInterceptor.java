package com.smart.config;

import com.smart.communication.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @ClassName MvcInterceptor
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/
@Slf4j
public class MvcInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService = new AuthenticationService();

    private static final String[] URL_WHITE_LIST = {"login", "reg", "swagger", "debug"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        log.info("url:" + url);

        if (Arrays.stream(URL_WHITE_LIST).anyMatch(url::contains)) {
            return true;
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }


        String token = request.getHeader("token");
//        log.info("token:" + token);
        return authenticationService.authentication(token, response);

//        if ("/".equals(url)) {
//            response.sendRedirect(Initialization.webUrl + "/treaweb/webmis/index.html");
//            return false;
//        }
//        if (url.indexOf("api/account/reg") > 0) {
//            return true;
//        }
//       response.sendRedirect(Initialization.webUserUrl + "/webuser/index.html?code=1001");
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.get().clear();
    }

}
