package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbOrdercombine;
import cn.exrick.manager.pojo.TbOrdercombineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbOrdercombineMapper {
    long countByExample(TbOrdercombineExample example);

    int deleteByExample(TbOrdercombineExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrdercombine record);

    int insertSelective(TbOrdercombine record);

    List<TbOrdercombine> selectByExample(TbOrdercombineExample example);

    TbOrdercombine selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrdercombine record, @Param("example") TbOrdercombineExample example);

    int updateByExample(@Param("record") TbOrdercombine record, @Param("example") TbOrdercombineExample example);

    int updateByPrimaryKeySelective(TbOrdercombine record);

    int updateByPrimaryKey(TbOrdercombine record);
}