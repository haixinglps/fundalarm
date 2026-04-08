package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupscount;
import cn.exrick.manager.pojo.TbGroupscountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupscountMapper {
    long countByExample(TbGroupscountExample example);

    int deleteByExample(TbGroupscountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupscount record);

    int insertSelective(TbGroupscount record);

    List<TbGroupscount> selectByExample(TbGroupscountExample example);

    TbGroupscount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupscount record, @Param("example") TbGroupscountExample example);

    int updateByExample(@Param("record") TbGroupscount record, @Param("example") TbGroupscountExample example);

    int updateByPrimaryKeySelective(TbGroupscount record);

    int updateByPrimaryKey(TbGroupscount record);
}