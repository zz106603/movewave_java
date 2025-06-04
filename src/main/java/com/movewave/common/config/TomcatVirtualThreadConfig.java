package com.movewave.common.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.coyote.ProtocolHandler;

import java.util.concurrent.Executors;

@Configuration
public class TomcatVirtualThreadConfig {

    /**
     * Tomcat의 Connector에 Virtual Thread를 사용하는 Executor를 설정하는 메서드입니다.
     * 
     * FIXME: 현재 Virtual Threads가 프로젝트에 유용하게 작동하는지 검토 필요
     *
     * @return Virtual Thread를 사용하는 Executor를 설정하는 TomcatConnectorCustomizer
     */
    @Bean
    public TomcatConnectorCustomizer tomcatVirtualThreadExecutorCustomizer() {
        return connector -> {
            ProtocolHandler protocolHandler = connector.getProtocolHandler();
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}