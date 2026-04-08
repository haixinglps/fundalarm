package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.WanwuVideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WanwuVideoMapper {
    long countByExample(WanwuVideoExample example);

    int deleteByExample(WanwuVideoExample example);

    int deleteByPrimaryKey(Integer vid);

    int insert(WanwuVideo record);

    int insertSelective(WanwuVideo record);

    List<WanwuVideo> selectByExample(WanwuVideoExample example);

    WanwuVideo selectByPrimaryKey(Integer vid);

    int updateByExampleSelective(@Param("record") WanwuVideo record, @Param("example") WanwuVideoExample example);

    int updateByExample(@Param("record") WanwuVideo record, @Param("example") WanwuVideoExample example);

    int updateByPrimaryKeySelective(WanwuVideo record);

    int updateByPrimaryKey(WanwuVideo record);
}