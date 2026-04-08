package cn.exrick.manager.service;

import java.util.List;

import cn.exrick.manager.pojo.TbShiroFilter;

/**
 * @author Exrickx
 */
public interface SystempolicyRobotService {

	/**
	 * 获得shiro过滤链配置
	 * 
	 * @return
	 */
	List<TbShiroFilter> getShiroFilter();

	/**
	 * 统计过滤链数目
	 * 
	 * @return
	 */
	Long countShiroFilter();

	/**
	 * 添加shiro过滤链
	 * 
	 * @param tbShiroFilter
	 * @return
	 */
	int addShiroFilter(TbShiroFilter tbShiroFilter);

	/**
	 * 更新shiro过滤链
	 * 
	 * @param tbShiroFilter
	 * @return
	 */
	int updateShiroFilter(TbShiroFilter tbShiroFilter);

	/**
	 * 删除shiro过滤链
	 * 
	 * @param id
	 * @return
	 */
	int deleteShiroFilter(int id);

}
