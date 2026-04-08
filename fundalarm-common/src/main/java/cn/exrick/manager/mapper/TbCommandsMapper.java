package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbCommands;
import cn.exrick.manager.pojo.TbCommandsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbCommandsMapper {
    long countByExample(TbCommandsExample example);

    int deleteByExample(TbCommandsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbCommands record);

    int insertSelective(TbCommands record);

    List<TbCommands> selectByExample(TbCommandsExample example);

    TbCommands selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbCommands record, @Param("example") TbCommandsExample example);

    int updateByExample(@Param("record") TbCommands record, @Param("example") TbCommandsExample example);

    int updateByPrimaryKeySelective(TbCommands record);

    int updateByPrimaryKey(TbCommands record);
}