package cn.exrick.manager.service;

import java.util.List;

import cn.exrick.manager.pojo.Category;
import cn.exrick.manager.pojo.StockDataHistory;
import cn.exrick.manager.pojo.StockDataWithBLOBs;

public interface StockService {

	Category getCategory(String subjectId);

	StockDataWithBLOBs getStockData(String stockid);

	List<StockDataHistory> getHistory(int counts, String stockid);

}
