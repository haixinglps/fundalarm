package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbRoute;
import cn.exrick.manager.pojo.TbRouteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbRouteMapper {
    long countByExample(TbRouteExample example);

    int deleteByExample(TbRouteExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbRoute record);

    int insertSelective(TbRoute record);

    List<TbRoute> selectByExample(TbRouteExample example);

    TbRoute selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbRoute record, @Param("example") TbRouteExample example);

    int updateByExample(@Param("record") TbRoute record, @Param("example") TbRouteExample example);

    int updateByPrimaryKeySelective(TbRoute record);

    int updateByPrimaryKey(TbRoute record);
}