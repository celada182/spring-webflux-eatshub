package com.celada.eatshub.catalog.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.celada.eatshub.catalog.repository")
@PropertySource("classpath:mongo-connection.properties")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private int port;

    @Value("${mongodb.username}")
    private String username;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.database}")
    private String database;

    @Value("${mongodb.authenticationDatabase}")
    private String authenticationDatabase;

    @Value("${mongodb.autoIndexCreation}")
    private boolean autoIndexCreation;

    @Value("${mongodb.maxPoolSize}")
    private int maxPoolSize;

    @Value("${mongodb.minPoolSize}")
    private int minPoolSize;

    @Value("${mongodb.maxConnectionLifeTime}")
    private long maxConnectionLifeTime;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        final ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%d/%s", host, port, database));

        final MongoCredential credential = MongoCredential.createCredential(username, authenticationDatabase, password.toCharArray());

        final MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .credential(credential)
                .applyToConnectionPoolSettings(builder -> builder
                        .maxSize(maxPoolSize)
                        .minSize(minPoolSize)
                        .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}
