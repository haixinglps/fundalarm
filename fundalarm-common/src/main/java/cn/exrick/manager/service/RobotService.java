package cn.exrick.manager.service;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import cn.exrick.manager.dto.SearchResultDTO;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.Wanwu;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.ZmqVideo;

/**
 * @author Exrickx
 */
public interface RobotService {

	/**
	 * 通过用户名获取用户
	 * 
	 * @param username
	 * @return
	 */

	void dealSearch(Update update);

	void dealSearchGroup(Update update);

	void dealGetWork(Update update);

	void updateDuration(String vid, String duration);

	void addVideo(Waiwang2Video video);

	Waiwang2Video getVideo(String vid);

	void updateUrl(String vid, String url);

	void updateUrl(String vid, String url, String author);

	Waiwang2Video getValidRecord(String author);

	String getAllWork(Update update);

	void updateLocation(String vid, String location);

	List<Waiwang2Video> getAllVideo(int page, int size);

	void updateLocationtl(String vid, String location);

	Taolu3Video getVideotl(String vid);

	List<WanwuVideo> getAllVideo2(int page, int size);

	WanwuVideo getVideoWw(String vid);

	void updateLocationww(String vid, String location);

	Waiwang2Video getAuthorFirst(String authorId);

	void addTiteDate(Wanwu wanwu);

	WaiwangVideo getVideoTG(String vid);

	ZmqVideo getVideoZmq(String vid);

	void updateLocationTG(String vid, String location);

	void updateLocationzmq(String vid, String location);

	Waiwang2Video getVideoWW2(String vid);

	void updateTitle(Waiwang2Video video);
	
	// ==================== 批量查询方法（供 QQ Bot 使用） ====================
	
	/**
	 * 搜索所有库
	 * @param keyword 搜索关键词
	 * @param page 页码（从1开始）
	 * @param pageSize 每页大小
	 * @return 搜索结果
	 */
	SearchResultDTO searchAll(String keyword, int page, int pageSize);
	
	/**
	 * 搜索网页库 (zmq)
	 * @param keyword 关键词
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<ZmqVideo> searchZmq(String keyword, int page, int pageSize);
	
	/**
	 * 搜索最新作品 (waiwang2)
	 * @param keyword 关键词
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<Waiwang2Video> searchWaiwang2(String keyword, int page, int pageSize);
	
	/**
	 * 搜索玩物 (wanwu)
	 * @param keyword 关键词
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<WanwuVideo> searchWanwu(String keyword, int page, int pageSize);
	
	/**
	 * 搜索淘露 (taolu3)
	 * @param keyword 关键词
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<Taolu3Video> searchTaolu3(String keyword, int page, int pageSize);
	
	/**
	 * 搜索电报 (waiwang)
	 * @param keyword 关键词
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<WaiwangVideo> searchWaiwang(String keyword, int page, int pageSize);
	
	/**
	 * 获取最新作品（全库）
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @return 结果列表
	 */
	List<Waiwang2Video> getLatestWorks(int page, int pageSize);

}
