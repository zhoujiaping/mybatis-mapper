package cn.zhou.mapper.page;

import java.util.List;

public class PageRes<T> {
    private Integer total;
    private List<T> rows;
    public static <T> PageRes<T> of(Pageable page,List<T> rows){
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
    
}
