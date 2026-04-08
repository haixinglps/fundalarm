package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupsforpolicy;
import cn.exrick.manager.pojo.TbGroupsforpolicyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupsforpolicyMapper {
    long countByExample(TbGroupsforpolicyExample example);

    int deleteByExample(TbGroupsforpolicyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupsforpolicy record);

    int insertSelective(TbGroupsforpolicy record);

    List<TbGroupsforpolicy> selectByExample(TbGroupsforpolicyExample example);

    TbGroupsforpolicy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupsforpolicy record, @Param("example") TbGroupsforpolicyExample example);

    int updateByExample(@Param("record") TbGroupsforpolicy record, @Param("example") TbGroupsforpolicyExample example);

    int updateByPrimaryKeySelective(TbGroupsforpolicy record);

    int updateByPrimaryKey(TbGroupsforpolicy record);
}