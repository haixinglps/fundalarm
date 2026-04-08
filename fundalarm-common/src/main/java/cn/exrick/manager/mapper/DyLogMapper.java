package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.DyLog;
import cn.exrick.manager.pojo.DyLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DyLogMapper {
    long countByExample(DyLogExample example);

    int deleteByExample(DyLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DyLog record);

    int insertSelective(DyLog record);

    List<DyLog> selectByExample(DyLogExample example);

    DyLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DyLog record, @Param("example") DyLogExample example);

    int updateByExample(@Param("record") DyLog record, @Param("example") DyLogExample example);

    int updateByPrimaryKeySelective(DyLog record);

    int updateByPrimaryKey(DyLog record);
}