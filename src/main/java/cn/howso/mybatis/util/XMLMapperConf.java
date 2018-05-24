package cn.howso.mybatis.util;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

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
    private String optiColumnName;
    private String optiPropertyName;
    
	public static XMLMapperConf of(Configuration configuration,String mapperClazzName, String dialect, String optiColumnName) throws ClassNotFoundException{
		XMLMapperConf conf = new XMLMapperConf();
		Class<?> clazz = Class.forName(mapperClazzName);
    	Table table = clazz.getAnnotation(Table.class);
    	if(table == null){
    		throw new RuntimeException(String.format("BaseMapper需要和%s一起使用", Table.class.getName()));
    	}
    	conf.optiColumnName = optiColumnName;
    	conf.optiPropertyName = Beans.underline2camel(optiColumnName);
    	conf.dialect = dialect;
    	conf.tablename = table.name();
    	conf.resultMap = configuration.getResultMap(mapperClazzName+".BaseResultMap");
        conf.idResultMapping = conf.resultMap.getIdResultMappings().get(0);
        conf.mappedColumns = conf.resultMap.getMappedColumns();
        conf.resultMappings = conf.resultMap.getResultMappings();
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
	public String getOptiColumnName() {
		return optiColumnName;
	}
	public String getOptiPropertyName() {
		return optiPropertyName;
	}
}
