package cn.exrick.trade.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.exrick.manager.pojo.TbOrderchild;
import cn.exrick.manager.pojo.TbOrderchildofyoubao;
import cn.exrick.manager.pojo.TbRecord;
 
 

public interface ShortVideoyoubaoService {
 

	int addpolicy(TbOrderchild tbOrderchild);
	List<Map> getrecordsforcheck(int organid,int page,int size,int status);

	int passrecord(int  recordid,int status,long organid,String desc);
	TbRecord saverecord(TbRecord record);
	
	List<TbOrderchild> getorderchildsbyorderid(String orderid, String phone, String policytype);
	List<TbOrderchild> getorderchildsbyorderidanduserid(String orderid, long userid, String policytype);
	void updateorderchild(TbOrderchild orderchild);
	List<Map> getrecordlistbycompanyidanduserid(long companytype, Long userid, int page, int size);

	List<TbOrderchild> getorderchildsbycompanyanduserid(long companytype, long userid, String policytype);
	List<Map> getrecordsforcheckforcustomer(int organid, int page, int size, int status);


}
