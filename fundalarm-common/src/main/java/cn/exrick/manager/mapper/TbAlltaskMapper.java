package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbAlltask;
import cn.exrick.manager.pojo.TbAlltaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAlltaskMapper {
    long countByExample(TbAlltaskExample example);

    int deleteByExample(TbAlltaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbAlltask record);

    int insertSelective(TbAlltask record);

    List<TbAlltask> selectByExample(TbAlltaskExample example);

    TbAlltask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbAlltask record, @Param("example") TbAlltaskExample example);

    int updateByExample(@Param("record") TbAlltask record, @Param("example") TbAlltaskExample example);

    int updateByPrimaryKeySelective(TbAlltask record);

    int updateByPrimaryKey(TbAlltask record);
}