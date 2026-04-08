package cn.exrick.manager.isearch.query.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.exrick.manager.isearch.query.bean.compare.ComparatorByKey;
import cn.exrick.manager.isearch.query.bean.compare.ComparatorByValue;



/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class CubeBean {

	private String key;
	private int num;
	private List<GroupBean> subBeans;

	public CubeBean() {
		subBeans = new ArrayList<GroupBean>();
	}

	public CubeBean(String key, int num, GroupBean bean) {
		this.key = key;
		this.num = num;
		subBeans = new ArrayList<GroupBean>();
		subBeans.add(bean);
	}

	public CubeBean(String key, int num, List<GroupBean> subBeans) {
		this.key = key;
		this.num = num;
		this.subBeans = subBeans;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<GroupBean> subList() {
		return subBeans;
	}

	public void setSubBeans(List<GroupBean> subBeans) {
		this.subBeans = subBeans;
	}

	public int subsize() {
		return subBeans.size();
	}

	public GroupBean getSub(int index) {
		return subBeans.get(index);
	}

	public void addSub(GroupBean bean) {
		subBeans.add(bean);
	}

	public void sortByKey() {
		Collections.sort(subBeans, new ComparatorByKey<GroupBean>());
	}

	public void sortByValue() {
		Collections.sort(subBeans, new ComparatorByValue<GroupBean>());
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.key + " : " + this.num + " / " + subsize());
		for (int i = 0; i < subBeans.size(); i++) {
			if (i == 0)
				sBuilder.append("(" + subBeans.get(i).toString() + ", ");
			else if (i == subBeans.size() - 1)
				sBuilder.append(subBeans.get(i).toString() + ")");
			else
				sBuilder.append(subBeans.get(i).toString() + ", ");
		}
		return sBuilder.toString();
	}

}
