package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbDouyinMessage;
import cn.exrick.manager.pojo.TbDouyinMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDouyinMessageMapper {
    long countByExample(TbDouyinMessageExample example);

    int deleteByExample(TbDouyinMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbDouyinMessage record);

    int insertSelective(TbDouyinMessage record);

    List<TbDouyinMessage> selectByExample(TbDouyinMessageExample example);

    TbDouyinMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbDouyinMessage record, @Param("example") TbDouyinMessageExample example);

    int updateByExample(@Param("record") TbDouyinMessage record, @Param("example") TbDouyinMessageExample example);

    int updateByPrimaryKeySelective(TbDouyinMessage record);

    int updateByPrimaryKey(TbDouyinMessage record);
}