package cn.sirenia.mybatis.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import cn.sirenia.mybatis.anno.Table;


public class XMLMapperConf {
	private String dialect;
	private String tablename;
	private Set<String> mappedColumns;
	private Set<String> mappedProperties;
	private List<ResultMapping> resultMappings;
	private ResultMapping idResultMapping;
	private ResultMapping optiLockResultMapping;
	private ResultMap resultMap;
	private String optiLockColumn;

	public static XMLMapperConf of(Configuration configuration, String mapperClazzName, String dialect)
			throws ClassNotFoundException {
		XMLMapperConf conf = new XMLMapperConf();
		Class<?> clazz = Class.forName(mapperClazzName);
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new RuntimeException(String.format("BaseMapper需要和%s一起使用", Table.class.getName()));
		}

		conf.tablename = table.name();
		conf.resultMap = configuration.getResultMap(mapperClazzName + ".BaseResultMap");
		conf.idResultMapping = conf.resultMap.getIdResultMappings().get(0);// 只支持单列主键
		conf.mappedColumns = conf.resultMap.getMappedColumns();
		conf.resultMappings = conf.resultMap.getResultMappings();
		conf.dialect = dialect;
		conf.optiLockColumn = table.optiLockColumn();
		Optional<ResultMapping> optiVersionRM = conf.resultMappings.stream().filter(rm -> {
			return rm.getColumn().equals(conf.optiLockColumn);
		}).findFirst();
		conf.optiLockResultMapping = optiVersionRM.orElse(null);
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

	public String getDialect() {
		return dialect;
	}

	public boolean isOptiMapping(ResultMapping rm) {
		// return rm.getColumn().equals(XMLMapperConf.OPTI_COLUMN_NAME);
		//return rm.getColumn().equals(optiLockResultMapping.getColumn());
		return rm.getColumn().equals(optiLockColumn);
	}

	public ResultMapping getOptiLockResultMapping() {
		return optiLockResultMapping;
	}

}
