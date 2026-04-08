package cn.exrick.manager.isearch.query.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.exrick.manager.isearch.query.bean.compare.ComparatorByKey;
import cn.exrick.manager.isearch.query.bean.compare.ComparatorByValue;


/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class GroupBeans {

	private List<GroupBean> beans;
	private int total = 0;

	public GroupBeans() {
		beans = new ArrayList<GroupBean>();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean add(GroupBean bean) {
		return beans.add(bean);
	}

	public boolean addAll(List<GroupBean> beans) {
		return this.beans.addAll(beans);
	}

	public GroupBean get(int index) {
		return beans.get(index);
	}

	public boolean isEmpty() {
		return beans.isEmpty();
	}

	public Iterator<GroupBean> iterator() {
		return beans.iterator();
	}

	public List<GroupBean> list() {
		return beans;
	}

	public GroupBean remove(int index) {
		return beans.remove(index);
	}

	public boolean remove(GroupBean bean) {
		return beans.remove(bean);
	}

	public boolean removeAll(List<GroupBean> beans) {
		return this.beans.removeAll(beans);
	}

	public GroupBean set(int index, GroupBean bean) {
		return beans.set(index, bean);
	}

	public int size() {
		return beans.size();
	}

	public void sortByKey() {
		Collections.sort(beans, new ComparatorByKey<GroupBean>());
	}

	public void sortByValue() {
		Collections.sort(beans, new ComparatorByValue<GroupBean>());
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("总文章数: " + total + ", 总记录数: " + beans.size() + " \r\n");
		for (int i = 0; i < beans.size(); i++) {
			GroupBean bean = beans.get(i);
			sBuilder.append(bean.getKey() + " : " + bean.getNum() + " \r\n");
		}
		return sBuilder.toString();
	}

}
