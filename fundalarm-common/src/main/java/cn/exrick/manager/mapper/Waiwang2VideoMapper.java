package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.Waiwang2VideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Waiwang2VideoMapper {
    long countByExample(Waiwang2VideoExample example);

    int deleteByExample(Waiwang2VideoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Waiwang2Video record);

    int insertSelective(Waiwang2Video record);

    List<Waiwang2Video> selectByExample(Waiwang2VideoExample example);

    Waiwang2Video selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Waiwang2Video record, @Param("example") Waiwang2VideoExample example);

    int updateByExample(@Param("record") Waiwang2Video record, @Param("example") Waiwang2VideoExample example);

    int updateByPrimaryKeySelective(Waiwang2Video record);

    int updateByPrimaryKey(Waiwang2Video record);
}