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

public class WamBeans {

	private List<WordBean> beans;
	private int docTotal;
	private int docNum;
	private String relation;

	public WamBeans() {
		beans = new ArrayList<WordBean>();
	}

	public int getDocTotal() {
		return docTotal;
	}

	public void setDocTotal(int docTotal) {
		this.docTotal = docTotal;
	}

	public int getDocNum() {
		return docNum;
	}

	public void setDocNum(int docNum) {
		this.docNum = docNum;
	}

	public void add(WordBean bean) {
		beans.add(bean);
	}

	public boolean addAll(List<WordBean> beans) {
		return this.beans.addAll(beans);
	}

	public WordBean get(int index) {
		return beans.get(index);
	}

	public boolean isEmpty() {
		return beans.isEmpty();
	}

	public Iterator<WordBean> iterator() {
		return beans.iterator();
	}

	public List<WordBean> list() {
		return beans;
	}

	public WordBean remove(int index) {
		return beans.remove(index);
	}

	public boolean remove(WordBean bean) {
		return beans.remove(bean);
	}

	public boolean removeAll(List<WordBean> beans) {
		return this.beans.removeAll(beans);
	}

	public WordBean set(int index, WordBean bean) {
		return beans.set(index, bean);
	}

	public int size() {
		return beans.size();
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String toString() {
		return relation;
	}

}
