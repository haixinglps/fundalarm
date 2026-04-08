package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.StockDataHistory;
import cn.exrick.manager.pojo.StockDataHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockDataHistoryMapper {
    long countByExample(StockDataHistoryExample example);

    int deleteByExample(StockDataHistoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StockDataHistory record);

    int insertSelective(StockDataHistory record);

    List<StockDataHistory> selectByExample(StockDataHistoryExample example);

    StockDataHistory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StockDataHistory record, @Param("example") StockDataHistoryExample example);

    int updateByExample(@Param("record") StockDataHistory record, @Param("example") StockDataHistoryExample example);

    int updateByPrimaryKeySelective(StockDataHistory record);

    int updateByPrimaryKey(StockDataHistory record);
}