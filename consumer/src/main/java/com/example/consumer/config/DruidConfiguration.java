package com.example.consumer.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 配置druid数据源
 * http://localhost:8888/druid/index.html
 */
@Configuration
public class DruidConfiguration {
	
	private Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

	@Bean
	public ServletRegistrationBean druidStatViewServlet() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		registrationBean.addInitParameter("allow", "127.0.0.1");
		registrationBean.addInitParameter("deny", "192.168.31.234");
		registrationBean.addInitParameter("loginUsername", "admin");
		registrationBean.addInitParameter("loginPassword", "admin");
		registrationBean.addInitParameter("resetEnable", "false");
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean druidWebStatViewFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new WebStatFilter());
		registrationBean.addInitParameter("urlPatterns", "/*");
		registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		return registrationBean;

	}

	@Bean
	public DataSource druidDataSource(@Value("${spring.datasource.mysql.url}") String url,
			@Value("${spring.datasource.mysql.driver-class-name}") String driver,
			@Value("${spring.datasource.mysql.username}") String userName,
			@Value("${spring.datasource.mysql.password}") String password,
			@Value("${spring.datasource.mysql.maxActive}") int maxActive,
			@Value("${spring.datasource.mysql.filters}") String filters,
			@Value("${spring.datasource.mysql.initialSize}") int initialSize,
			@Value("${spring.datasource.mysql.minIdle}") int minIdle,
			@Value("${spring.datasource.mysql.maxWait}") int maxWait,
			@Value("${spring.datasource.mysql.timeBetweenEvictionRunsMillis}") int timeBetweenEvictionRunsMillis,
			@Value("${spring.datasource.mysql.minEvictableIdleTimeMillis}") int minEvictableIdleTimeMillis,
			@Value("${spring.datasource.mysql.validationQuery}") String validationQuery,
			@Value("${spring.datasource.mysql.testWhileIdle}") boolean testWhileIdle,
			@Value("${spring.datasource.mysql.testOnBorrow}") boolean testOnBorrow,
			@Value("${spring.datasource.mysql.testOnReturn}") boolean testOnReturn,
			@Value("${spring.datasource.mysql.poolPreparedStatements}") boolean poolPreparedStatements,
			@Value("${spring.datasource.mysql.maxPoolPreparedStatementPerConnectionSize}") int maxPoolPreparedStatementPerConnectionSize,
			@Value("${spring.datasource.mysql.connectionProperties}") String connectionProperties,
			@Value("${spring.datasource.mysql.useGlobalDataSourceStat}") boolean useGlobalDataSourceStat

	) {
		DruidDataSource dataSource = new DruidDataSource();
		/*数据源主要配置*/
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		
		/*数据源补充配置*/
		dataSource.setMaxActive(maxActive);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		dataSource.setConnectionProperties(connectionProperties);
		dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
		
		try {
			dataSource.setFilters(filters);
			logger.info("Druid数据源初始化设置成功......");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Druid数据源filters设置失败......");
		}
		return dataSource;

	}
}

