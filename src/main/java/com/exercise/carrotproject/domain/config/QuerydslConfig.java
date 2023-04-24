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
public class QuerydslConfig {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SQLTemplates mysqlTemplates() {
        return H2Templates.builder().build();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        return new SQLQueryFactory(configuration(), new SpringConnectionProvider(dataSource));
    }


    @Bean
    public com.querydsl.sql.Configuration configuration() {
        SQLTemplates templates = H2Templates.builder().build();
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }
}
