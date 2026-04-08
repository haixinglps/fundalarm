package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbSubject;
import cn.exrick.manager.pojo.TbSubjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbSubjectMapper {
    long countByExample(TbSubjectExample example);

    int deleteByExample(TbSubjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbSubject record);

    int insertSelective(TbSubject record);

    List<TbSubject> selectByExample(TbSubjectExample example);

    TbSubject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbSubject record, @Param("example") TbSubjectExample example);

    int updateByExample(@Param("record") TbSubject record, @Param("example") TbSubjectExample example);

    int updateByPrimaryKeySelective(TbSubject record);

    int updateByPrimaryKey(TbSubject record);
}