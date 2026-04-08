package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbSummery;
import cn.exrick.manager.pojo.TbSummeryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbSummeryMapper {
    long countByExample(TbSummeryExample example);

    int deleteByExample(TbSummeryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbSummery record);

    int insertSelective(TbSummery record);

    List<TbSummery> selectByExample(TbSummeryExample example);

    TbSummery selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbSummery record, @Param("example") TbSummeryExample example);

    int updateByExample(@Param("record") TbSummery record, @Param("example") TbSummeryExample example);

    int updateByPrimaryKeySelective(TbSummery record);

    int updateByPrimaryKey(TbSummery record);
}