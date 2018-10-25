package cn.sirenia.mybatis.plugin.model;

import java.io.Serializable;

public abstract class Pageable implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int total; 
    public void setTotal(int total){
        this.total = total;
    }
    
    public int getTotal() {
        return total;
    }
    public abstract String createPageSql(String sql, String dialect);

    public String createPageSql(String sql, String dialect, int limit, int offset) {
        StringBuffer pageSql = new StringBuffer();
        if (dialect.startsWith("mysql")) {
            pageSql.append(sql);
            pageSql.append(" limit " + offset + "," + limit);
        } else if (dialect.startsWith("oracle")) {
            pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
            pageSql.append(sql);
            pageSql.append(") as tmp_tb where ROWNUM<=");
            pageSql.append(offset + limit);
            pageSql.append(") where row_id>");
            pageSql.append(offset);
        } else if (dialect.startsWith("postgre")) {
            pageSql.append(sql);
            pageSql.append(" limit " + limit + " offset  " + offset);
        }
        return pageSql.toString();
    }

}
