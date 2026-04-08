package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbUserGateid;
import cn.exrick.manager.pojo.TbUserGateidExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbUserGateidMapper {
    long countByExample(TbUserGateidExample example);

    int deleteByExample(TbUserGateidExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbUserGateid record);

    int insertSelective(TbUserGateid record);

    List<TbUserGateid> selectByExample(TbUserGateidExample example);

    TbUserGateid selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbUserGateid record, @Param("example") TbUserGateidExample example);

    int updateByExample(@Param("record") TbUserGateid record, @Param("example") TbUserGateidExample example);

    int updateByPrimaryKeySelective(TbUserGateid record);

    int updateByPrimaryKey(TbUserGateid record);
}