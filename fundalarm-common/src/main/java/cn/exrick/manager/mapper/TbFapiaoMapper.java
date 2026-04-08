package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbFapiao;
import cn.exrick.manager.pojo.TbFapiaoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbFapiaoMapper {
    long countByExample(TbFapiaoExample example);

    int deleteByExample(TbFapiaoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbFapiao record);

    int insertSelective(TbFapiao record);

    List<TbFapiao> selectByExample(TbFapiaoExample example);

    TbFapiao selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbFapiao record, @Param("example") TbFapiaoExample example);

    int updateByExample(@Param("record") TbFapiao record, @Param("example") TbFapiaoExample example);

    int updateByPrimaryKeySelective(TbFapiao record);

    int updateByPrimaryKey(TbFapiao record);
}