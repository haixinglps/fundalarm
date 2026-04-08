package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbOrderchild;
import cn.exrick.manager.pojo.TbOrderchildExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbOrderchildMapper {
    long countByExample(TbOrderchildExample example);

    int deleteByExample(TbOrderchildExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrderchild record);

    int insertSelective(TbOrderchild record);

    List<TbOrderchild> selectByExample(TbOrderchildExample example);

    TbOrderchild selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrderchild record, @Param("example") TbOrderchildExample example);

    int updateByExample(@Param("record") TbOrderchild record, @Param("example") TbOrderchildExample example);

    int updateByPrimaryKeySelective(TbOrderchild record);

    int updateByPrimaryKey(TbOrderchild record);
}