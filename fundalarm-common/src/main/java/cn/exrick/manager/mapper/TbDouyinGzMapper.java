package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbDouyinGz;
import cn.exrick.manager.pojo.TbDouyinGzExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDouyinGzMapper {
    long countByExample(TbDouyinGzExample example);

    int deleteByExample(TbDouyinGzExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbDouyinGz record);

    int insertSelective(TbDouyinGz record);

    List<TbDouyinGz> selectByExample(TbDouyinGzExample example);

    TbDouyinGz selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbDouyinGz record, @Param("example") TbDouyinGzExample example);

    int updateByExample(@Param("record") TbDouyinGz record, @Param("example") TbDouyinGzExample example);

    int updateByPrimaryKeySelective(TbDouyinGz record);

    int updateByPrimaryKey(TbDouyinGz record);
}