package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbRechargeManage;
import cn.exrick.manager.pojo.TbRechargeManageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbRechargeManageMapper {
    long countByExample(TbRechargeManageExample example);

    int deleteByExample(TbRechargeManageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbRechargeManage record);

    int insertSelective(TbRechargeManage record);

    List<TbRechargeManage> selectByExample(TbRechargeManageExample example);

    TbRechargeManage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbRechargeManage record, @Param("example") TbRechargeManageExample example);

    int updateByExample(@Param("record") TbRechargeManage record, @Param("example") TbRechargeManageExample example);

    int updateByPrimaryKeySelective(TbRechargeManage record);

    int updateByPrimaryKey(TbRechargeManage record);
}