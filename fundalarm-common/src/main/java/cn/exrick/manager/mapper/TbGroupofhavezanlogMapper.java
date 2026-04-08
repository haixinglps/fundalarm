package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupofhavezanlog;
import cn.exrick.manager.pojo.TbGroupofhavezanlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupofhavezanlogMapper {
    long countByExample(TbGroupofhavezanlogExample example);

    int deleteByExample(TbGroupofhavezanlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupofhavezanlog record);

    int insertSelective(TbGroupofhavezanlog record);

    List<TbGroupofhavezanlog> selectByExample(TbGroupofhavezanlogExample example);

    TbGroupofhavezanlog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupofhavezanlog record, @Param("example") TbGroupofhavezanlogExample example);

    int updateByExample(@Param("record") TbGroupofhavezanlog record, @Param("example") TbGroupofhavezanlogExample example);

    int updateByPrimaryKeySelective(TbGroupofhavezanlog record);

    int updateByPrimaryKey(TbGroupofhavezanlog record);
}