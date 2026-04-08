package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbDestination;
import cn.exrick.manager.pojo.TbDestinationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDestinationMapper {
    long countByExample(TbDestinationExample example);

    int deleteByExample(TbDestinationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbDestination record);

    int insertSelective(TbDestination record);

    List<TbDestination> selectByExample(TbDestinationExample example);

    TbDestination selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbDestination record, @Param("example") TbDestinationExample example);

    int updateByExample(@Param("record") TbDestination record, @Param("example") TbDestinationExample example);

    int updateByPrimaryKeySelective(TbDestination record);

    int updateByPrimaryKey(TbDestination record);
}