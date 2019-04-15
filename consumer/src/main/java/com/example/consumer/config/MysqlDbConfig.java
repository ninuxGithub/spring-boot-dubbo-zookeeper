package com.example.consumer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;


/**
 * basePackages: 扫描repository 所在的包
 * entiryMangerFactory package : 实例bean的包
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "dataSourceTransactionManager",
        basePackages = {"com.example.consumer.repository"})
public class MysqlDbConfig {

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource mysqlDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "mysqlEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return mysqlEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mysqlDataSource)
                .properties(getVendorProperties(mysqlDataSource))
                .packages("com.example.consumer.bean")
                .persistenceUnit("mysqlPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

//    @Primary
//    @Bean(name = "mysqlTransactionManager")
//    PlatformTransactionManager mysqlTransactionManager(EntityManagerFactoryBuilder builder) {
//        JpaTransactionManager pm = new JpaTransactionManager(mysqlEntityManagerFactory(builder).getObject());
//        pm.setGlobalRollbackOnParticipationFailure(false);
//        return pm;
//    }

    @Primary
    @Bean(name = "dataSourceTransactionManager")
    DataSourceTransactionManager dataSourceTransactionManager(EntityManagerFactoryBuilder builder) {
        DataSourceTransactionManager pm = new DataSourceTransactionManager(mysqlDataSource);
        return pm;
    }

}