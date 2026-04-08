package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbCommandsDesc;
import cn.exrick.manager.pojo.TbCommandsDescExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbCommandsDescMapper {
    long countByExample(TbCommandsDescExample example);

    int deleteByExample(TbCommandsDescExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbCommandsDesc record);

    int insertSelective(TbCommandsDesc record);

    List<TbCommandsDesc> selectByExample(TbCommandsDescExample example);

    TbCommandsDesc selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbCommandsDesc record, @Param("example") TbCommandsDescExample example);

    int updateByExample(@Param("record") TbCommandsDesc record, @Param("example") TbCommandsDescExample example);

    int updateByPrimaryKeySelective(TbCommandsDesc record);

    int updateByPrimaryKey(TbCommandsDesc record);
}