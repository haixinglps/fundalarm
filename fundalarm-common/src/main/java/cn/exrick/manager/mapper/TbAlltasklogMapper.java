package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbAlltasklog;
import cn.exrick.manager.pojo.TbAlltasklogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAlltasklogMapper {
    long countByExample(TbAlltasklogExample example);

    int deleteByExample(TbAlltasklogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbAlltasklog record);

    int insertSelective(TbAlltasklog record);

    List<TbAlltasklog> selectByExample(TbAlltasklogExample example);

    TbAlltasklog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbAlltasklog record, @Param("example") TbAlltasklogExample example);

    int updateByExample(@Param("record") TbAlltasklog record, @Param("example") TbAlltasklogExample example);

    int updateByPrimaryKeySelective(TbAlltasklog record);

    int updateByPrimaryKey(TbAlltasklog record);
}