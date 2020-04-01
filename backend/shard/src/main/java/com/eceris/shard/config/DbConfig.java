package com.eceris.shard.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DbConfig {

//    @Value("${spring.datasource.hikari.jdbc-url}")
//    private final String jdbcUrl;
    private final String jdbcUrl = "jdbc:h2:file:~/h2database";

//    @Value("${spring.datasource.hikari.driver-class-name}")
//    private final String driverName;
    private final String driverName="org.h2.Driver";

//    @Value("${spring.datasource.hikari.username}")
//    private final String username;
    private final String username="test";

//    @Value("${spring.datasource.hikari.password}")
//    private final String password;
    private final String password="1234";

//    @Value("{$spring.datasource.hikari.jdbc-url.slaves}")
//    private final String[] slaves;
    private final String[] slaves = new String[]{"jdbc:h2:file:~/slave1","jdbc:h2:file:~/slave2","jdbc:h2:file:~/slave3"};

    public DataSource createDataSource(String url) {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setDriverClassName(driverName);
        ds.setUsername(username);
        ds.setPassword(password);
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setUrl(url);
//        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
//        dataSource.setUsername(databaseProperty.getUsername());
//        dataSource.setPassword(databaseProperty.getPassword());
        return ds;
    }

    @Bean
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        DataSource master = createDataSource(jdbcUrl);
        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("master", master);
        dataSourceMap.put("slave1", createDataSource(slaves[0]));
        dataSourceMap.put("slave2", createDataSource(slaves[1]));
        dataSourceMap.put("slave3", createDataSource(slaves[2]));
        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.setDefaultTargetDataSource(master);
        return replicationRoutingDataSource;
    }

    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.eceris.shard");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
