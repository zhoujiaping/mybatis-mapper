package cn.howso.mybatis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.mapping.ResultMapping;

/**
 * 支持mapper方法注解使用SqlProvider实现带标签的动态sql。 SELECT注解使用script支持带标签的写法,但是注解中没法用java代码编程，它只能是字符串常量。
 * SelectProvider支持java代码编程，但是不支持script。为了结合静态的xml和动态的java的优势， 修改了mybatis的源代码。 性能问题，可以通过缓存和装饰器模式实现。 目前暂不考虑性能问题。
 */
public class ScriptSqlProvider {

    public static final String lineSeparator = System.lineSeparator();

    public String selectByPrimaryKey(XMLMapperConf helper) throws ClassNotFoundException {
        Set<String> columns = helper.getMappedColumns();
        ResultMapping idResultMapping = helper.getIdResultMapping();
        List<String> sql = new ArrayList<>();
        sql.add("select");
        sql.add(String.join(",", columns));
        sql.add("from " + helper.getTablename());
        sql.add(String.format("where %s=#{id,jdbcType=%s}", idResultMapping.getColumn(),
                idResultMapping.getJdbcType()));
        return wrapScript(String.join(lineSeparator, sql));
        // return "<script>select * from sys_user <where>id=#{id}</where></script>";
    }

