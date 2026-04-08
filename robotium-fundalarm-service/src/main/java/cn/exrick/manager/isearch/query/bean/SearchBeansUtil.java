package cn.exrick.manager.isearch.query.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cn.exrick.manager.isearch.query.Search;


/**
 * 检索结果工具
 * 
 * @author guild
 * 
 */
public class SearchBeansUtil {
	
	public static SearchBeans removeRepeatUrl(SearchBeans beans) {
		SearchBeans newBeans = new SearchBeans();
		Set<String> urls = new HashSet<String>();
		for (SearchBean bean : beans.list()) {
			if (urls.add(bean.getUr()))
				newBeans.add(bean);
		}
		return newBeans;
	}

	// 去掉重复的数据
	public static SearchBeans removeRepeat(SearchBeans beans) {
		SearchBeans newBeans = new SearchBeans();
		Set<String> titles = new HashSet<String>();
		Set<String> urls = new HashSet<String>();
		for (int i = 0; i < beans.size(); i++) {
			SearchBean bean = beans.get(i);
			// 无重复数据直接添加
			if (titles.add(bean.getTi()) && urls.add(bean.getUr())) {
				newBeans.add(bean);
				continue;
			}
			// 有重复数据添加到重复数据中
			for (int j = 0; j < newBeans.size(); j++) {
				SearchBean newBean = newBeans.get(j);
				if (newBean.getTi().equals(bean.getTi()) || newBean.getUr().equals(bean.getUr())) {
					SearchBeans subBeans = newBean.getSubBeans();
					subBeans.add(bean);
					newBean.setSubBeans(subBeans);
					break;
				}
			}
		}
		return newBeans;
	}

	
	
	
	public static SearchBeans removeRepeatdomain(SearchBeans beans) {
		SearchBeans newBeans = new SearchBeans();
		Set<String> titles = new HashSet<String>();
		Set<String> urls = new HashSet<String>();
		for (int i = 0; i < beans.size(); i++) {
			SearchBean bean = beans.get(i);
			// 无重复数据直接添加
			if (titles.add(bean.getTi()) && urls.add(bean.getUr())&& urls.add(bean.getDm())) {
				newBeans.add(bean);
				continue;
			}
			// 有重复数据添加到重复数据中
			for (int j = 0; j < newBeans.size(); j++) {
				SearchBean newBean = newBeans.get(j);
				if (newBean.getTi().equals(bean.getTi()) || newBean.getUr().equals(bean.getUr())||newBean.getDm().equals(bean.getDm())) {
					SearchBeans subBeans = newBean.getSubBeans();
					subBeans.add(bean);
					newBean.setSubBeans(subBeans);
					break;
				}
			}
		}
		return newBeans;
	}
	
	
	
	
	
	
	
	/**
	 * 排序
	 */
	public static SearchBeans sortDesc(SearchBeans beans) {
		for (int i = 0; i < beans.size(); i++) {
			for (int j = 0; j < beans.size(); j++) {
				SearchBean bean1 = beans.get(i);
				SearchBean bean2 = beans.get(j);
				try {
					if (bean1.getRq() > bean2.getRq()) {
						beans.remove(j);
						beans.add(j, bean1);
						beans.remove(i);
						beans.add(i, bean2);
					}
				} catch (Exception e) {
					
				}
			}
		}
		return beans;
	}
	
	/**
	 * 排序
	 */
	public static SearchBeans sort(SearchBeans beans) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Search.DATE_FORMAT);
		for (int i = 0; i < beans.size(); i++) {
			for (int j = 0; j < beans.size(); j++) {
				SearchBean bean1 = beans.get(i);
				SearchBean bean2 = beans.get(j);
				try {
					Date date1 = dateFormat.parse(bean1.getPubdate());
					Date date2 = dateFormat.parse(bean2.getPubdate());
					if (date1.before(date2)) {
						beans.remove(j);
						beans.add(j, bean1);
						beans.remove(i);
						beans.add(i, bean2);
					}
				} catch (Exception e) {

				}
			}
		}
		return beans;
	}

}
