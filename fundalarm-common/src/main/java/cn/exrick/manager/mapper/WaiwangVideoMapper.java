package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WaiwangVideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WaiwangVideoMapper {
    long countByExample(WaiwangVideoExample example);

    int deleteByExample(WaiwangVideoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WaiwangVideo record);

    int insertSelective(WaiwangVideo record);

    List<WaiwangVideo> selectByExample(WaiwangVideoExample example);

    WaiwangVideo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WaiwangVideo record, @Param("example") WaiwangVideoExample example);

    int updateByExample(@Param("record") WaiwangVideo record, @Param("example") WaiwangVideoExample example);

    int updateByPrimaryKeySelective(WaiwangVideo record);

    int updateByPrimaryKey(WaiwangVideo record);
}