package pl.madamusinski.dbsync.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.madamusinski.dbsync.repository.syncTwo",
        entityManagerFactoryRef = "syncTwoEntityManager",
        transactionManagerRef = "syncTwoTransactionManager")
public class SyncTwoDbConfig {

    @Autowired
    Environment env;

    @Bean
    public DataSource syncTwoDataSource(){
        DriverManagerDataSource ds
                = new DriverManagerDataSource();
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        ds.setUrl(env.getProperty("spring.datasource2.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean syncTwoEntityManager(){
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(syncTwoDataSource());
        em.setPackagesToScan(
                new String[] {"pl.madamusinski.dbsync.domain"}
        );
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.jpa.properties.hibernate.dialect"));

        return em;
    }

    @Bean
    public PlatformTransactionManager syncTwoTransactionManager(){
        JpaTransactionManager tx
                = new JpaTransactionManager();
        tx.setEntityManagerFactory(
                syncTwoEntityManager().getObject()
        );
        return tx;
    }
}
