package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.StockData;
import cn.exrick.manager.pojo.StockDataExample;
import cn.exrick.manager.pojo.StockDataWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockDataMapper {
    long countByExample(StockDataExample example);

    int deleteByExample(StockDataExample example);

    int deleteByPrimaryKey(String stockid);

    int insert(StockDataWithBLOBs record);

    int insertSelective(StockDataWithBLOBs record);

    List<StockDataWithBLOBs> selectByExampleWithBLOBs(StockDataExample example);

    List<StockData> selectByExample(StockDataExample example);

    StockDataWithBLOBs selectByPrimaryKey(String stockid);

    int updateByExampleSelective(@Param("record") StockDataWithBLOBs record, @Param("example") StockDataExample example);

    int updateByExampleWithBLOBs(@Param("record") StockDataWithBLOBs record, @Param("example") StockDataExample example);

    int updateByExample(@Param("record") StockData record, @Param("example") StockDataExample example);

    int updateByPrimaryKeySelective(StockDataWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(StockDataWithBLOBs record);

    int updateByPrimaryKey(StockData record);
}