package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbWalletMapper {
    long countByExample(TbWalletExample example);

    int deleteByExample(TbWalletExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbWallet record);

    int insertSelective(TbWallet record);

    List<TbWallet> selectByExample(TbWalletExample example);

    TbWallet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbWallet record, @Param("example") TbWalletExample example);

    int updateByExample(@Param("record") TbWallet record, @Param("example") TbWalletExample example);

    int updateByPrimaryKeySelective(TbWallet record);

    int updateByPrimaryKey(TbWallet record);
}