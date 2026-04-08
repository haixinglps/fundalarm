package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbXgecustomer;
import cn.exrick.manager.pojo.TbXgecustomerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbXgecustomerMapper {
    long countByExample(TbXgecustomerExample example);

    int deleteByExample(TbXgecustomerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbXgecustomer record);

    int insertSelective(TbXgecustomer record);

    List<TbXgecustomer> selectByExample(TbXgecustomerExample example);

    TbXgecustomer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbXgecustomer record, @Param("example") TbXgecustomerExample example);

    int updateByExample(@Param("record") TbXgecustomer record, @Param("example") TbXgecustomerExample example);

    int updateByPrimaryKeySelective(TbXgecustomer record);

    int updateByPrimaryKey(TbXgecustomer record);
}