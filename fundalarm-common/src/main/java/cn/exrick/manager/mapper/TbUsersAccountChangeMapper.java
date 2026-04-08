package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbUsersAccountChange;
import cn.exrick.manager.pojo.TbUsersAccountChangeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbUsersAccountChangeMapper {
    long countByExample(TbUsersAccountChangeExample example);

    int deleteByExample(TbUsersAccountChangeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUsersAccountChange record);

    int insertSelective(TbUsersAccountChange record);

    List<TbUsersAccountChange> selectByExample(TbUsersAccountChangeExample example);

    TbUsersAccountChange selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUsersAccountChange record, @Param("example") TbUsersAccountChangeExample example);

    int updateByExample(@Param("record") TbUsersAccountChange record, @Param("example") TbUsersAccountChangeExample example);

    int updateByPrimaryKeySelective(TbUsersAccountChange record);

    int updateByPrimaryKey(TbUsersAccountChange record);
}