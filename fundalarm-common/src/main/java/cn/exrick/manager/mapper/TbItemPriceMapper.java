package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbItemPrice;
import cn.exrick.manager.pojo.TbItemPriceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbItemPriceMapper {
    long countByExample(TbItemPriceExample example);

    int deleteByExample(TbItemPriceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbItemPrice record);

    int insertSelective(TbItemPrice record);

    List<TbItemPrice> selectByExample(TbItemPriceExample example);

    TbItemPrice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbItemPrice record, @Param("example") TbItemPriceExample example);

    int updateByExample(@Param("record") TbItemPrice record, @Param("example") TbItemPriceExample example);

    int updateByPrimaryKeySelective(TbItemPrice record);

    int updateByPrimaryKey(TbItemPrice record);
}