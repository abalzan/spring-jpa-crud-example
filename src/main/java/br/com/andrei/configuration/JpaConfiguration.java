package br.com.andrei.configuration;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.andrei.repositories", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class JpaConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.jpa")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	/*
	 * Configure MY SQL pooled DataSource.
	 */
//	@Bean
//	public DataSource dataSource() {
//		DataSourceProperties dataSourceProperties = dataSourceProperties();
//		MysqlDataSource dataSource = (MysqlDataSource) DataSourceBuilder.create(dataSourceProperties.getClassLoader())
//				.driverClassName(dataSourceProperties.getDriverClassName()).url(dataSourceProperties.getUrl())
//				.username(dataSourceProperties.getUsername()).password(dataSourceProperties.getPassword())
//				.type(MysqlDataSource.class).build();
//		return dataSource;
//	}
	 @Bean
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
	        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
	        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
	        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
	        return dataSource;
	    }

	/*
	 * Entity Manager Factory setup.
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan(new String[] {"br.com.andrei.entity" });
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setJpaProperties(jpaProperties());
		return factoryBean;
	}

	/*
	 * Provider specific adapter.
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}

	/*
	 * Here you can specify any provider specific properties.
	 */
	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.ddl-auto", environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("spring.jpa.show-sql"));
		properties.put("hibernate.", environment.getRequiredProperty("spring.jpa.show-sql"));
		//properties.put("hibernate.format_sql", environment.getRequiredProperty("spring.jpa.hibernate.format-sql"));
//		if (!StringUtils.isEmpty(environment.getRequiredProperty("datasource.sample.defaultSchema"))) {
//			properties.put("hibernate.default_schema",
//					environment.getRequiredProperty("datasource.sample.defaultSchema"));
//		}
		return properties;
	}

	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

}
