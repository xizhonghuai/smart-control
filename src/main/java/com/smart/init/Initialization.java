package com.smart.init;

import com.smart.communication.ServerAPI;
import com.smart.config.SpringUtil;
import com.smart.domain.VirtualDevice;
import com.toolutils.ConstantUtils;
import com.transmission.server.core.BootServerParameter;
import com.transmission.server.debug.DebugService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    private ServerAPI serverAPI = new ServerAPI();

    @PostConstruct
    public void init() {
        // todo 加载数据
        try {
            //启动调试服务
            new DebugService(debugPort).start();
            //todo 启动服务
            BootServerParameter bootServerParameter = new BootServerParameter();
            bootServerParameter.setServerName("YX-Cloud-Iot");
            bootServerParameter.setIdle(3000);
            bootServerParameter.setPort(Collections.singletonList(defaultServerPort));
            bootServerParameter.setDebug(true);
            bootServerParameter.setServiceId("smart-control");
            bootServerParameter.setServerType(ConstantUtils.TCP);
            try {
                serverAPI.createService(bootServerParameter);
                serverAPI.startServer("smart-control");

                VirtualDevice virtualDevice = SpringUtil.getObject(VirtualDevice.class);
                virtualDevice.connect();
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
