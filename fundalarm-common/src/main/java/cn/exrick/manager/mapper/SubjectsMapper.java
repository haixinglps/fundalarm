package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Subjects;
import cn.exrick.manager.pojo.SubjectsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubjectsMapper {
    long countByExample(SubjectsExample example);

    int deleteByExample(SubjectsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Subjects record);

    int insertSelective(Subjects record);

    List<Subjects> selectByExample(SubjectsExample example);

    Subjects selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Subjects record, @Param("example") SubjectsExample example);

    int updateByExample(@Param("record") Subjects record, @Param("example") SubjectsExample example);

    int updateByPrimaryKeySelective(Subjects record);

    int updateByPrimaryKey(Subjects record);
}