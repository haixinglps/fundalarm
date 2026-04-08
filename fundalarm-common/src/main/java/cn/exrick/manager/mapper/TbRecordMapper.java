package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbRecord;
import cn.exrick.manager.pojo.TbRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbRecordMapper {
    long countByExample(TbRecordExample example);

    int deleteByExample(TbRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbRecord record);

    int insertSelective(TbRecord record);

    List<TbRecord> selectByExample(TbRecordExample example);

    TbRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbRecord record, @Param("example") TbRecordExample example);

    int updateByExample(@Param("record") TbRecord record, @Param("example") TbRecordExample example);

    int updateByPrimaryKeySelective(TbRecord record);

    int updateByPrimaryKey(TbRecord record);
}