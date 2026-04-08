package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbAllmember;
import cn.exrick.manager.pojo.TbAllmemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAllmemberMapper {
    long countByExample(TbAllmemberExample example);

    int deleteByExample(TbAllmemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbAllmember record);

    int insertSelective(TbAllmember record);

    List<TbAllmember> selectByExample(TbAllmemberExample example);

    TbAllmember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbAllmember record, @Param("example") TbAllmemberExample example);

    int updateByExample(@Param("record") TbAllmember record, @Param("example") TbAllmemberExample example);

    int updateByPrimaryKeySelective(TbAllmember record);

    int updateByPrimaryKey(TbAllmember record);
}