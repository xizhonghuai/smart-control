package com.init;

import com.commonmanger.ServerManage;
import com.toolutils.ConstantUtils;
import com.transmission.server.core.BootServerParameter;
import com.transmission.server.debug.DebugService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @ClassName Initialization
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "smart-control")
public class Initialization {

    private String webHost;
    private String webUserHost;
    private Integer defaultServerPort;
    private Integer debugPort;

    public static String webUrl;
    public static String webUserUrl;

    @Autowired
    private ServerManage serverManage;


    @PostConstruct
    public void init() {
        // todo 加载数据

        try {
            //启动调试服务
            new DebugService(debugPort).start();

            //todo 启动服务
            BootServerParameter bootServerParameter = new BootServerParameter();
            bootServerParameter.setServerName("YX-Cloud-Iot");
            bootServerParameter.setIdle(300);
            bootServerParameter.setPort(Collections.singletonList(defaultServerPort));
            bootServerParameter.setDebug(true);
            bootServerParameter.setServiceId("treatment");
            bootServerParameter.setServerType(ConstantUtils.TCP);
            try {
                serverManage.createService(bootServerParameter);
                serverManage.startServer("treatment");
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.toString());
            }

            webUrl = webHost;
            webUserUrl = webUserHost;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }


}
