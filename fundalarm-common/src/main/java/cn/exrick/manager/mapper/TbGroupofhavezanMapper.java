package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupofhavezan;
import cn.exrick.manager.pojo.TbGroupofhavezanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupofhavezanMapper {
    long countByExample(TbGroupofhavezanExample example);

    int deleteByExample(TbGroupofhavezanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupofhavezan record);

    int insertSelective(TbGroupofhavezan record);

    List<TbGroupofhavezan> selectByExample(TbGroupofhavezanExample example);

    TbGroupofhavezan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupofhavezan record, @Param("example") TbGroupofhavezanExample example);

    int updateByExample(@Param("record") TbGroupofhavezan record, @Param("example") TbGroupofhavezanExample example);

    int updateByPrimaryKeySelective(TbGroupofhavezan record);

    int updateByPrimaryKey(TbGroupofhavezan record);
}