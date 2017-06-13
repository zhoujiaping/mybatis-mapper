package cn.zhou.mapper.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.zhou.mapper.util.MapperConf;
import cn.zhou.mapper.util.ResMap;

/**
 * 支持mapper方法注解使用SqlProvider实现带标签的动态sql。 SELECT注解使用script支持带标签的写法,但是注解中没法用java代码编程，它只能是字符串常量。
 * SelectProvider支持java代码编程，但是不支持script。为了结合静态的xml和动态的java的优势， 修改了mybatis的源代码。 性能问题，可以通过缓存和装饰器模式实现。 目前暂不考虑性能问题。
 */
public class XMLMapperSqlProvider {

    public static final String lineSeparator = System.lineSeparator();

    public String selectByPrimaryKey(MapperConf conf) throws ClassNotFoundException {
        Set<String> columns = conf.getColumns();
        ResMap idResMap = conf.getIdResMap();
        List<String> sql = new ArrayList<>();
        sql.add("<select id='selectByPrimaryKey' resultMap='BaseResultMap' parameterType='map'>");
        sql.add("select");
        sql.add(String.join(",", columns));
        sql.add("from " + conf.getTablename());
        sql.add(String.format("where %s=#{id,jdbcType=%s}", idResMap.getColumn(), idResMap.getJdbcType()));
        sql.add("</select>");
        return String.join(lineSeparator, sql);
    }

    public String countByExample(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<select id='countByExample' parameterType='object' resultType='java.lang.Integer'>");
        sql.add("select count(*) from " + conf.getTablename());
        sql.add("<if test='_parameter != null'>");
        sql.add(whereClause(conf));
        sql.add("</if>");
        sql.add("</select>");
        return String.join(lineSeparator, sql);
    }

    public String deleteByExample(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<delete id='deleteByExample' parameterType='object'>");
        sql.add("delete from " + conf.getTablename());
        sql.add("<if test='_parameter != null'>");
        sql.add(whereClause(conf));
        sql.add("</if>");
        sql.add("</delete>");
        return String.join(lineSeparator, sql);
    }

    public String deleteByPrimaryKey(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        ResMap idMP = conf.getIdResMap();
        sql.add(" <delete id='deleteByPrimaryKey' parameterType='map'>");
        sql.add("delete from " + conf.getTablename());
        sql.add(String.format("where %s= #{id,jdbcType=%s}", idMP.getColumn(), idMP.getJdbcType()));
        sql.add("</delete>");
        return String.join(lineSeparator, sql);
    }

