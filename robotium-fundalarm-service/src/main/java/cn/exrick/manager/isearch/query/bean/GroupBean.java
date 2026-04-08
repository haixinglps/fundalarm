package cn.exrick.manager.isearch.query.bean;

/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class GroupBean {

	private String key;
	private int num;

	public GroupBean() {

	}

	public GroupBean(String key, int num) {
		this.key = key;
		this.num = num;
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

	public String toString() {
		return this.key + " : " + this.num;
	}

}
