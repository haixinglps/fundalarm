package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroups;
import cn.exrick.manager.pojo.TbGroupsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupsMapper {
    long countByExample(TbGroupsExample example);

    int deleteByExample(TbGroupsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroups record);

    int insertSelective(TbGroups record);

    List<TbGroups> selectByExampleWithBLOBs(TbGroupsExample example);

    List<TbGroups> selectByExample(TbGroupsExample example);

    TbGroups selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroups record, @Param("example") TbGroupsExample example);

    int updateByExampleWithBLOBs(@Param("record") TbGroups record, @Param("example") TbGroupsExample example);

    int updateByExample(@Param("record") TbGroups record, @Param("example") TbGroupsExample example);

    int updateByPrimaryKeySelective(TbGroups record);

    int updateByPrimaryKeyWithBLOBs(TbGroups record);

    int updateByPrimaryKey(TbGroups record);
}