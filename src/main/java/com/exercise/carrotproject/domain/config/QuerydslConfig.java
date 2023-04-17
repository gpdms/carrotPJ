package com.exercise.carrotproject.domain.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.*;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Supplier;

@Configuration
@EnableTransactionManagement
public class QuerydslConfig {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        SQLTemplates templates = H2Templates.builder().build();
        return new SQLQueryFactory(templates, new SpringConnectionProvider(d));
    }

/*
    @Bean
    @Qualifier("transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
*/

/*    @Bean
    public com.querydsl.sql.Configuration configuration() {
        SQLTemplates templates = H2Templates.builder().build();
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }*/
}
