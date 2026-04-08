package cn.exrick.manager.isearch.query.bean;

public class RelationBean {
	
	private int id;
	private String word;
	private int docnum;
	private double idf;
	private int mark;
	private double radius;
	private double angle;
	private RelationBean parent;
	
	public RelationBean() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDocnum() {
		return docnum;
	}

	public void setDocnum(int docnum) {
		this.docnum = docnum;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public RelationBean getParent() {
		return parent;
	}

	public void setParent(RelationBean parent) {
		this.parent = parent;
	}
	

}
