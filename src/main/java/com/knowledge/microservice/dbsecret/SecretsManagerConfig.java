
package com.knowledge.microservice.dbsecret;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;


@Configuration
public class SecretsManagerConfig {

    private final Logger logger = LogManager.getLogger(SecretsManagerConfig.class);

    private final Environment environment;

    @Autowired
    public SecretsManagerConfig(Environment environment) {
        super();
        this.environment = environment;
    }

    @Bean
    @ConditionalOnProperty(name = "aws.data-source.flag", havingValue = "true")
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.of("ap-south-1"))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "aws.data-source.flag", havingValue = "true")
    public Map<String, String> databaseConfig(SecretsManagerClient client) {
        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(environment.getProperty("aws.secretsmanager.secret-name"))
                    .build();
            GetSecretValueResponse response = client.getSecretValue(request);
            String secret = response.secretString();
            if (secret == null || secret.trim().isEmpty()) {
                throw new IllegalStateException("Secret string is null or empty");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> secretMap;
            try {
                secretMap = mapper.readValue(secret, new TypeReference<>() {
                });
                logger.info(":::DB SECRET::" + secretMap);
            } catch (JsonProcessingException e) {
                logger.error("Failed to parse secret JSON", e);
                throw new RuntimeException("Failed to parse secret", e);
            }
            // Validate required keys directly in secretMap
            if (!secretMap.containsKey("SPRING_DATASOURCE_URL") || secretMap.get("SPRING_DATASOURCE_URL").isEmpty()) {
                logger.error("Missing or empty key: SPRING_DATASOURCE_URL");
                throw new IllegalStateException("Missing or empty key: SPRING_DATASOURCE_URL");
            }
            if (!secretMap.containsKey("SPRING_DATASOURCE_USERNAME") || secretMap.get("SPRING_DATASOURCE_USERNAME").isEmpty()) {
                logger.error("Missing or empty key: SPRING_DATASOURCE_USERNAME");
                throw new IllegalStateException("Missing or empty key: SPRING_DATASOURCE_USERNAME");
            }
            if (!secretMap.containsKey("SPRING_DATASOURCE_PASSWORD") || secretMap.get("SPRING_DATASOURCE_PASSWORD").isEmpty()) {
                logger.error("Missing or empty key: SPRING_DATASOURCE_PASSWORD");
                throw new IllegalStateException("Missing or empty key: SPRING_DATASOURCE_PASSWORD");
            }
            if (!secretMap.containsKey("SPRING_DATASOURCE_DRIVER_CLASS_NAME") || secretMap.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME").isEmpty()) {
                logger.error("Missing or empty key: SPRING_DATASOURCE_DRIVER_CLASS_NAME");
                throw new IllegalStateException("Missing or empty key: SPRING_DATASOURCE_DRIVER_CLASS_NAME");
            }
            return secretMap;
        } catch (Exception e) {
            logger.error("Failed to fetch or parse secret from AWS Secrets Manager", e);
            throw new RuntimeException("Failed to fetch or parse secret", e);
        }
    }
}

