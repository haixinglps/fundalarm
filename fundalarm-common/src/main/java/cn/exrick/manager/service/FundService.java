package cn.exrick.manager.service;

import java.math.BigDecimal;
import java.util.List;

import cn.exrick.manager.pojo.Fund;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;

/**
 * @author Exrickx
 */
public interface FundService {

	Fund1Gaoduanzhuangbei2Ok getInfoByTableName(String tableName);

//	void updateCurrentPrice(String tableName, BigDecimal price);

//	void updateCurrentPrice(String tableName, BigDecimal price, int tag, BigDecimal realbuyprice, BigDecimal money,
//			String lineName);

	List<Fund> getIndex();

//	void updateIndex(String tableName);

//	void updateIndex(String tableName, int tag);
//
//	void updateIndex(String tableName, int tag, BigDecimal zzl);

	void updateIndex(String tableName, int tag, BigDecimal zzl, String comment);

	void updateFund(Fund fund);

	List<Fund> getIndexBtc();

	List<Fund1Gaoduanzhuangbei2Ok> getCangweisByTableName(String tableName);

//	void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName);

	void updateCurrentPrice(String tableName, BigDecimal price, int tag, BigDecimal realbuyprice, BigDecimal money,
			Fund fundindex, Fund1Gaoduanzhuangbei2Ok fundItem, String lastvalue);

	Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndLevel(String tableName, int level);

//	void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName, List<Fund1Gaoduanzhuangbei2Ok> duichongList);

//	Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndDt(String tableName, long dt);

	Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndDt(String tableName, long dt, int level);

	void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName, List<Fund1Gaoduanzhuangbei2Ok> duichongList,
			Fund fundindex);

//	void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName, Fund1Gaoduanzhuangbei2Ok cwLast,
//			Fund1Gaoduanzhuangbei2Ok cwFirst);

}
