package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbPolicysmember;
import cn.exrick.manager.pojo.TbPolicysmemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbPolicysmemberMapper {
    long countByExample(TbPolicysmemberExample example);

    int deleteByExample(TbPolicysmemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbPolicysmember record);

    int insertSelective(TbPolicysmember record);

    List<TbPolicysmember> selectByExample(TbPolicysmemberExample example);

    TbPolicysmember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbPolicysmember record, @Param("example") TbPolicysmemberExample example);

    int updateByExample(@Param("record") TbPolicysmember record, @Param("example") TbPolicysmemberExample example);

    int updateByPrimaryKeySelective(TbPolicysmember record);

    int updateByPrimaryKey(TbPolicysmember record);
}