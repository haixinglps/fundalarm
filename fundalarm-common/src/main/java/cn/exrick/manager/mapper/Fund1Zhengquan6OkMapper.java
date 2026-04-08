package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.Fund1Zhengquan6Ok;
import cn.exrick.manager.pojo.Fund1Zhengquan6OkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Fund1Zhengquan6OkMapper {
    long countByExample(Fund1Zhengquan6OkExample example);

    int deleteByExample(Fund1Zhengquan6OkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Fund1Zhengquan6Ok record);

    int insertSelective(Fund1Zhengquan6Ok record);

    List<Fund1Zhengquan6Ok> selectByExample(Fund1Zhengquan6OkExample example);

    Fund1Zhengquan6Ok selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund1Zhengquan6Ok record, @Param("example") Fund1Zhengquan6OkExample example);

    int updateByExample(@Param("record") Fund1Zhengquan6Ok record, @Param("example") Fund1Zhengquan6OkExample example);

    int updateByPrimaryKeySelective(Fund1Zhengquan6Ok record);

    int updateByPrimaryKey(Fund1Zhengquan6Ok record);
}