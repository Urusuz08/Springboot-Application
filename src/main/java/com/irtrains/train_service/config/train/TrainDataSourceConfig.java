package com.irtrains.train_service.config.train;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.irtrains.train_service.repository.train",
        entityManagerFactoryRef = "trainEntityManagerFactory",
        transactionManagerRef = "trainTransactionManager"
)
public class TrainDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.train")
    public DataSourceProperties trainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource trainDataSource(@Qualifier("trainDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean trainEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("trainDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.irtrains.train_service.model.train") // Adjust package as needed
                .persistenceUnit("train")
                .build();
    }

    @Bean
    public PlatformTransactionManager trainTransactionManager(
            @Qualifier("trainEntityManagerFactory") EntityManagerFactory trainEntityManagerFactory) {
        return new JpaTransactionManager(trainEntityManagerFactory);
    }

    @Bean
    public SpringLiquibase trainLiquibase(@Qualifier("trainDataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/traindb_changelog.xml");
        return liquibase;
    }
}