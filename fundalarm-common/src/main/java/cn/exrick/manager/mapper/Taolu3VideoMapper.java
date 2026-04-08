package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Taolu3VideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Taolu3VideoMapper {
    long countByExample(Taolu3VideoExample example);

    int deleteByExample(Taolu3VideoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Taolu3Video record);

    int insertSelective(Taolu3Video record);

    List<Taolu3Video> selectByExample(Taolu3VideoExample example);

    Taolu3Video selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Taolu3Video record, @Param("example") Taolu3VideoExample example);

    int updateByExample(@Param("record") Taolu3Video record, @Param("example") Taolu3VideoExample example);

    int updateByPrimaryKeySelective(Taolu3Video record);

    int updateByPrimaryKey(Taolu3Video record);
}