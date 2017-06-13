package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.dao.BaseMapper;
import cn.zhou.mapper.model.Incident;
import cn.zhou.mapper.model.IncidentExample;

@Table(name = "sy_incident")
public interface IncidentMapper extends BaseMapper<Incident, IncidentExample, Integer> {
	/*@Override
	@ResultType(Integer.class)
	@InsertProvider(type = ScriptSqlProviderImpl.class, method = "insertSelectiveSelectKey")
    @SelectKey(before = true, statement = "select nextVal('sy_incident_id_seq')", keyColumn = "id", keyProperty = "id", resultType = Integer.class, statementType = StatementType.STATEMENT)*/
    int insertSelectiveSelectKey(Incident record);
	//@Override
	//int insert(Incident record);
}
