package cn.exrick.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2OkExample;

public interface Fund1Gaoduanzhuangbei2OkMapper {
	long countByExample(Fund1Gaoduanzhuangbei2OkExample example);

	int deleteByExample(Fund1Gaoduanzhuangbei2OkExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(Fund1Gaoduanzhuangbei2Ok record);

	int insertSelective(Fund1Gaoduanzhuangbei2Ok record);

	List<Fund1Gaoduanzhuangbei2Ok> selectByExample(Fund1Gaoduanzhuangbei2OkExample example);

	Fund1Gaoduanzhuangbei2Ok selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByExampleSelectiveReset(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByExampleSelectiveResetZhiying(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByExampleSelectiveResetIscurrent(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByExampleSelectiveResetNext(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByExample(@Param("record") Fund1Gaoduanzhuangbei2Ok record,
			@Param("example") Fund1Gaoduanzhuangbei2OkExample example);

	int updateByPrimaryKeySelective(Fund1Gaoduanzhuangbei2Ok record);

	int updateByPrimaryKey(Fund1Gaoduanzhuangbei2Ok record);

	List<Fund1Gaoduanzhuangbei2Ok> selectByExampleDynamic(Fund1Gaoduanzhuangbei2OkExample example);
}