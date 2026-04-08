package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.FundFangdichan1Ok;
import cn.exrick.manager.pojo.FundFangdichan1OkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FundFangdichan1OkMapper {
    long countByExample(FundFangdichan1OkExample example);

    int deleteByExample(FundFangdichan1OkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FundFangdichan1Ok record);

    int insertSelective(FundFangdichan1Ok record);

    List<FundFangdichan1Ok> selectByExample(FundFangdichan1OkExample example);

    FundFangdichan1Ok selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FundFangdichan1Ok record, @Param("example") FundFangdichan1OkExample example);

    int updateByExample(@Param("record") FundFangdichan1Ok record, @Param("example") FundFangdichan1OkExample example);

    int updateByPrimaryKeySelective(FundFangdichan1Ok record);

    int updateByPrimaryKey(FundFangdichan1Ok record);
}