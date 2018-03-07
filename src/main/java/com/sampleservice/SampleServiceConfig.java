package com.sampleservice;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class SampleServiceConfig {

    @Bean(name = "TeeFilter")
    public Filter teeFilter() {
        return new ch.qos.logback.access.servlet.TeeFilter();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        LogbackValve logbackValve = new LogbackValve();
        logbackValve.setFilename("logback-access.xml");
        tomcat.addContextValves(logbackValve);
        return tomcat;
    }
}
