package cn.zhou.mapper.util;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import cn.zhou.mapper.anno.Table;

public class XMLMapperConf {
    private String tablename;
    private Set<String> mappedColumns;
    private Set<String> mappedProperties;
    private List<ResultMapping> resultMappings;
    private ResultMapping idResultMapping;
    private ResultMap resultMap;
    
	public static XMLMapperConf of(Configuration configuration,String mapperClazzName) throws ClassNotFoundException{
		XMLMapperConf conf = new XMLMapperConf();
		Class<?> clazz = Class.forName(mapperClazzName);
    	Table table = clazz.getAnnotation(Table.class);
    	conf.tablename = table.name();
    	conf.resultMap = configuration.getResultMap(mapperClazzName+".BaseResultMap");
        conf.idResultMapping = conf.resultMap.getIdResultMappings().get(0);
        conf.mappedColumns = conf.resultMap.getMappedColumns();
        conf.resultMappings = conf.resultMap.getResultMappings();
        return conf;
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
	
}
