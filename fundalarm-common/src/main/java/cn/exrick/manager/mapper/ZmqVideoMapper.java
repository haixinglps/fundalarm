package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.pojo.ZmqVideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ZmqVideoMapper {
    long countByExample(ZmqVideoExample example);

    int deleteByExample(ZmqVideoExample example);

    int deleteByPrimaryKey(Integer vid);

    int insert(ZmqVideo record);

    int insertSelective(ZmqVideo record);

    List<ZmqVideo> selectByExample(ZmqVideoExample example);

    ZmqVideo selectByPrimaryKey(Integer vid);

    int updateByExampleSelective(@Param("record") ZmqVideo record, @Param("example") ZmqVideoExample example);

    int updateByExample(@Param("record") ZmqVideo record, @Param("example") ZmqVideoExample example);

    int updateByPrimaryKeySelective(ZmqVideo record);

    int updateByPrimaryKey(ZmqVideo record);
}