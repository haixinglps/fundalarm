package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbItemrelationpolicy;
import cn.exrick.manager.pojo.TbItemrelationpolicyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbItemrelationpolicyMapper {
    long countByExample(TbItemrelationpolicyExample example);

    int deleteByExample(TbItemrelationpolicyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbItemrelationpolicy record);

    int insertSelective(TbItemrelationpolicy record);

    List<TbItemrelationpolicy> selectByExample(TbItemrelationpolicyExample example);

    TbItemrelationpolicy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbItemrelationpolicy record, @Param("example") TbItemrelationpolicyExample example);

    int updateByExample(@Param("record") TbItemrelationpolicy record, @Param("example") TbItemrelationpolicyExample example);

    int updateByPrimaryKeySelective(TbItemrelationpolicy record);

    int updateByPrimaryKey(TbItemrelationpolicy record);
}