package com.hackersanon.banqi.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = { @ComponentScan("com.hackersanon.banqi")})
public class HibernateConfig {
    private final static String DATABASE_URL = "jdbc:mysql://cs414-f19-hackersanon.mysql.database.azure.com:3306";
    private final static String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DATABASE_USERNAME = "hackersanon@cs414-f19-hackersanon";
    private final static String DATABASE_PASSWORD = "CS414BANQI";


    @Bean
    public LocalSessionFactoryBean getSessionFactory(){
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setHibernateProperties(getHibernateProperties());
        factoryBean.setDataSource(getDataSource());
        factoryBean.setPackagesToScan("com.hackersanon.banqi");
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager(){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public static DataSource getDataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://cs414-f19-hackersanon.mysql.database.azure.com:3306/banqi?useSSL=true&requireSSL=false");
        dataSource.setUser("hackersanon@cs414-f19-hackersanon");
        dataSource.setPassword("cs414BANQI");
        dataSource.setDatabaseName("banqi");

        try {
            dataSource.setServerTimezone("US/Central");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    private static Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.c3p0.min_size","5");
        properties.put("hibernate.c3p0.max_size","20");
        properties.put("hibernate.c3p0.acquire_increment","1");
        properties.put("hibernate.c3p0.timeout","1000");
        properties.put("hibernate.c3p0.max_statements","150");
        properties.put("hibernate.archive.autodetection","class,hbm");
        return properties;
    }

//    @Bean
//    @Qualifier("jpaVendorAdaptor")
//    public JpaVendorAdapter getJpaVendorAdaptor(){
//        JpaVendorAdapter jpaVendorAdapter;
//        jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        return jpaVendorAdapter;
//    }

}


