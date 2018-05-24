package cn.sirenia.mybatis.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class MySqlSessionFactoryBean
		implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent>{
	private static final Log LOGGER = LogFactory.getLog(SqlSessionFactoryBean.class);
	private Resource configLocation;
	private Configuration configuration;
	private Resource[] mapperLocations;
	private DataSource dataSource;
	private TransactionFactory transactionFactory;
	private Properties configurationProperties;
	private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	private SqlSessionFactory sqlSessionFactory;
	private String environment = SqlSessionFactoryBean.class.getSimpleName();
	private boolean failFast;
	private Interceptor[] plugins;
	private TypeHandler<?>[] typeHandlers;
	private String typeHandlersPackage;
	private Class<?>[] typeAliases;
	private String typeAliasesPackage;
	private Class<?> typeAliasesSuperType;
	private DatabaseIdProvider databaseIdProvider;
	private Class<? extends VFS> vfs;
	private Cache cache;
	private ObjectFactory objectFactory;
	private ObjectWrapperFactory objectWrapperFactory;

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
		this.objectWrapperFactory = objectWrapperFactory;
	}

	public DatabaseIdProvider getDatabaseIdProvider() {
		return this.databaseIdProvider;
	}

	public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
		this.databaseIdProvider = databaseIdProvider;
	}

	public Class<? extends VFS> getVfs() {
		return this.vfs;
	}

	public void setVfs(Class<? extends VFS> vfs) {
		this.vfs = vfs;
	}

	public Cache getCache() {
		return this.cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public void setPlugins(Interceptor[] plugins) {
		this.plugins = plugins;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
		this.typeAliasesSuperType = typeAliasesSuperType;
	}

	public void setTypeHandlersPackage(String typeHandlersPackage) {
		this.typeHandlersPackage = typeHandlersPackage;
	}

	public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
		this.typeHandlers = typeHandlers;
	}

	public void setTypeAliases(Class<?>[] typeAliases) {
		this.typeAliases = typeAliases;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setMapperLocations(Resource[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
		this.configurationProperties = sqlSessionFactoryProperties;
	}

	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy) {
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
		} else {
			this.dataSource = dataSource;
		}

	}

	public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
		this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.dataSource, "Property \'dataSource\' is required");
		Assert.notNull(this.sqlSessionFactoryBuilder, "Property \'sqlSessionFactoryBuilder\' is required");
		Assert.state(
				this.configuration == null && this.configLocation == null || this.configuration == null
						|| this.configLocation == null,
				"Property \'configuration\' and \'configLocation\' can not specified with together");
		this.sqlSessionFactory = this.buildSqlSessionFactory();
	}

	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		MyXMLConfigBuilder xmlConfigBuilder = null;
		Configuration configuration;
		if (this.configuration != null) {
			configuration = this.configuration;
			if (configuration.getVariables() == null) {
				configuration.setVariables(this.configurationProperties);
			} else if (this.configurationProperties != null) {
				configuration.getVariables().putAll(this.configurationProperties);
			}
		} else if (this.configLocation != null) {
			xmlConfigBuilder = new MyXMLConfigBuilder(this.configLocation.getInputStream(), (String) null,
					this.configurationProperties);
			configuration = xmlConfigBuilder.getConfiguration();
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"Property `configuration` or \'configLocation\' not specified, using default MyBatis Configuration");
			}

			configuration = new Configuration();
			configuration.setVariables(this.configurationProperties);
		}

		if (this.objectFactory != null) {
			configuration.setObjectFactory(this.objectFactory);
		}

		if (this.objectWrapperFactory != null) {
			configuration.setObjectWrapperFactory(this.objectWrapperFactory);
		}

		if (this.vfs != null) {
			configuration.setVfsImpl(this.vfs);
		}

		String[] ex;
		String[] arg3;
		int arg4;
		int mapperLocation;
		String e;
		if (StringUtils.hasLength(this.typeAliasesPackage)) {
			ex = StringUtils.tokenizeToStringArray(this.typeAliasesPackage, ",; \t\n");
			arg3 = ex;
			arg4 = ex.length;

			for (mapperLocation = 0; mapperLocation < arg4; ++mapperLocation) {
				e = arg3[mapperLocation];
				configuration.getTypeAliasRegistry().registerAliases(e,
						this.typeAliasesSuperType == null ? Object.class : this.typeAliasesSuperType);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Scanned package: \'" + e + "\' for aliases");
				}
			}
		}

		int resourcesLength;
		if (!ObjectUtils.isEmpty(this.typeAliases)) {
			Class[] arg24 = this.typeAliases;
			resourcesLength = arg24.length;

			for (arg4 = 0; arg4 < resourcesLength; ++arg4) {
				Class arg29 = arg24[arg4];
				configuration.getTypeAliasRegistry().registerAlias(arg29);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Registered type alias: \'" + arg29 + "\'");
				}
			}
		}

		if (!ObjectUtils.isEmpty(this.plugins)) {
			Interceptor[] arg25 = this.plugins;
			resourcesLength = arg25.length;

			for (arg4 = 0; arg4 < resourcesLength; ++arg4) {
				Interceptor arg30 = arg25[arg4];
				configuration.addInterceptor(arg30);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Registered plugin: \'" + arg30 + "\'");
				}
			}
		}

		if (StringUtils.hasLength(this.typeHandlersPackage)) {
			ex = StringUtils.tokenizeToStringArray(this.typeHandlersPackage, ",; \t\n");
			arg3 = ex;
			arg4 = ex.length;

			for (mapperLocation = 0; mapperLocation < arg4; ++mapperLocation) {
				e = arg3[mapperLocation];
				configuration.getTypeHandlerRegistry().register(e);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Scanned package: \'" + e + "\' for type handlers");
				}
			}
		}

		if (!ObjectUtils.isEmpty(this.typeHandlers)) {
			TypeHandler[] arg27 = this.typeHandlers;
			resourcesLength = arg27.length;

			for (arg4 = 0; arg4 < resourcesLength; ++arg4) {
				TypeHandler arg32 = arg27[arg4];
				configuration.getTypeHandlerRegistry().register(arg32);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Registered type handler: \'" + arg32 + "\'");
				}
			}
		}

		if (this.databaseIdProvider != null) {
			try {
				configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
			} catch (SQLException arg23) {
				throw new NestedIOException("Failed getting a databaseId", arg23);
			}
		}

		if (this.cache != null) {
			configuration.addCache(this.cache);
		}

		if (xmlConfigBuilder != null) {
			try {
				xmlConfigBuilder.parse();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Parsed configuration file: \'" + this.configLocation + "\'");
				}
			} catch (Exception arg21) {
				throw new NestedIOException("Failed to parse config resource: " + this.configLocation, arg21);
			} finally {
				ErrorContext.instance().reset();
			}
		}

		if (this.transactionFactory == null) {
			this.transactionFactory = new SpringManagedTransactionFactory();
		}

		configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));
		if (!ObjectUtils.isEmpty(this.mapperLocations)) {
			Resource[] resources = this.mapperLocations;
			resourcesLength = resources.length;

			for (arg4 = 0; arg4 < resourcesLength; ++arg4) {
				Resource arg33 = resources[arg4];
				if (arg33 != null) {
					try {
						MyXMLMapperBuilder arg31 = new MyXMLMapperBuilder(arg33.getInputStream(), configuration,
								arg33.toString(), configuration.getSqlFragments());
						arg31.parse();
					} catch (Exception arg19) {
						throw new NestedIOException("Failed to parse mapping resource: \'" + arg33 + "\'", arg19);
					} finally {
						ErrorContext.instance().reset();
					}

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Parsed mapper file: \'" + arg33 + "\'");
					}
				}
			}
			//解析完扫描到的mapper.xml之后，自动添加没有的mapper.xml
			Collection<Class<?>> parsedMappers = xmlConfigBuilder.getConfiguration().getMapperRegistry().getMappers();
			MapperHolder.MAPPERS.removeAll(parsedMappers);
			Iterator<Class<?>> mapperClassIterator = MapperHolder.MAPPERS.iterator();
			String mapperClassName = null;
			String xml = new StringBuilder()
					.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
					.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >")
			 		.append("<mapper namespace=\"%s\">")
			 		.append("</mapper>").toString();
			while(mapperClassIterator.hasNext()){
				mapperClassName = mapperClassIterator.next().getName();
				try {
					InputStream in = new ByteArrayInputStream(String.format(xml,mapperClassName).getBytes());;
					MyXMLMapperBuilder mapperBuilder = new MyXMLMapperBuilder(in, configuration,
							mapperClassName, configuration.getSqlFragments());
					mapperBuilder.parse();
				} catch (Exception arg19) {
					throw new NestedIOException("Failed to parse mapping resource: \'" + mapperClassName + "\'", arg19);
				} finally {
					ErrorContext.instance().reset();
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Parsed mapper file: \'" + mapperClassName + "\'");
				}
			}
			//完毕
		} else if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Property \'mapperLocations\' was not specified or no matching resources found");
		}

		return this.sqlSessionFactoryBuilder.build(configuration);
	}

	public SqlSessionFactory getObject() throws Exception {
		if (this.sqlSessionFactory == null) {
			this.afterPropertiesSet();
		}

		return this.sqlSessionFactory;
	}

	public Class<? extends SqlSessionFactory> getObjectType() {
		return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
	}

	public boolean isSingleton() {
		return true;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (this.failFast && event instanceof ContextRefreshedEvent) {
			this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
		}

	}

}
