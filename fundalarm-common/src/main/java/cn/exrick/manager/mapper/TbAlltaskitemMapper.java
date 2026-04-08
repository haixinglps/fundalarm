package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbAlltaskitem;
import cn.exrick.manager.pojo.TbAlltaskitemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAlltaskitemMapper {
    long countByExample(TbAlltaskitemExample example);

    int deleteByExample(TbAlltaskitemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbAlltaskitem record);

    int insertSelective(TbAlltaskitem record);

    List<TbAlltaskitem> selectByExample(TbAlltaskitemExample example);

    TbAlltaskitem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbAlltaskitem record, @Param("example") TbAlltaskitemExample example);

    int updateByExample(@Param("record") TbAlltaskitem record, @Param("example") TbAlltaskitemExample example);

    int updateByPrimaryKeySelective(TbAlltaskitem record);

    int updateByPrimaryKey(TbAlltaskitem record);
}