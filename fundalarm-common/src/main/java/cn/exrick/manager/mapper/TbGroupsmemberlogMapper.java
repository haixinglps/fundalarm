package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbGroupsmemberlog;
import cn.exrick.manager.pojo.TbGroupsmemberlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbGroupsmemberlogMapper {
    long countByExample(TbGroupsmemberlogExample example);

    int deleteByExample(TbGroupsmemberlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbGroupsmemberlog record);

    int insertSelective(TbGroupsmemberlog record);

    List<TbGroupsmemberlog> selectByExample(TbGroupsmemberlogExample example);

    TbGroupsmemberlog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbGroupsmemberlog record, @Param("example") TbGroupsmemberlogExample example);

    int updateByExample(@Param("record") TbGroupsmemberlog record, @Param("example") TbGroupsmemberlogExample example);

    int updateByPrimaryKeySelective(TbGroupsmemberlog record);

    int updateByPrimaryKey(TbGroupsmemberlog record);
}