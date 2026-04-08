package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Fund1Jungong3Ok;
import cn.exrick.manager.pojo.Fund1Jungong3OkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Fund1Jungong3OkMapper {
    long countByExample(Fund1Jungong3OkExample example);

    int deleteByExample(Fund1Jungong3OkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Fund1Jungong3Ok record);

    int insertSelective(Fund1Jungong3Ok record);

    List<Fund1Jungong3Ok> selectByExample(Fund1Jungong3OkExample example);

    Fund1Jungong3Ok selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund1Jungong3Ok record, @Param("example") Fund1Jungong3OkExample example);

    int updateByExample(@Param("record") Fund1Jungong3Ok record, @Param("example") Fund1Jungong3OkExample example);

    int updateByPrimaryKeySelective(Fund1Jungong3Ok record);

    int updateByPrimaryKey(Fund1Jungong3Ok record);
}