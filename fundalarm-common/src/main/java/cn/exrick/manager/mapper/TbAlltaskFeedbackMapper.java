package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbAlltaskFeedback;
import cn.exrick.manager.pojo.TbAlltaskFeedbackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAlltaskFeedbackMapper {
    long countByExample(TbAlltaskFeedbackExample example);

    int deleteByExample(TbAlltaskFeedbackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbAlltaskFeedback record);

    int insertSelective(TbAlltaskFeedback record);

    List<TbAlltaskFeedback> selectByExample(TbAlltaskFeedbackExample example);

    TbAlltaskFeedback selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbAlltaskFeedback record, @Param("example") TbAlltaskFeedbackExample example);

    int updateByExample(@Param("record") TbAlltaskFeedback record, @Param("example") TbAlltaskFeedbackExample example);

    int updateByPrimaryKeySelective(TbAlltaskFeedback record);

    int updateByPrimaryKey(TbAlltaskFeedback record);
}