    public String countByExample(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("select count(*) from " + helper.getTablename());
        sql.add("<if test='_parameter != null'>");
        sql.add(whereClause(helper));
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String deleteByExample(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("delete from " + helper.getTablename());
        sql.add("<if test='_parameter != null'>");
        sql.add(whereClause(helper));
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String deleteByPrimaryKey(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        ResultMapping idMP = helper.getIdResultMapping();
        sql.add("delete from " + helper.getTablename());
        sql.add(String.format("where %s= #{id,jdbcType=%s}", idMP.getColumn(), idMP.getJdbcType()));
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String insert(XMLMapperConf helper) {
        //getMappedColumns和getResultMappings的字段顺序不是一致的。
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + helper.getTablename());
        sql.add(helper.getResultMappings().stream().map(item->item.getColumn()).collect(Collectors.joining(",", "(", ")")));
        sql.add("values ");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return "#{" + mapping.getProperty() + ",jdbcType=" + mapping.getJdbcType() + "}";
        }).collect(Collectors.joining(",", "(", ")")));
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String insertSelective(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + helper.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='%s != null'>%s,</if>", mapping.getProperty(), mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("<trim prefix='values (' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='%s != null'>#{%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String selectByExample(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("select");
        sql.add("<if test='distinct' >");
        sql.add("distinct");
        sql.add("</if>");
        sql.add("'false' as QUERYID,");
        sql.add(String.join(",", helper.getMappedColumns()));
        sql.add("from " + helper.getTablename());
        sql.add("<if test='_parameter != null' >");
        sql.add(whereClause(helper));
        sql.add("</if>");
        sql.add("<if test='orderByClause != null'>");
        sql.add("order by ${orderByClause}");
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }
    public String selectUniqueByExample(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("select");
        sql.add("<if test='distinct' >");
        sql.add("distinct");
        sql.add("</if>");
        sql.add("'false' as QUERYID,");
        sql.add(String.join(",", helper.getMappedColumns()));
        sql.add("from " + helper.getTablename());
        sql.add("<if test='_parameter != null' >");
        sql.add(whereClause(helper));
        sql.add("</if>");
        sql.add("<if test='orderByClause != null'>");
        sql.add("order by ${orderByClause}");
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String updateByExampleSelective(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("update " + helper.getTablename());
        sql.add("<set>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='record.%s != null'>%s=#{record.%s,jdbcType=%s},</if>",
                    mapping.getProperty(), mapping.getColumn(), mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</set>");
        sql.add("<if test='_parameter != null'>");
        sql.add(exampleWhereClause(helper));
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String updateByExample(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("update " + helper.getTablename());
        sql.add(" set ");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("%s = #{record.%s,jdbcType=%s}", mapping.getColumn(), mapping.getProperty(),
                    mapping.getJdbcType());
        }).collect(Collectors.joining("," + lineSeparator)));
        sql.add(" <if test='_parameter != null'>");
        sql.add(exampleWhereClause(helper));
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String updateByPrimaryKeySelective(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();

        ResultMapping idRM = helper.getIdResultMapping();
        sql.add("update " + helper.getTablename());
        sql.add("<set>");
        sql.add(helper.getResultMappings().stream().filter(mapping->!mapping.getColumn().equals(idRM.getColumn())).map(mapping -> {
            return String.format("<if test='%s != null'>%s = #{%s,jdbcType=%s},</if>",
                    mapping.getProperty(), mapping.getColumn(), mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</set>");
        sql.add(String.format("where %s=#{%s,jdbcType=%s}", idRM.getColumn(), idRM.getProperty(),
                idRM.getJdbcType()));
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String updateByPrimaryKey(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        ResultMapping idRM = helper.getIdResultMapping();
        sql.add("update " + helper.getTablename());
        sql.add(" set ");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("%s = #{%s,jdbcType=%s}", mapping.getColumn(), mapping.getProperty(),
                    mapping.getJdbcType());
        }).collect(Collectors.joining(",")));
        sql.add(String.format("where %s=#{%s,jdbcType=%s}", idRM.getColumn(), idRM.getProperty(),
                idRM.getJdbcType()));
        return wrapScript(String.join(lineSeparator, sql));
    }

    /** 由插件去实现分页，该方法只是配合插件 */

    public String selectByExampleByPage(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("select");
        sql.add("<if test='example.distinct' >");
        sql.add("distinct");
        sql.add("</if>");
        sql.add("'false' as QUERYID,");
        sql.add(String.join(",", helper.getMappedColumns()));
        sql.add("from " + helper.getTablename());
        sql.add("<if test='_parameter != null' >");
        sql.add(exampleWhereClause(helper));
        sql.add("</if>");
        sql.add("<if test='example.orderByClause != null'>");
        sql.add("order by ${example.orderByClause}");
        sql.add("</if>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    public String batchInsert(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + helper.getTablename());
        sql.add(helper.getResultMappings().stream().map(item->item.getColumn()).collect(Collectors.joining(",","(",")")));
        sql.add("values");
        sql.add("<foreach collection='recordList' item='record' separator=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("#{record.%s,jdbcType=%s}", mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(",", "(", ")")));
        sql.add("</foreach>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    /**
     * 根据第一条记录判断插入哪些字段。
     */

    public String batchInsertSelective(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + helper.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='recordList[0].%s != null'>%s,</if>", mapping.getProperty(),
                    mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("values");
        sql.add("<foreach collection='recordList' item='record' separator=','>");
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='recordList[0].%s != null'>#{record.%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("</foreach>");
        return wrapScript(String.join(lineSeparator, sql));
    }

    /*public String insertSelectiveSelectKey(XMLMapperConf helper){
        List<String> sql = new ArrayList<>();
        sql.add("insert into " + helper.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='%s != null'>%s,</if>", mapping.getProperty(), mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("<trim prefix='values (' suffix=')' suffixOverrides=','>");
        sql.add(helper.getResultMappings().stream().map(mapping -> {
            return String.format("<if test='%s != null'>#{%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        return wrapScript(String.join(lineSeparator, sql));
    }*/
    public String selectKey(XMLMapperConf helper){
        List<String> sql = new ArrayList<>();
        sql.add(String.format("select nextVal('%s_id_seq')", helper.getTablename()));
        return wrapScript(String.join(lineSeparator, sql));
    }
    private String exampleWhereClause(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("<where>");
        sql.add("<foreach collection='example.oredCriteria' item='criteria' separator='or'>");
        sql.add("<if test='criteria.valid'>");
        sql.add("<trim prefix='(' prefixOverrides='and' suffix=')'>");
        sql.add("<foreach collection='criteria.criteria' item='criterion'>");
        sql.add("<choose>");
        sql.add("<when test='criterion.noValue'>");
        sql.add("and ${criterion.condition}");
        sql.add("</when>");
        sql.add("<when test='criterion.singleValue'>");
        sql.add("and ${criterion.condition} #{criterion.value}");
        sql.add("</when>");
        sql.add("<when test='criterion.betweenValue'>");
        sql.add("and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}");
        sql.add("</when>");
        sql.add("<when test='criterion.listValue'>");
        sql.add("and ${criterion.condition}");
        sql.add("<foreach close=')' collection='criterion.value' item='listItem' open='(' separator=','>");
        sql.add("#{listItem}");
        sql.add("</foreach>");
        sql.add("</when>");
        sql.add("</choose>");
        sql.add("</foreach>");
        sql.add("</trim>");
        sql.add("</if>");
        sql.add("</foreach>");
        sql.add("</where>");
        return String.join(lineSeparator, sql);
    }
    private String whereClause(XMLMapperConf helper) {
        List<String> sql = new ArrayList<>();
        sql.add("<where>");
        sql.add("<foreach collection='oredCriteria' item='criteria' separator='or'>");
        sql.add("<if test='criteria.valid'>");
        sql.add("<trim prefix='(' prefixOverrides='and' suffix=')'>");
        sql.add("<foreach collection='criteria.criteria' item='criterion'>");
        sql.add("<choose>");
        sql.add("<when test='criterion.noValue'>");
        sql.add("and ${criterion.condition}");
        sql.add("</when>");
        sql.add("<when test='criterion.singleValue'>");
        sql.add("and ${criterion.condition} #{criterion.value}");
        sql.add("</when>");
        sql.add("<when test='criterion.betweenValue'>");
        sql.add("and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}");
        sql.add("</when>");
        sql.add("<when test='criterion.listValue'>");
        sql.add("and ${criterion.condition}");
        sql.add("<foreach close=')' collection='criterion.value' item='listItem' open='(' separator=','>");
        sql.add("#{listItem}");
        sql.add("</foreach>");
        sql.add("</when>");
        sql.add("</choose>");
        sql.add("</foreach>");
        sql.add("</trim>");
        sql.add("</if>");
        sql.add("</foreach>");
        sql.add("</where>");
        return String.join(lineSeparator, sql);
    }

    public String wrapScript(String sql) {
        return "<script>" + sql + "</script>";
    }
}
