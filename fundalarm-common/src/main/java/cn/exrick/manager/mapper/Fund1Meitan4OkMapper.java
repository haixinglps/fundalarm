package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Fund1Meitan4Ok;
import cn.exrick.manager.pojo.Fund1Meitan4OkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Fund1Meitan4OkMapper {
    long countByExample(Fund1Meitan4OkExample example);

    int deleteByExample(Fund1Meitan4OkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Fund1Meitan4Ok record);

    int insertSelective(Fund1Meitan4Ok record);

    List<Fund1Meitan4Ok> selectByExample(Fund1Meitan4OkExample example);

    Fund1Meitan4Ok selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund1Meitan4Ok record, @Param("example") Fund1Meitan4OkExample example);

    int updateByExample(@Param("record") Fund1Meitan4Ok record, @Param("example") Fund1Meitan4OkExample example);

    int updateByPrimaryKeySelective(Fund1Meitan4Ok record);

    int updateByPrimaryKey(Fund1Meitan4Ok record);
}