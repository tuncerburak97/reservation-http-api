package com.reztech.reservation_http_api.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

/**
 * MongoDB configuration class with auto-indexing and validation
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.reztech.reservation_http_api.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private int port;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return "reservation_db";
    }
    
    /**
     * Enable validation before saving to MongoDB
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    /**
     * Validator bean for MongoDB validation
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
    
    /**
     * Enable auto-index creation in development
     */
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }


    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder
                .credential(MongoCredential.createCredential(username, getDatabaseName(), password.toCharArray()))
                .applyToClusterSettings(settings  -> {
                    settings.hosts(singletonList(new ServerAddress(host, port)));
                });

        builder.applyToConnectionPoolSettings(settings -> {

            settings.maxConnectionLifeTime(2000, TimeUnit.MILLISECONDS)
                    .minSize(10)//pool
                    .maxSize(100)//pool
                    .maintenanceFrequency(10, TimeUnit.MILLISECONDS)
                    .maintenanceInitialDelay(11, TimeUnit.MILLISECONDS)
                    .maxConnectionIdleTime(30, TimeUnit.SECONDS)
                    .maxWaitTime(15, TimeUnit.MILLISECONDS);
        });
    }
} 