    public String insert(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<insert id='insert' parameterType='object'>");
        sql.add("insert into " + conf.getTablename());
        sql.add(conf.getResMaps().stream().map(item -> item.getColumn()).collect(Collectors.joining(",", "(", ")")));
        sql.add("values ");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return "#{" + mapping.getProperty() + ",jdbcType=" + mapping.getJdbcType() + "}";
        }).collect(Collectors.joining(",", "(", ")")));
        sql.add("</insert>");
        return String.join(lineSeparator, sql);
    }

    public String insertSelective(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<insert id='insertSelective' parameterType='object'>");
        sql.add("insert into " + conf.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='%s != null'>%s,</if>", mapping.getProperty(), mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("<trim prefix='values (' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='%s != null'>#{%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("</insert>");
        return String.join(lineSeparator, sql);
    }

    public String selectByExample(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<select id='selectByExample' resultMap='BaseResultMap' parameterType='object'>");
        sql.add("select");
        sql.add("<if test='distinct' >");
        sql.add("distinct");
        sql.add("</if>");
        sql.add("'false' as QUERYID,");
        sql.add(String.join(",", conf.getColumns()));
        sql.add("from " + conf.getTablename());
        sql.add("<if test='_parameter != null' >");
        sql.add(whereClause(conf));
        sql.add("</if>");
        sql.add("<if test='orderByClause != null'>");
        sql.add("order by ${orderByClause}");
        sql.add("</if>");
        sql.add("</select>");
        return String.join(lineSeparator, sql);
    }

    public String updateByExampleSelective(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<update id='updateByExampleSelective' parameterType='map'>");
        sql.add("update " + conf.getTablename());
        sql.add("<set>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='record.%s != null'>%s=#{record.%s,jdbcType=%s},</if>",
                    mapping.getProperty(), mapping.getColumn(), mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</set>");
        sql.add("<if test='_parameter != null'>");
        sql.add(exampleWhereClause(conf));
        sql.add("</if>");
        sql.add("</update>");
        return String.join(lineSeparator, sql);
    }

    public String updateByExample(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<update id='updateByExample' parameterType='map'>");
        sql.add("update " + conf.getTablename());
        sql.add(" set ");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("%s = #{record.%s,jdbcType=%s}", mapping.getColumn(), mapping.getProperty(),
                    mapping.getJdbcType());
        }).collect(Collectors.joining("," + lineSeparator)));
        sql.add(" <if test='_parameter != null'>");
        sql.add(exampleWhereClause(conf));
        sql.add("</if>");
        sql.add("</update>");
        return String.join(lineSeparator, sql);
    }

    public String updateByPrimaryKeySelective(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        ResMap idRM = conf.getIdResMap();
        sql.add("<update id='updateByPrimaryKeySelective' parameterType='object'>");
        sql.add("update " + conf.getTablename());
        sql.add("<set>");
        sql.add(conf.getResMaps().stream().filter(mapping -> !mapping.getColumn().equals(idRM.getColumn()))
                .map(mapping -> {
                    return String.format("<if test='%s != null'>%s = #{%s,jdbcType=%s},</if>", mapping.getProperty(),
                            mapping.getColumn(), mapping.getProperty(), mapping.getJdbcType());
                }).collect(Collectors.joining(lineSeparator)));
        sql.add("</set>");
        sql.add(String.format("where %s=#{%s,jdbcType=%s}", idRM.getColumn(), idRM.getProperty(), idRM.getJdbcType()));
        sql.add("</update>");
        return String.join(lineSeparator, sql);
    }

    public String updateByPrimaryKey(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        ResMap idRM = conf.getIdResMap();
        sql.add("<update id='updateByPrimaryKey' parameterType='object'>");
        sql.add("update " + conf.getTablename());
        sql.add(" set ");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("%s = #{%s,jdbcType=%s}", mapping.getColumn(), mapping.getProperty(),
                    mapping.getJdbcType());
        }).collect(Collectors.joining(",")));
        sql.add(String.format("where %s=#{%s,jdbcType=%s}", idRM.getColumn(), idRM.getProperty(), idRM.getJdbcType()));
        sql.add("</update>");
        return String.join(lineSeparator, sql);
    }

    /** 由插件去实现分页，该方法只是配合插件 */

    public String selectByExampleByPage(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<select id='selectByExampleByPage' resultMap='BaseResultMap' parameterType='map'>");
        sql.add("select");
        sql.add("<if test='example.distinct' >");
        sql.add("distinct");
        sql.add("</if>");
        sql.add("'false' as QUERYID,");
        sql.add(String.join(",", conf.getColumns()));
        sql.add("from " + conf.getTablename());
        sql.add("<if test='_parameter != null' >");
        sql.add(exampleWhereClause(conf));
        sql.add("</if>");
        sql.add("<if test='example.orderByClause != null'>");
        sql.add("order by ${example.orderByClause}");
        sql.add("</if>");
        sql.add("</select>");
        return String.join(lineSeparator, sql);
    }

    public String batchInsert(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<insert id='batchInsert' parameterType='map'>");
        sql.add("insert into " + conf.getTablename());
        sql.add(conf.getResMaps().stream().map(item -> item.getColumn()).collect(Collectors.joining(",", "(", ")")));
        sql.add("values");
        sql.add("<foreach collection='recordList' item='record' separator=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("#{record.%s,jdbcType=%s}", mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(",", "(", ")")));
        sql.add("</foreach>");
        sql.add("</insert>");
        return String.join(lineSeparator, sql);
    }

    /**
     * 根据第一条记录判断插入哪些字段。
     */

    public String batchInsertSelective(MapperConf conf) {
        List<String> sql = new ArrayList<>();
        sql.add("<insert id='batchInsertSelective' parameterType='map'>");
        sql.add("insert into " + conf.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='recordList[0].%s != null'>%s,</if>", mapping.getProperty(),
                    mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("values");
        sql.add("<foreach collection='recordList' item='record' separator=','>");
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='record.%s != null'>#{record.%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("</foreach>");
        sql.add("</insert>");
        return String.join(lineSeparator, sql);
    }

    /**
     * @param helper
     * @return
     */
    public String insertSelectiveSelectKey(MapperConf conf) {
        ResMap idResMap = conf.getIdResMap();
        String idType = conf.getIdType();
        List<String> sql = new ArrayList<>();
        sql.add("<insert id='insertSelectiveSelectKey' parameterType='object'>");
        sql.add(String.format("<selectKey keyProperty='%s' resultType='%s' order='BEFORE'>",idResMap.getProperty(),idType));
        sql.add(String.format("select nextval('%s_id_seq')",conf.getTablename()));
        sql.add("</selectKey>");
        sql.add("insert into " + conf.getTablename());
        sql.add("<trim prefix='(' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='%s != null'>%s,</if>", mapping.getProperty(), mapping.getColumn());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("<trim prefix='values (' suffix=')' suffixOverrides=','>");
        sql.add(conf.getResMaps().stream().map(mapping -> {
            return String.format("<if test='%s != null'>#{%s,jdbcType=%s},</if>", mapping.getProperty(),
                    mapping.getProperty(), mapping.getJdbcType());
        }).collect(Collectors.joining(lineSeparator)));
        sql.add("</trim>");
        sql.add("</insert>");
        return String.join(lineSeparator, sql);
    }

    private String exampleWhereClause(MapperConf conf) {
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

    private String whereClause(MapperConf conf) {
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
}
