package cn.sirenia.mybatis.plugin.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * 为什么要实现List接口？
 * 1：mapper的返回值类型会被作为依据，执行查询单条或者查询集合。
 * 2：框架中返回值结果必须是List类型。
 * 所以实现List只是为了不报错。
 * 但是问题来了，自定义的类继承Collection或者其子接口、或者Map，在rpc序列化和反序列化时会出问题。
 * @param <T>
 */
public class PageResWraper<T> implements List<T>,Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer total;
    private List<T> rows;
    public PageResWraper(){
    	super();
    }
    public static <T> PageResWraper<T> of(List<T> rows,Pageable page){
        PageResWraper<T> pageRes = new PageResWraper<>();
        pageRes.total = page.getTotal();
        pageRes.setRows(rows);
        return pageRes;
    }
    public PageRes<T> getPageRes(){
    	return PageRes.of(rows, total);
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
    public static <T> PageResWraper<T> of(List<T> rows,int size) {
        PageResWraper<T> pageRes = new PageResWraper<>();
        pageRes.total = size;
        pageRes.setRows(rows);
        return pageRes;
    }
    public PageRes<T> toPageRes(){
        return PageRes.of(rows,total);
    }
	@Override
	public int size() {
		return rows.size();
	}
	@Override
	public boolean isEmpty() {
		return rows.isEmpty();
	}
	@Override
	public boolean contains(Object o) {
		return rows.contains(o);
	}
	@Override
	public Iterator<T> iterator() {
		return rows.iterator();
	}
	@Override
	public Object[] toArray() {
		return rows.toArray();
	}
	@Override
	public <E> E[] toArray(E[] a) {
		return rows.toArray(a);
	}
	@Override
	public boolean add(T e) {
		return rows.add(e);
	}
	@Override
	public boolean remove(Object o) {
		return rows.remove(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return rows.contains(c);
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return rows.addAll(c);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return rows.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return rows.retainAll(c);
	}
	@Override
	public void clear() {
		rows.clear();
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return rows.addAll(index, c);
	}
	@Override
	public T get(int index) {
		return rows.get(index);
	}
	@Override
	public T set(int index, T element) {
		return rows.set(index, element);
	}
	@Override
	public void add(int index, T element) {
		rows.add(index,element);
	}
	@Override
	public T remove(int index) {
		return rows.remove(index);
	}
	@Override
	public int indexOf(Object o) {
		return rows.indexOf(o);
	}
	@Override
	public int lastIndexOf(Object o) {
		return rows.lastIndexOf(o);
	}
	@Override
	public ListIterator<T> listIterator() {
		return rows.listIterator();
	}
	@Override
	public ListIterator<T> listIterator(int index) {
		return rows.listIterator(index);
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return rows.subList(fromIndex, toIndex);
	}
    
}
