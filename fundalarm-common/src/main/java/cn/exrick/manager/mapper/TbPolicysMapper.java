package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbPolicys;
import cn.exrick.manager.pojo.TbPolicysExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TbPolicysMapper {
    long countByExample(TbPolicysExample example);

    int deleteByExample(TbPolicysExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbPolicys record);

    int insertSelective(TbPolicys record);

    List<TbPolicys> selectByExample(TbPolicysExample example);

    TbPolicys selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbPolicys record, @Param("example") TbPolicysExample example);

    int updateByExample(@Param("record") TbPolicys record, @Param("example") TbPolicysExample example);

    int updateByPrimaryKeySelective(TbPolicys record);

    int updateByPrimaryKey(TbPolicys record);

	List<Map> selectbycustomerinfo(Map<String, String> condition);
}