package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbFeedbackSystem;
import cn.exrick.manager.pojo.TbFeedbackSystemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbFeedbackSystemMapper {
    long countByExample(TbFeedbackSystemExample example);

    int deleteByExample(TbFeedbackSystemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbFeedbackSystem record);

    int insertSelective(TbFeedbackSystem record);

    List<TbFeedbackSystem> selectByExample(TbFeedbackSystemExample example);

    TbFeedbackSystem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbFeedbackSystem record, @Param("example") TbFeedbackSystemExample example);

    int updateByExample(@Param("record") TbFeedbackSystem record, @Param("example") TbFeedbackSystemExample example);

    int updateByPrimaryKeySelective(TbFeedbackSystem record);

    int updateByPrimaryKey(TbFeedbackSystem record);
}