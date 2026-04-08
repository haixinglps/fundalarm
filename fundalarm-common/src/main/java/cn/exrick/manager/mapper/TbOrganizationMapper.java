package cn.exrick.manager.mapper;

import cn.exrick.manager.pojo.TbOrganization;
import cn.exrick.manager.pojo.TbOrganizationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbOrganizationMapper {
    long countByExample(TbOrganizationExample example);

    int deleteByExample(TbOrganizationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbOrganization record);

    int insertSelective(TbOrganization record);

    List<TbOrganization> selectByExample(TbOrganizationExample example);

    TbOrganization selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbOrganization record, @Param("example") TbOrganizationExample example);

    int updateByExample(@Param("record") TbOrganization record, @Param("example") TbOrganizationExample example);

    int updateByPrimaryKeySelective(TbOrganization record);

    int updateByPrimaryKey(TbOrganization record);
}