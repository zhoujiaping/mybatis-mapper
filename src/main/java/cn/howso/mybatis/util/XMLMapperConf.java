package cn.howso.mybatis.util;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import cn.howso.mybatis.anno.OptimisticLock;
import cn.howso.mybatis.anno.Table;

public class XMLMapperConf {
    private String tablename;
    private Set<String> mappedColumns;
    private Set<String> mappedProperties;
    private List<ResultMapping> resultMappings;
    private ResultMapping idResultMapping;
    private ResultMap resultMap;
    private boolean enableOptimisticLock;//是否启用乐观锁
    private String dialect;
    
    
    public static final String OPTI_COLUMN_NAME = "opti_version";
    private static final Set<String> OPTI_JAVA_TYPE_NAME_SET = new HashSet<>();
    static{
    	OPTI_JAVA_TYPE_NAME_SET.add(Integer.class.getName());
    	OPTI_JAVA_TYPE_NAME_SET.add(Long.class.getName());
    }
    
	public static XMLMapperConf of(Configuration configuration,String mapperClazzName, String dialect) throws ClassNotFoundException{
		XMLMapperConf conf = new XMLMapperConf();
		Class<?> clazz = Class.forName(mapperClazzName);
    	Table table = clazz.getAnnotation(Table.class);
    	if(table == null){
    		throw new RuntimeException(String.format("BaseMapper需要和%s一起使用", Table.class.getName()));
    	}
    	
    	conf.tablename = table.name();
    	conf.resultMap = configuration.getResultMap(mapperClazzName+".BaseResultMap");
        conf.idResultMapping = conf.resultMap.getIdResultMappings().get(0);
        conf.mappedColumns = conf.resultMap.getMappedColumns();
        conf.resultMappings = conf.resultMap.getResultMappings();
        OptimisticLock optiLock = clazz.getAnnotation(OptimisticLock.class);
        if(optiLock == null){
        	conf.enableOptimisticLock = false;
        }else{
        	conf.enableOptimisticLock = true;
        	//校验乐观锁字段是否为整数类型
        	Optional<ResultMapping> optiVersionRM = conf.resultMappings.stream().filter(rm->{
        		return rm.getColumn().equals(OPTI_COLUMN_NAME) && OPTI_JAVA_TYPE_NAME_SET.contains(rm.getJavaType().getName());
        	}).findFirst();
        	optiVersionRM.orElseThrow(()->{
        		throw new RuntimeException("未找到乐观锁字段或者乐观锁字段类型错误");
        	});
        }
        return conf;
	}

	public boolean isEnableOptimisticLock() {
		return enableOptimisticLock;
	}
	public String getTablename() {
		return tablename;
	}

	public Set<String> getMappedColumns() {
		return mappedColumns;
	}

	public Set<String> getMappedProperties() {
		return mappedProperties;
	}

	public List<ResultMapping> getResultMappings() {
		return resultMappings;
	}

	public ResultMapping getIdResultMapping() {
		return idResultMapping;
	}

	public ResultMap getResultMap() {
		return resultMap;
	}
	public String getDialect() {
		return dialect;
	}
	
}
