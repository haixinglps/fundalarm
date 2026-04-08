package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbNews;
import cn.exrick.manager.pojo.TbNewsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbNewsMapper {
    long countByExample(TbNewsExample example);

    int deleteByExample(TbNewsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbNews record);

    int insertSelective(TbNews record);

    List<TbNews> selectByExampleWithBLOBs(TbNewsExample example);

    List<TbNews> selectByExample(TbNewsExample example);

    TbNews selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbNews record, @Param("example") TbNewsExample example);

    int updateByExampleWithBLOBs(@Param("record") TbNews record, @Param("example") TbNewsExample example);

    int updateByExample(@Param("record") TbNews record, @Param("example") TbNewsExample example);

    int updateByPrimaryKeySelective(TbNews record);

    int updateByPrimaryKeyWithBLOBs(TbNews record);

    int updateByPrimaryKey(TbNews record);
}