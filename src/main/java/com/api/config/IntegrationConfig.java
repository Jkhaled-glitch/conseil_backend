package com.api.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.api.dao.integration",
    entityManagerFactoryRef = "integration_EntityManagerFactory",
    transactionManagerRef = "integration_TransactionManager"
)

@ConditionalOnProperty(name = "spring.integration.datasource.jdbcUrl")
public class IntegrationConfig {
    

   
    @Bean(name = "integration_DataSource")
    @ConfigurationProperties(prefix = "spring.integration.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "integration_EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean IntegrationEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(dataSource())
                .packages("com.api.entities.integration")
                .persistenceUnit("Integration_db")
           
                .build();
    }

  
    @Bean(name = "integration_TransactionManager")
    public PlatformTransactionManager IntegrationTransactionManager(
            @Qualifier("integration_EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    
}