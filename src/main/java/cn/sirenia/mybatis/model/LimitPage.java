package cn.sirenia.mybatis.model;

public class LimitPage extends Pageable{

    private int limit = 20; // 每页显示记录数
    private int offset = 0; // 每页显示记录数

    public static LimitPage of(int limit,int offset){
        LimitPage page = new LimitPage();
        page.setLimit(limit);
        page.setOffset(offset);
        return page;
    }
    public long getLimit() {
        return limit;
    }

    
    public void setLimit(int limit) {
        this.limit = limit;
    }

    
    public long getOffset() {
        return offset;
    }

    
    public void setOffset(int offset) {
        this.offset = offset;
    }

    
    @Override
    public String toString() {
        return "Page [limit=" + limit + ", offset=" + offset + ", total=" + total + "]";
    }
    @Override
    public String createPageSql(String sql, String dialect) {
        return super.createPageSql(sql, dialect, limit, offset);
    }
    
}
