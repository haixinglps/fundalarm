package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupofhaveaddlog;
import cn.exrick.manager.pojo.TbGroupofhaveaddlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupofhaveaddlogMapper {
    long countByExample(TbGroupofhaveaddlogExample example);

    int deleteByExample(TbGroupofhaveaddlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupofhaveaddlog record);

    int insertSelective(TbGroupofhaveaddlog record);

    List<TbGroupofhaveaddlog> selectByExample(TbGroupofhaveaddlogExample example);

    TbGroupofhaveaddlog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupofhaveaddlog record, @Param("example") TbGroupofhaveaddlogExample example);

    int updateByExample(@Param("record") TbGroupofhaveaddlog record, @Param("example") TbGroupofhaveaddlogExample example);

    int updateByPrimaryKeySelective(TbGroupofhaveaddlog record);

    int updateByPrimaryKey(TbGroupofhaveaddlog record);
}