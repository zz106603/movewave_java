package com.movewave.common.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.coyote.ProtocolHandler;

import java.util.concurrent.Executors;

@Configuration
public class TomcatVirtualThreadConfig {

    @Bean
    public TomcatConnectorCustomizer tomcatVirtualThreadExecutorCustomizer() {
        return connector -> {
            ProtocolHandler protocolHandler = connector.getProtocolHandler();
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
