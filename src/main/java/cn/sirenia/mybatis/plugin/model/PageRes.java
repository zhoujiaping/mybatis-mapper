package cn.sirenia.mybatis.plugin.model;

import java.io.Serializable;
import java.util.List;

public class PageRes<T> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer total;
    private List<T> rows;
    public static <T> PageRes<T> of(List<T> rows,Pageable page){
        PageRes<T> pageRes = new PageRes<>();
        pageRes.total = page.getTotal();
        pageRes.setRows(rows);
        return pageRes;
    }
    public Integer getTotal() {
        return total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public List<T> getRows() {
        return rows;
    }
    
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
    public static <T> PageRes<T> of(List<T> rows,int total) {
        PageRes<T> pageRes = new PageRes<>();
        pageRes.total = total;
        pageRes.setRows(rows);
        return pageRes;
    }
    
}
