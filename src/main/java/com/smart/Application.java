package com.smart;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @ClassName com.smart.Application
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/1/20
 * @Version V1.0
 **/
@SpringBootApplication
//@MapperScan("com.smart.dao")
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
    }

}
