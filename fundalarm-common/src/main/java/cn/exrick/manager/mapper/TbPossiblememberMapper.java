package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbPossiblemember;
import cn.exrick.manager.pojo.TbPossiblememberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbPossiblememberMapper {
    long countByExample(TbPossiblememberExample example);

    int deleteByExample(TbPossiblememberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbPossiblemember record);

    int insertSelective(TbPossiblemember record);

    List<TbPossiblemember> selectByExample(TbPossiblememberExample example);

    TbPossiblemember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbPossiblemember record, @Param("example") TbPossiblememberExample example);

    int updateByExample(@Param("record") TbPossiblemember record, @Param("example") TbPossiblememberExample example);

    int updateByPrimaryKeySelective(TbPossiblemember record);

    int updateByPrimaryKey(TbPossiblemember record);
}