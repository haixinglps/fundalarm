package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupofhaveadd;
import cn.exrick.manager.pojo.TbGroupofhaveaddExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupofhaveaddMapper {
    long countByExample(TbGroupofhaveaddExample example);

    int deleteByExample(TbGroupofhaveaddExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupofhaveadd record);

    int insertSelective(TbGroupofhaveadd record);

    List<TbGroupofhaveadd> selectByExample(TbGroupofhaveaddExample example);

    TbGroupofhaveadd selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupofhaveadd record, @Param("example") TbGroupofhaveaddExample example);

    int updateByExample(@Param("record") TbGroupofhaveadd record, @Param("example") TbGroupofhaveaddExample example);

    int updateByPrimaryKeySelective(TbGroupofhaveadd record);

    int updateByPrimaryKey(TbGroupofhaveadd record);
}