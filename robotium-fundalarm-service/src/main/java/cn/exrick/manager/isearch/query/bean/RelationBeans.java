package cn.exrick.manager.isearch.query.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationBeans {

	private List<RelationBean> beans = new ArrayList<RelationBean>();

	private int total = 0;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void add(int index, RelationBean bean) {
		beans.add(index, bean);
	}

	public boolean add(RelationBean bean) {
		return beans.add(bean);
	}

	public boolean addAll(List<RelationBean> beans) {
		return this.beans.addAll(beans);
	}

	public RelationBean get(int index) {
		return beans.get(index);
	}

	public boolean isEmpty() {
		return beans.isEmpty();
	}

	public Iterator<RelationBean> iterator() {
		return beans.iterator();
	}

	public List<RelationBean> list() {
		return beans;
	}

	public RelationBean remove(int index) {
		return beans.remove(index);
	}

	public boolean remove(RelationBean bean) {
		return beans.remove(bean);
	}

	public boolean removeAll(List<RelationBean> beans) {
		return this.beans.removeAll(beans);
	}

	public RelationBean set(int index, RelationBean bean) {
		return beans.set(index, bean);
	}

	public RelationBeans subBeans(int fromIndex, int toIndex) {
		List<RelationBean> subBeanList = beans.subList(fromIndex, toIndex);
		RelationBeans beans = new RelationBeans();
		beans.addAll(subBeanList);
		return beans;
	}

	public int size() {
		return beans.size();
	}

}
