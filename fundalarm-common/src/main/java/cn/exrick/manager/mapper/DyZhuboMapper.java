package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.DyZhubo;
import cn.exrick.manager.pojo.DyZhuboExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DyZhuboMapper {
    long countByExample(DyZhuboExample example);

    int deleteByExample(DyZhuboExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DyZhubo record);

    int insertSelective(DyZhubo record);

    List<DyZhubo> selectByExample(DyZhuboExample example);

    DyZhubo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DyZhubo record, @Param("example") DyZhuboExample example);

    int updateByExample(@Param("record") DyZhubo record, @Param("example") DyZhuboExample example);

    int updateByPrimaryKeySelective(DyZhubo record);

    int updateByPrimaryKey(DyZhubo record);
}