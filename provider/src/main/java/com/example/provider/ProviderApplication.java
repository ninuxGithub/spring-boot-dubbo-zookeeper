package com.example.provider;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;

@SpringBootApplication
public class ProviderApplication {


    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.setProperty("java.security.auth.login.config",
                loader.getResource("").getPath()+ File.separator+"kafka_client_jaas.conf");
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProviderApplication.class).web(WebApplicationType.SERVLET).run(args);

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER,true);
    }

}

