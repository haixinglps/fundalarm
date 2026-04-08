package cn.exrick.manager.service;

import cn.exrick.manager.pojo.TbDouyinGz;
import cn.exrick.manager.pojo.TbDouyinMessage;

/**
 * @author Exrickx
 */
public interface RpaService {

	void saveGuanzhu(TbDouyinGz gz);

	void saveSixin(TbDouyinMessage message);

}
