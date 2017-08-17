package com.ziroom.minsu.spider.config.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源配置
 * @author jixd
 * @created 2017年08月16日 11:50:12
 * @param
 * @return
 */
@Configuration
@EnableAutoConfiguration
@MapperScan(basePackages = "com.ziroom.minsu.spider.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DatabaseConfiguration {
	
	@Autowired
	private ApplicationContext appContext;
	
	@Bean(name = "masterDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.master")
	@Primary
	public DataSource masterDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name = "slaveDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.slave")
	public DataSource slaveDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean
	public AbstractRoutingDataSource roundRobinDataSouceProxy(@Qualifier("masterDataSource")DataSource master, @Qualifier("slaveDataSource") DataSource slave) {
		ReadWriteSplitRoutingDataSource proxy = new ReadWriteSplitRoutingDataSource();
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put(DbContextHolder.DbType.MASTER, master);
		targetDataSources.put(DbContextHolder.DbType.SLAVE,  slave);
		proxy.setDefaultTargetDataSource(master);
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource")DataSource master,	@Qualifier("slaveDataSource") DataSource slave) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();		
		sessionFactory.setDataSource((DataSource)appContext.getBean("roundRobinDataSouceProxy"));
		//分页参考使用 https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md

		//分页插件配置
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("pageSizeZero", "true");//分页尺寸为0时查询所有纪录不再执行分页
		properties.setProperty("reasonable", "true");//页码<=0 查询第一页，页码>=总页数查询最后一页
		properties.setProperty("supportMethodsArguments", "true");//支持通过 Mapper 接口参数来传递分页参数 countryMapper.selectByPageNumSize(user, 1, 10);
		pageInterceptor.setProperties(properties);

		sessionFactory.setPlugins(new Interceptor[]{pageInterceptor});
		//设置加载xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
		return sessionFactory.getObject();
	}
	
}
