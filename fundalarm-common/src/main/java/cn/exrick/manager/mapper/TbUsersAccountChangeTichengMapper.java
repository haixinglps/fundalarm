package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbUsersAccountChangeTicheng;
import cn.exrick.manager.pojo.TbUsersAccountChangeTichengExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbUsersAccountChangeTichengMapper {
    long countByExample(TbUsersAccountChangeTichengExample example);

    int deleteByExample(TbUsersAccountChangeTichengExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUsersAccountChangeTicheng record);

    int insertSelective(TbUsersAccountChangeTicheng record);

    List<TbUsersAccountChangeTicheng> selectByExample(TbUsersAccountChangeTichengExample example);

    TbUsersAccountChangeTicheng selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUsersAccountChangeTicheng record, @Param("example") TbUsersAccountChangeTichengExample example);

    int updateByExample(@Param("record") TbUsersAccountChangeTicheng record, @Param("example") TbUsersAccountChangeTichengExample example);

    int updateByPrimaryKeySelective(TbUsersAccountChangeTicheng record);

    int updateByPrimaryKey(TbUsersAccountChangeTicheng record);
}