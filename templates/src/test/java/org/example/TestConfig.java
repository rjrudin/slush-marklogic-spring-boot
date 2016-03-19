package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.marklogic.junit.spring.BasicTestConfig;

/**
 * Extends BasicTestConfig from ml-junit, which reads in properties from gradle.properties. This also reads in the host
 * property from application.properties, which is not stored in gradle.properties.
 */
@Configuration
@PropertySource({ "file:gradle.properties", "file:src/main/resources/application.properties" })
public class TestConfig extends BasicTestConfig {

    @Value("${marklogic.mlHost:localhost}")
    private String mlHost;

    @Override
    public String getMlHost() {
        return mlHost;
    }

}
