package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbDycomment;
import cn.exrick.manager.pojo.TbDycommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDycommentMapper {
    long countByExample(TbDycommentExample example);

    int deleteByExample(TbDycommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbDycomment record);

    int insertSelective(TbDycomment record);

    List<TbDycomment> selectByExample(TbDycommentExample example);

    TbDycomment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbDycomment record, @Param("example") TbDycommentExample example);

    int updateByExample(@Param("record") TbDycomment record, @Param("example") TbDycommentExample example);

    int updateByPrimaryKeySelective(TbDycomment record);

    int updateByPrimaryKey(TbDycomment record);
}