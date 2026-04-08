package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbWalletTicheng;
import cn.exrick.manager.pojo.TbWalletTichengExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbWalletTichengMapper {
    long countByExample(TbWalletTichengExample example);

    int deleteByExample(TbWalletTichengExample example);

    int deleteByPrimaryKey(Long organId);

    int insert(TbWalletTicheng record);

    int insertSelective(TbWalletTicheng record);

    List<TbWalletTicheng> selectByExample(TbWalletTichengExample example);

    TbWalletTicheng selectByPrimaryKey(Long organId);

    int updateByExampleSelective(@Param("record") TbWalletTicheng record, @Param("example") TbWalletTichengExample example);

    int updateByExample(@Param("record") TbWalletTicheng record, @Param("example") TbWalletTichengExample example);

    int updateByPrimaryKeySelective(TbWalletTicheng record);

    int updateByPrimaryKey(TbWalletTicheng record);
    int updateticheng(TbWalletTicheng record);
}