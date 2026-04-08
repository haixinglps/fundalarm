package cn.exrick.manager.isearch.query.bean;

/**
 * 检索结果
 * 
 * @author guild
 * 
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchBeans {
	
	private List<SearchBean> beans = new ArrayList<SearchBean>();
	
	private int total = 0;
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public void add(int index, SearchBean bean) {
		beans.add(index, bean);
	}
	
	public boolean add(SearchBean bean) {
		return beans.add(bean);
	}
	
	public boolean addAll(List<SearchBean> beans) {
		return this.beans.addAll(beans);
	}
	
	public SearchBean get(int index) {
		return beans.get(index);
	}
	
	public boolean isEmpty() {
		return beans.isEmpty();
	}
	
	public Iterator<SearchBean> iterator() {
		return beans.iterator();
	}
	
	public List<SearchBean> list() {
		return beans;
	}
	
	public SearchBean remove(int index) {
		return beans.remove(index);
	}
	
	public boolean remove(SearchBean bean) {
		return beans.remove(bean);
	}
	
	public boolean removeAll(List<SearchBean> beans) {
		return this.beans.removeAll(beans);
	}
	
	public SearchBean set(int index, SearchBean bean) {
		return beans.set(index, bean);
	}
	
	public SearchBeans subBeans(int fromIndex, int toIndex) {
		List<SearchBean> subBeanList = beans.subList(fromIndex, toIndex);
		SearchBeans beans = new SearchBeans();
		beans.addAll(subBeanList);
		return beans;
	}
	
	public int size() {
		return beans.size();
	}
	
}
