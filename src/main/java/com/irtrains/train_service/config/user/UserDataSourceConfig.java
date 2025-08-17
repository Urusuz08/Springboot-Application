package com.irtrains.train_service.config.user;


import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableJpaRepositories(
    basePackages = "com.irtrains.train_service.repository.user",
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager"
)
public class UserDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource userDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("userDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.irtrains.train_service.model.user") // Adjust package as needed
            .persistenceUnit("user")
            .build();
    }

    @Bean
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("userEntityManagerFactory") EntityManagerFactory userEntityManagerFactory) {
            return new JpaTransactionManager(userEntityManagerFactory);
    }

    @Bean
    public SpringLiquibase userLiquibase(@Qualifier("userDataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/user_changelog.xml");
        return liquibase;
    }
}