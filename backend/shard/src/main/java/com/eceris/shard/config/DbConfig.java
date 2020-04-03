package com.eceris.shard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@PropertySources(
        @PropertySource("classpath:db/db.properties")
)
public class DbConfig {
    private final JpaProperties jpaProperties;

    @Bean
    public DataSource routingDataSource(DataSource writeShardDataSource, DataSource readShardDataSource) {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("write", writeShardDataSource);
        dataSourceMap.put("read", readShardDataSource);

        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.setDefaultTargetDataSource(writeShardDataSource);
        return replicationRoutingDataSource;
    }

    @Bean
    public DataSource dataSource(DataSource writeShardDataSource, DataSource readShardDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource(writeShardDataSource, readShardDataSource));
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.eceris.shard");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties.getProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }


}
