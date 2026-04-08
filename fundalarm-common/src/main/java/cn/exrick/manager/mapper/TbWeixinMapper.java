package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbWeixin;
import cn.exrick.manager.pojo.TbWeixinExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbWeixinMapper {
    long countByExample(TbWeixinExample example);

    int deleteByExample(TbWeixinExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbWeixin record);

    int insertSelective(TbWeixin record);

    List<TbWeixin> selectByExampleWithBLOBs(TbWeixinExample example);

    List<TbWeixin> selectByExample(TbWeixinExample example);

    TbWeixin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbWeixin record, @Param("example") TbWeixinExample example);

    int updateByExampleWithBLOBs(@Param("record") TbWeixin record, @Param("example") TbWeixinExample example);

    int updateByExample(@Param("record") TbWeixin record, @Param("example") TbWeixinExample example);

    int updateByPrimaryKeySelective(TbWeixin record);

    int updateByPrimaryKeyWithBLOBs(TbWeixin record);

    int updateByPrimaryKey(TbWeixin record);
}