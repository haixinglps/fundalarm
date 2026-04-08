package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupscategory;
import cn.exrick.manager.pojo.TbGroupscategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupscategoryMapper {
    long countByExample(TbGroupscategoryExample example);

    int deleteByExample(TbGroupscategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupscategory record);

    int insertSelective(TbGroupscategory record);

    List<TbGroupscategory> selectByExample(TbGroupscategoryExample example);

    TbGroupscategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupscategory record, @Param("example") TbGroupscategoryExample example);

    int updateByExample(@Param("record") TbGroupscategory record, @Param("example") TbGroupscategoryExample example);

    int updateByPrimaryKeySelective(TbGroupscategory record);

    int updateByPrimaryKey(TbGroupscategory record);
}