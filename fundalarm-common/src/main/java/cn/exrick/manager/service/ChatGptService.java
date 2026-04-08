package cn.exrick.manager.service;

import com.github.pagehelper.PageInfo;

import cn.exrick.manager.dto.Chatgpt;
import cn.exrick.manager.dto.front.CommentGptLog;
import cn.exrick.manager.pojo.TbChatgpt;
import cn.exrick.manager.pojo.TbDycomment;

/**
 * @author Exrickx
 */
public interface ChatGptService {

	/**
	 * 通过用户名获取用户
	 * 
	 * @param username
	 * @return
	 */
	String chat(String message);

	/**
	 * 通过用户名获取角色
	 * 
	 * @param username
	 * @return
	 */

	String dycomment(String info);

	PageInfo<CommentGptLog> getCommentLog(int page, int size, String uid);

	void saveCommentLog(TbDycomment comment);

	String solution(String info);

	void saveTask(TbChatgpt chatgpt);

	PageInfo<Chatgpt> getTasks(int page, int size, String uid);

}
