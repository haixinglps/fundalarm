package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbSubjectalarm;
import cn.exrick.manager.pojo.TbSubjectalarmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbSubjectalarmMapper {
    long countByExample(TbSubjectalarmExample example);

    int deleteByExample(TbSubjectalarmExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbSubjectalarm record);

    int insertSelective(TbSubjectalarm record);

    List<TbSubjectalarm> selectByExample(TbSubjectalarmExample example);

    TbSubjectalarm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbSubjectalarm record, @Param("example") TbSubjectalarmExample example);

    int updateByExample(@Param("record") TbSubjectalarm record, @Param("example") TbSubjectalarmExample example);

    int updateByPrimaryKeySelective(TbSubjectalarm record);

    int updateByPrimaryKey(TbSubjectalarm record);
}