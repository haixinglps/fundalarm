package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbTraveltype;
import cn.exrick.manager.pojo.TbTraveltypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbTraveltypeMapper {
    long countByExample(TbTraveltypeExample example);

    int deleteByExample(TbTraveltypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbTraveltype record);

    int insertSelective(TbTraveltype record);

    List<TbTraveltype> selectByExample(TbTraveltypeExample example);

    TbTraveltype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbTraveltype record, @Param("example") TbTraveltypeExample example);

    int updateByExample(@Param("record") TbTraveltype record, @Param("example") TbTraveltypeExample example);

    int updateByPrimaryKeySelective(TbTraveltype record);

    int updateByPrimaryKey(TbTraveltype record);
}