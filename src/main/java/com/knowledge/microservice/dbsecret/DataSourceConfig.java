
package com.knowledge.microservice.dbsecret;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConditionalOnProperty(name = "aws.data-source.flag", havingValue = "true")
    public DataSource dataSource(Map<String, String> databaseConfig) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databaseConfig.get("SPRING_DATASOURCE_URL"));
        dataSource.setUsername(databaseConfig.get("SPRING_DATASOURCE_USERNAME"));
        dataSource.setPassword(databaseConfig.get("SPRING_DATASOURCE_PASSWORD"));
        dataSource.setDriverClassName(databaseConfig.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));
        return dataSource;
    }
}

