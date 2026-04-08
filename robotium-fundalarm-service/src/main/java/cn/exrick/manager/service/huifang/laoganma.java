package cn.exrick.manager.service.huifang;

import java.io.IOException;

import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class laoganma {

//	https://www2.zimuquan25.uk/index.php/vod/show/id/1/page/1.html
	static ThreadSafeFileWriter writer = null;

	public static void main(String[] args) {
		try {
			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/history.txt");

//			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self.txt");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		java.io.File file = new java.io.File("d:/download/result.json");
		String jsonString = FileUtil.readString(file, "UTF-8");
		JSONObject jObject = new JSONObject(jsonString);
		JSONArray jArray = jObject.getJSONArray("messages");
		for (int i = 0; i < jArray.size(); i++) {
			JSONObject mess = jArray.getJSONObject(i);
			if (mess.containsKey("file_name")) {

				String dtString = mess.getStr("date");
				System.out.println(mess);
				String title = mess.getStr("file_name");
				if (mess.getJSONArray("text_entities").size() > 0)
					title = mess.getJSONArray("text_entities").getJSONObject(0).getStr("text") + "_"
							+ mess.getStr("file_name");

				String vid = mess.getStr("id");
				String rid = mess.getStr("reply_to_message_id");
				String durationString = mess.getStr("duration_seconds");

				// String urlString = "https://t.me/c/3173512543/" + rid + "/" + vid;

				String content = dtString + "\t" + vid + "\t" + title + "\t" + rid + "\t" + "laoganma" + "\t"
						+ durationString;
				writer.write(content);
			}

		}

	}

}
