package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Wanwu;
import cn.exrick.manager.pojo.WanwuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WanwuMapper {
    long countByExample(WanwuExample example);

    int deleteByExample(WanwuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Wanwu record);

    int insertSelective(Wanwu record);

    List<Wanwu> selectByExample(WanwuExample example);

    Wanwu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Wanwu record, @Param("example") WanwuExample example);

    int updateByExample(@Param("record") Wanwu record, @Param("example") WanwuExample example);

    int updateByPrimaryKeySelective(Wanwu record);

    int updateByPrimaryKey(Wanwu record);
}