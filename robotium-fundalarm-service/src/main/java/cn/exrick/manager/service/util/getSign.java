package cn.exrick.manager.service.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class getSign {
	static String urlString = "https://cx2z.52mj.vip/player.php?id=20708sid=1";

	public static String getStr() throws IOException {
//	  <video class="yzmplayer-video yzmplayer-video-current" webkit-playsinline="" playsinline="" preload="auto" src="undefined?sign=f387e4b9f9db117580143f0c243b6d66a24324f9bbf18225bb9f04740d682a653f8324bbd29946d003dc69752e2cb1f6">
//	    
//	  </video>
		Document doc = Jsoup.connect(urlString).get();
		String html = doc.outerHtml();
		int beginindex = html.indexOf("?sign=");
		int endindex = html.indexOf("//视频链接");
//		System.out.println(html);
		String videostr = html.substring(beginindex, endindex);
		int signindex = videostr.indexOf("sign=");
		String signstr = videostr.substring(signindex + 5);
		int signendindex = signstr.indexOf("\"");
		String signreal = signstr.substring(0, signendindex);
		System.out.println(signreal);
		return signreal;

	}

	public static void main(String[] args) {
		try {
			getStr();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
