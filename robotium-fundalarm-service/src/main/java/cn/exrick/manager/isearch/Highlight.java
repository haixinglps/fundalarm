package cn.exrick.manager.isearch;

import com.zhongsou.search.core.query.highlight.HighLight;

/**
 * 高亮显示
 * 
 * @author guiliangdong
 * 
 */
public class Highlight {

	private String startMark;
	private String endMark;
	private HighLight hl;

	public Highlight() {
		this("<em>", "</em>");
	}

	public Highlight(String startMark, String endMark) {
		this.startMark = startMark;
		this.endMark = endMark;
		hl = new HighLight();
	}

	public String highlight(String text, String word) {
		return highlight(text, word, -1);
	}

	public String highlight(String text, String word, int len) {
		if (text == null || text.length() == 0)
			return text;
		if (len == -1)
			len = text.length() * 2;
		if (word == null || word.length() == 0) {
			//System.out.println("hl0:"+hl.highLight(text, word, startMark, endMark, len));
			if (len > text.length())
				return text.substring(0, text.length());
			else
				return text.substring(0, len);
		}
		//System.out.println("hl1:"+hl.highLight(text, word, startMark, endMark, len));
		return hl.highLight(text, word, startMark, endMark, len);
	}

	public static void main(String[] args) {
		
	}

}
