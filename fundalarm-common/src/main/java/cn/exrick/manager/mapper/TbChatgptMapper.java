package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbChatgpt;
import cn.exrick.manager.pojo.TbChatgptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbChatgptMapper {
    long countByExample(TbChatgptExample example);

    int deleteByExample(TbChatgptExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbChatgpt record);

    int insertSelective(TbChatgpt record);

    List<TbChatgpt> selectByExample(TbChatgptExample example);

    TbChatgpt selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbChatgpt record, @Param("example") TbChatgptExample example);

    int updateByExample(@Param("record") TbChatgpt record, @Param("example") TbChatgptExample example);

    int updateByPrimaryKeySelective(TbChatgpt record);

    int updateByPrimaryKey(TbChatgpt record);
}