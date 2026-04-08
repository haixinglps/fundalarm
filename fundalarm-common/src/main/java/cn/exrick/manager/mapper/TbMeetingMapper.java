package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbMeeting;
import cn.exrick.manager.pojo.TbMeetingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbMeetingMapper {
    long countByExample(TbMeetingExample example);

    int deleteByExample(TbMeetingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbMeeting record);

    int insertSelective(TbMeeting record);

    List<TbMeeting> selectByExample(TbMeetingExample example);

    TbMeeting selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbMeeting record, @Param("example") TbMeetingExample example);

    int updateByExample(@Param("record") TbMeeting record, @Param("example") TbMeetingExample example);

    int updateByPrimaryKeySelective(TbMeeting record);

    int updateByPrimaryKey(TbMeeting record);
}