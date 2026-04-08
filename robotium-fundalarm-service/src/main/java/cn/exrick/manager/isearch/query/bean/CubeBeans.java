package cn.exrick.manager.isearch.query.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.exrick.manager.isearch.query.bean.compare.ComparatorCubeByKey;
import cn.exrick.manager.isearch.query.bean.compare.ComparatorCubeByValue;



/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class CubeBeans {

	private int total = 0;
	private List<CubeBean> beans;
	private List<GroupBean> subBeans = new ArrayList<GroupBean>();

	public CubeBeans() {
		beans = new ArrayList<CubeBean>();
	}

	public void add(CubeBean bean) {
		total += bean.getNum();
		beans.add(bean);
	}

	public CubeBean get(int index) {
		return beans.get(index);
	}

	public int size() {
		return beans.size();
	}
	
	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void sortByKey() {
		Collections.sort(beans, new ComparatorCubeByKey<CubeBean>());
	}

	public void sortByValue() {
		Collections.sort(beans, new ComparatorCubeByValue<CubeBean>());
	}

	public void sortSubByKey() {
		for (int i = 0; i < beans.size(); i++) {
			CubeBean bean = beans.get(i);
			bean.sortByKey();
		}
	}

	public void sortSubByValue() {
		for (int i = 0; i < beans.size(); i++) {
			CubeBean bean = beans.get(i);
			bean.sortByValue();
		}
	}
	
	public List<CubeBean> list() {
		return beans;
	}

	public void setSubBeans(List<GroupBean> subBeans) {
		this.subBeans = subBeans;
	}

	public List<GroupBean> getSubBeans() {
		return subBeans;
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("总文章数: " + total + ", 总记录数: " + beans.size() + " \r\n");
		for (int i = 0; i < beans.size(); i++) {
			CubeBean bean = beans.get(i);
			List<GroupBean> subList = bean.subList();
			sBuilder.append(bean.getKey() + " : " + bean.getNum() + " [");
			for (GroupBean subBean : subList) {
				sBuilder.append(subBean + ", ");
			}
			sBuilder.replace(sBuilder.length() - 2, sBuilder.length(), "");
			sBuilder.append("] \r\n");
		}
		sBuilder.append("二字段 分组 \r\n");
		for (int i = 0; i < subBeans.size(); i++) {
			GroupBean bean = subBeans.get(i);
			sBuilder.append(bean.getKey() + ":" + bean.getNum() + " \r\n");
		}
		return sBuilder.toString();
	}

}
