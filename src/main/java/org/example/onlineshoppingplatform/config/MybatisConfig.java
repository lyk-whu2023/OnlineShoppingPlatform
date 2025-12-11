package org.example.onlineshoppingplatform.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan("org.example.onlineshoppingplatform.mapper")
public class MybatisConfig {
    @Bean
    public org.apache.ibatis.session.SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration ibatisConf = new org.apache.ibatis.session.Configuration();
        ibatisConf.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(ibatisConf);
        return factory.getObject();
    }
}
