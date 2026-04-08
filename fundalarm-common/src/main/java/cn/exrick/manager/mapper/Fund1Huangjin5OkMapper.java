package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Fund1Huangjin5Ok;
import cn.exrick.manager.pojo.Fund1Huangjin5OkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Fund1Huangjin5OkMapper {
    long countByExample(Fund1Huangjin5OkExample example);

    int deleteByExample(Fund1Huangjin5OkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Fund1Huangjin5Ok record);

    int insertSelective(Fund1Huangjin5Ok record);

    List<Fund1Huangjin5Ok> selectByExample(Fund1Huangjin5OkExample example);

    Fund1Huangjin5Ok selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund1Huangjin5Ok record, @Param("example") Fund1Huangjin5OkExample example);

    int updateByExample(@Param("record") Fund1Huangjin5Ok record, @Param("example") Fund1Huangjin5OkExample example);

    int updateByPrimaryKeySelective(Fund1Huangjin5Ok record);

    int updateByPrimaryKey(Fund1Huangjin5Ok record);
}