package cn.exrick.manager.isearch.query.bean.compare;

import java.util.Comparator;

import cn.exrick.manager.isearch.query.bean.CubeBean;


/**
 * 检索结果比较
 * 
 * @author guild
 * 
 */
public class ComparatorCubeByValue<T extends CubeBean> implements Comparator<T> {

	public int compare(T bean1, T bean2) {
		return (bean1.getNum() > bean2.getNum()) ? -1 : 1;
	}

}
