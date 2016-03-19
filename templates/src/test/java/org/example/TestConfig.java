package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.marklogic.client.helper.DatabaseClientConfig;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.client.spring.SimpleDatabaseClientProvider;

/**
 * Extends BasicTestConfig from ml-junit, which reads in properties from gradle.properties, to also read from
 * application.properties.
 */
@Configuration
@PropertySource({ "file:gradle.properties", "file:src/main/resources/application.properties" })
public class TestConfig extends DevConfig {

    @Value("${mlTestRestPort}")
    private Integer mlTestRestPort;

    /**
     * Must be static so that it's loaded first and then used to parse the value annotations above.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * ml-junit depends on an instance of DatabaseClientProvider in order to connect to MarkLogic.
     */
    @Bean
    public DatabaseClientProvider databaseClientProvider() {
        DatabaseClientConfig config = new DatabaseClientConfig(mlHost, mlTestRestPort, mlRestAdminUsername,
                mlRestAdminPassword);
        config.setDatabase(mlAppName + "-test-content");
        return new SimpleDatabaseClientProvider(config);
    }
}