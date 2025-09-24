package com.irtrains.train_service.config.train;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                // Include BOTH station and train repositories
                "com.irtrains.train_service.repository.station",
                "com.irtrains.train_service.repository.train"
        },
        entityManagerFactoryRef = "trainEntityManagerFactory",
        transactionManagerRef = "trainTransactionManager"
)
public class TrainDataSourceConfig {

    // DataSource properties for the train+station logical datasource
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.train")
    public DataSourceProperties trainDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Primary DataSource bean (mark @Primary if multiple datasources exist)
    @Bean
    @Primary
    public DataSource trainDataSource(@Qualifier("trainDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    // Builder (can remain non-primary)
    @Bean
    public EntityManagerFactoryBuilder trainEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
                new java.util.HashMap<>(), null);
    }

    // EntityManagerFactory scanning both station & train entities (and enums)
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean trainEntityManagerFactory(
            @Qualifier("trainEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("trainDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(
                        "com.irtrains.train_service.model.station",
                        "com.irtrains.train_service.model.train",
                        "com.irtrains.train_service.model.enums"
                )
                .persistenceUnit("train")
                .build();
    }

    // Transaction manager for this unit
    @Bean
    @Primary
    public PlatformTransactionManager trainTransactionManager(
            @Qualifier("trainEntityManagerFactory") EntityManagerFactory trainEntityManagerFactory) {
        return new JpaTransactionManager(trainEntityManagerFactory);
    }

    // Liquibase tied to this datasource (changeLog may include both station & train changes)
    @Bean
    public SpringLiquibase trainLiquibase(@Qualifier("trainDataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/traindb_changelog.xml");
        return liquibase;
    }
}