package cn.exrick.manager.isearch.query.bean;

/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class WordBean {

	private String word;
	private int mark;
	private double idf;
	private double docnum;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getDocnum() {
		return docnum;
	}

	public void setDocnum(double docnum) {
		this.docnum = docnum;
	}

}
