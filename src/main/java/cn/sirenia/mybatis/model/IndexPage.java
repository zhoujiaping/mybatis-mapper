package cn.sirenia.mybatis.model;

/**
 * <p>
 * mybatis的分页插件需要的model
 * </p>
 * 
 * @author wzf
 * @date 2016年3月15日 上午9:16:24
 */
public class IndexPage extends Pageable{

    /** 当前页,从1开始 */
    private int index;

    /** 每页显示记录数 */
    private int size;
    public static IndexPage of(int index,int size){
        IndexPage p = new IndexPage();
        p.index = index;
        p.size = size;
        return p;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    @Override
    public String createPageSql(String sql, String dialect) {
        int limit = size;
        int offset = (index-1)*size;
        return super.createPageSql(sql, dialect, limit, offset);
    }
}
