package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbOrganBankCard;
import cn.exrick.manager.pojo.TbOrganBankCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbOrganBankCardMapper {
    long countByExample(TbOrganBankCardExample example);

    int deleteByExample(TbOrganBankCardExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrganBankCard record);

    int insertSelective(TbOrganBankCard record);

    List<TbOrganBankCard> selectByExample(TbOrganBankCardExample example);

    TbOrganBankCard selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrganBankCard record, @Param("example") TbOrganBankCardExample example);

    int updateByExample(@Param("record") TbOrganBankCard record, @Param("example") TbOrganBankCardExample example);

    int updateByPrimaryKeySelective(TbOrganBankCard record);

    int updateByPrimaryKey(TbOrganBankCard record);
}