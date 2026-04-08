package cn.exrick.manager.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pagehelper.PageInfo;

import cn.exrick.common.pojo.DataTablesResult;
import cn.exrick.common.pojo.MemberLoginRegist;
import cn.exrick.manager.dto.TaskDto;
import cn.exrick.manager.dto.front.TaskFeedBack;
import cn.exrick.manager.dto.front.UserDto;
import cn.exrick.manager.pojo.DyLog;
import cn.exrick.manager.pojo.TbAllmember;
import cn.exrick.manager.pojo.TbAlltask;
import cn.exrick.manager.pojo.TbAlltaskFeedback;
import cn.exrick.manager.pojo.TbAlltaskitem;
import cn.exrick.manager.pojo.TbAlltasklog;
import cn.exrick.manager.pojo.TbCommands;
import cn.exrick.manager.pojo.TbCommandsDesc;
import cn.exrick.manager.pojo.TbCustomer;
import cn.exrick.manager.pojo.TbDestination;
import cn.exrick.manager.pojo.TbFapiao;
import cn.exrick.manager.pojo.TbFeedbackSystem;
import cn.exrick.manager.pojo.TbGroupofhaveadd;
import cn.exrick.manager.pojo.TbGroupofhaveaddlog;
import cn.exrick.manager.pojo.TbGroupofhavezan;
import cn.exrick.manager.pojo.TbGroupofhavezanlog;
import cn.exrick.manager.pojo.TbGroups;
import cn.exrick.manager.pojo.TbGroupscategory;
import cn.exrick.manager.pojo.TbGroupscount;
import cn.exrick.manager.pojo.TbGroupsforpolicy;
import cn.exrick.manager.pojo.TbGroupsmemberlog;
import cn.exrick.manager.pojo.TbItemPrice;
import cn.exrick.manager.pojo.TbItemrelationpolicy;
import cn.exrick.manager.pojo.TbMeeting;
import cn.exrick.manager.pojo.TbMember;
import cn.exrick.manager.pojo.TbNews;
import cn.exrick.manager.pojo.TbOrder;
import cn.exrick.manager.pojo.TbOrdercombine;
import cn.exrick.manager.pojo.TbOrganBankCard;
import cn.exrick.manager.pojo.TbOrganization;
import cn.exrick.manager.pojo.TbPermission;
import cn.exrick.manager.pojo.TbPolicys;
import cn.exrick.manager.pojo.TbPolicysmember;
import cn.exrick.manager.pojo.TbPossiblemember;
import cn.exrick.manager.pojo.TbRechargeManage;
import cn.exrick.manager.pojo.TbRole;
import cn.exrick.manager.pojo.TbRoute;
import cn.exrick.manager.pojo.TbSubject;
import cn.exrick.manager.pojo.TbSubjectalarm;
import cn.exrick.manager.pojo.TbSummery;
import cn.exrick.manager.pojo.TbTraveltype;
import cn.exrick.manager.pojo.TbUser;
import cn.exrick.manager.pojo.TbUserinfo;
import cn.exrick.manager.pojo.TbUsersAccountChange;
import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletTicheng;
import cn.exrick.manager.pojo.TbWeixin;
import cn.exrick.manager.pojo.TbXgecustomer;

/**
 * @author Exrickx
 */
public interface UserpolicyRobotService {

	/**
	 * 通过用户名获取用户
	 * 
	 * @param username
	 * @return
	 */
	TbUser getUserByUsername(String username);

	/**
	 * 通过用户名获取角色
	 * 
	 * @param username
	 * @return
	 */
	Set<String> getRoles(String username);

	/**
	 * 通过用户名获取权限
	 * 
	 * @param username
	 * @return
	 */
	Set<String> getPermissions(String username);

	/**
	 * 获取角色列表
	 * 
	 * @return
	 */
	DataTablesResult getRoleList();

	/**
	 * 获取所有角色
	 * 
	 * @return
	 */
	List<TbRole> getAllRoles();

	/**
	 * 添加角色
	 * 
	 * @param tbRole
	 * @return
	 */
	int addRole(TbRole tbRole);

	/**
	 * 通过角色名获取角色
	 * 
	 * @param roleName
	 * @return
	 */
	TbRole getRoleByRoleName(String roleName);

	/**
	 * 判断角色编辑名是否已存在
	 * 
	 * @param id
	 * @param roleName
	 * @return
	 */
	boolean getRoleByEditName(int id, String roleName);

	/**
	 * 更新角色
	 * 
	 * @param tbRole
	 * @return
	 */
	int updateRole(TbRole tbRole);

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	int deleteRole(int id);

	/**
	 * 统计角色数
	 * 
	 * @return
	 */
	Long countRole();

	/**
	 * 获得所有权限列表
	 * 
	 * @return
	 */
	DataTablesResult getPermissionList();

	/**
	 * 添加权限
	 * 
	 * @param tbPermission
	 * @return
	 */
	int addPermission(TbPermission tbPermission);

	/**
	 * 更新权限
	 * 
	 * @param tbPermission
	 * @return
	 */
	int updatePermission(TbPermission tbPermission);

	/**
	 * 删除权限
	 * 
	 * @param id
	 * @return
	 */
	int deletePermission(int id);

	/**
	 * 统计权限
	 * 
	 * @return
	 */
	Long countPermission();

	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	DataTablesResult getUserList();

	/**
	 * 添加管理员
	 * 
	 * @param user
	 * @return
	 */
	int addUser(TbUser user);

	/**
	 * 通过id获取
	 * 
	 * @param id
	 * @return
	 */
	TbUser getUserById(Long id);

	/**
	 * 通过用户名获取
	 * 
	 * @param username
	 * @return
	 */
	boolean getUserByName(String username);

	/**
	 * 通过手机获取
	 * 
	 * @param phone
	 * @return
	 */
	boolean getUserByPhone(String phone);

	/**
	 * 通过邮件获取
	 * 
	 * @param emaill
	 * @return
	 */
	boolean getUserByEmail(String emaill);

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	int updateUser(TbUser user);

	/**
	 * 更改状态
	 * 
	 * @param id
	 * @param state
	 * @return
	 */
	int changeUserState(Long id, int state);

	/**
	 * 修改密码
	 * 
	 * @param tbUser
	 * @return
	 */
	int changePassword(TbUser tbUser);

	/**
	 * 判断编辑用户名
	 * 
	 * @param id
	 * @param username
	 * @return
	 */
	boolean getUserByEditName(Long id, String username);

	/**
	 * 判断编辑手机
	 * 
	 * @param id
	 * @param phone
	 * @return
	 */
	boolean getUserByEditPhone(Long id, String phone);

	/**
	 * 判断编辑邮箱
	 * 
	 * @param id
	 * @param emaill
	 * @return
	 */
	boolean getUserByEditEmail(Long id, String emaill);

	/**
	 * 删除管理员
	 * 
	 * @param userId
	 * @return
	 */
	int deleteUser(Long userId);

	/**
	 * 统计管理员
	 * 
	 * @return
	 */
	Long countUser();

	TbOrganization getOrgan(String username);

	TbOrganization getOrganbyid(long id);

	List<TbOrder> queryorder(long organid, String locationtype, int status, int page, int size);

	TbOrder getorder(String id);

	void delorder(long id);

	void updateorder(TbOrder order);

	void delorder(String id);

	void addorder(TbOrder order);

	void addorgan(TbOrganization organ);

	void addbankcard(TbOrganBankCard card);

	TbOrganBankCard getbankoforgan(long organid);

	void updatebankcard(TbOrganBankCard card);

	void addaccountlog(TbUsersAccountChange accountChange);

	List<TbUsersAccountChange> getlogs(long organid, long majororganid, int page, int size);

	int updatewallet(TbWallet wallet);

	void addwallet(TbWallet wallet);

	TbWallet getwallet(long organid);

	TbTraveltype gettravetypeofywx(String safetype, int days);

	TbTraveltype gettravetypeoflzx(String locationtype, int days);

	int savemember(TbMember member);

	List<TbOrganization> getOrganbyparentid(long parentid);

	int updatepayorder(long organid, TbOrder order);

	int updatecancelorder(long organid, TbOrder order);

	void delorgan(long id);

	TbOrganization getOrganbyname(String name);

	List<TbUser> getUserByOrganid(long organid, int page, int size);

	int addUser2(TbUser user);

	void saverecharge(TbRechargeManage item);

	TbRechargeManage getrecharge(long id);

	int updaterecharge(TbRechargeManage item);

	List<TbRechargeManage> getrechargelist(long userid, Date starttime, Date endtime, int status, int page, int size);

//	List<Map> getlogs2(long organid, long majororganid, int page, int size);

	List<TbSummery> getsummerys(int status, int page, int size);

	List<TbMember> getmembersbyorderid(String orderid);

	void deletmemberoforder(String orderid);

	TbMember getmembersbyid(long id);

	int updatemember(TbMember member);

	void updateorder2(TbOrder order, TbOrder orderold);

	void addorderandpay(TbOrder order);

	void passsummerys(TbSummery summery);

	TbSummery getsummery(int id);

	TbOrder getorderbyorderid(String orderid);

	void updateorgan(TbOrganization organ);

	List<Map> querys(String sql, int page, int size);

	int updatepayorderreply(long organid, TbOrder order, int type);

	String payOrder2(long orderId, String paymentType, String terminalip, String openId, String parentid,
			String redirect);

	String payOrder(long orderId, String terminalip);

	String payOrder2bc(String orderId, BigDecimal payment, String terminalip, String openId, String parentid,
			String redirect);

	String payOrderbc(String orderId, BigDecimal payment, String terminalip);

	// int updatepayorderreplybc(long organid, TbOrder order, int type);
	//
	// int updatepayorderreplybc(long organid, TbOrder order, int type,
	// String status);

	int updatepayorderreplybc(String tf, long organid, TbOrder order, int type, String status);

	void updateorder2ce(TbOrder order, TbOrder orderold);

	List<TbOrder> querypayorder(int page, int size);

	List<TbOrder> querypayorderce(int page, int size);

	void addwalletticheng(TbWalletTicheng walletticheng);

	TbWalletTicheng getwalletticheng(long organid);

	int updatewalletticheng(TbWalletTicheng walletTicheng);

	Map<String, BigDecimal> moneytopay(BigDecimal ordrpayment, long organid);

//	List<Map> getlogs2two(long organid, long majororganid, int page, int size);

	List<Map> getlogs2(long organid, long majororganid, int page, int size, long begintime, long endtime);

	List<Map> getlogs2two(long organid, long majororganid, int page, int size, long begintime, long endtime);

	int addnews(TbNews tbNews);

	List<TbNews> getnews(int page, int size);

	int addroute(TbRoute route, List<TbDestination> destinations);

	TbRoute getroute(int id);

	List<TbDestination> getdestinationsbyrouteid(int id);

	void updateroute(TbRoute route, List<TbDestination> destinations);

	List<TbRoute> getroutebyname(String routename, long organid);

	void delroute(int id);

	List<TbOrder> querypayorderysx(int page, int size);

	List<TbOrder> querypayorderysxce(int page, int size);

	List<TbOrder> querypayorderysxceforupdate(int page, int size);

	List<TbOrder> querypayorderysxforupdate(int page, int size);

//	int register(MemberLoginRegist memberLoginRegist);

	boolean codeVerify(String phone, String validate);

	// PageInfo<TbPossiblemember> getpossiblemembers(int page, int size);

	PageInfo<TbPossiblemember> getpossiblemembers(int customertype, int searchtag, int page, int size, int key,
			String keyword, int key2);

	TbPossiblemember getpossiblemember(long id);

	int updatePossiblemember(TbPossiblemember mPossiblemember);

	int addmymember(TbAllmember allmember);

	PageInfo<TbAllmember> getmymembers(int customertype, int searchtag, int page, int size, int key, String keyword,
			long ownerid, int key2);

	TbAllmember getmymember(long id);

	int updateallmember(TbAllmember allmember);

	int updatemeeting(TbMeeting meeting);

	int addmeeting(TbMeeting meeting);

	List<TbMeeting> getmeetings(int page, int size, long userid);

	int updatelockmember(TbPossiblemember mPossiblemember, TbAllmember allmember);

	int updatecancelmember(TbPossiblemember mPossiblemember, TbAllmember allmember);

	long addorgan2(TbOrganization organ);

	int addregister(MemberLoginRegist memberLoginRegist);

	PageInfo<TbCustomer> getmycustomers(Integer page, Integer size, int customertype, long ownerid);

	PageInfo<TbCustomer> getmycustomers(int customertype, int searchtag, int page, int size, int key, String keyword,
			long ownerid, int key2);

	TbCustomer getmycustomer(int id);

	int updatecustomer(TbCustomer customer);

	PageInfo<TbPossiblemember> getmypossiblemembers(long createrid, int page, int size);

	int addpossiblemember(TbPossiblemember possiblemember);

//	PageInfo<TbPolicys> getmypolicys(Integer page, Integer size, long userid);

//	void generateorderbypolicyid(int policyid);

//	PageInfo<TbOrder> getmyorders(Integer page, Integer size, int policyid);

//	int addpolicy(TbPolicys tbPolicys);

	int addpolicy(TbPolicys tbPolicys, String members);

	List<Map> getproductsbypolicyid(int policyid);

	int delpossiblemember(long id);

	List<TbCustomer> getmycustomersbytypeandkeyword(long ownerid, int customertype, String keyword);

	List<TbPolicys> querypaypolicysysx(int page, int size);

	void updatepolicy(TbPolicys policys);

	PageInfo<TbPolicys> getmypolicysbycustomerid(Integer page, Integer size, long customerid);

	TbCustomer getmycustomersbyphone(String phone);

	List<TbPolicysmember> getmembersbypolicyid(long policyid);

	List<TbItemrelationpolicy> getitemsbypolicyid(int policyid);

//	void generateorderbypolicyid(int policyid, Date beginDate, Date enddDate);

	void updategenerateorderbypolicyid(int policyid, Date beginDate, Date enddDate);

	int updatememberofpolicy(TbPolicysmember member);

	List<TbPolicys> querypaypolicybystatus(int page, int size, int status);

	List<TbPolicysmember> getmymembersbypolicyid(int policyid);

	void updatepolicymember(int policyid, List<TbPolicysmember> members, List<TbPolicysmember> editmembers,
			List<TbPolicysmember> delmembers);

	PageInfo<TbMember> getmembersbyorderidbypagesize(String orderid, int page, int size);

	TbPolicysmember getPolicysmemberbyid(long id);

	PageInfo<TbOrder> getmyorders(Integer page, Integer size, int policyid, Integer status);

	List<TbItemPrice> getpriceofitem(int itemid);

	void updatepolicymemberforcurrent(int policyid, List<TbPolicysmember> members, List<TbPolicysmember> editmembers,
			List<TbPolicysmember> delmembers);

	void saveuserinfo(TbUserinfo userinfo);

	TbUserinfo getuserinfobyid(int id);

	void updateuserinfo(TbUserinfo userinfo);

	TbFapiao getfapiao(int id);

	void savefapiao(TbFapiao fapiao);

	TbPolicys getpolicybyid(int id);

	void updatefapiao(TbFapiao fapiao);

	TbUserinfo getuserinfobyuserid(long id);

	TbFapiao getfapiaobypolicyid(int id);

	TbTraveltype getitembyid(int id);

	List<TbOrder> querypayorderbystatus(Integer status, int page, int size);

	int updateorder2(TbOrder order);

	void updatesyncpolicystoyoubao(TbOrder order);

	List<TbPolicysmember> getmembersbypolicyidforcompany(long policyid);

	TbOrder getmainorderbypolicyid(int policyid);

	PageInfo<TbPolicys> getmypolicysofright(Integer page, Integer size, long userid);

	PageInfo<TbPolicys> getmypolicys(Integer page, Integer size, long userid, TbPolicys condition);

	int delpolicy(int id, long userid);

	int combineorders(String orders);

	TbOrdercombine getcombineorder(String id);

	String payOrdercom(long orderId, BigDecimal payment, String terminalip);

	String payOrder2com(long orderId, String paymentType, String terminalip, String openId, String parentid,
			String redirect, BigDecimal payment);

	int updatepayordercomreply(TbOrdercombine cOrdercombine);

	int addpolicyforguest(TbPolicys tbPolicys, String membersst);

	List<TbGroupsforpolicy> getgroupsbypolicyid(int policyid);

	int addgroupforpolicy(TbGroupsforpolicy group);

	PageInfo<TbPossiblemember> getmypossiblemembersbysort(long createrid, int page, int size, String sortname,
			String sortorder);

	PageInfo<TbAllmember> getmymembersbysort(int customertype, int searchtag, int page, int size, int key,
			String keyword, long ownerid, int key2, String sortname, String sortorder);

	PageInfo<TbCustomer> getmycustomersbysort(int customertype, int searchtag, int page, int size, int key,
			String keyword, long ownerid, int key2, String sortname, String sortorder);

	PageInfo<TbNews> getnewsall(int page, int size);

	PageInfo<TbNews> getnewsall(int page, int size, Long begintime, Long endtime);

	int addweixin(TbWeixin group);

	List<TbWeixin> getweixins(int page, int size);

	List<TbGroups> getgroupbynameandrobotid(String groupname, int robotid);

	int addgroup(TbGroups groups);

	int updategroup(TbGroups groups);

	int addgroupcc(TbGroupscount groupscc);

	int updategroupcc(TbGroupscount groupscc);

	List<TbGroupscount> getgroupccbygroupid(int groupid, int dt);

	List<TbAlltaskitem> getalltaskitem(Integer taskid);

	TbGroups getgroupbyid(int id);

	TbAlltask gettaskbyid(int id);

	int updatetask(TbAlltask task);

	int savetask(TbAlltask task);

	List<TbAlltask> getsametimemytasks(TbAlltask task, Integer robotid);

	PageInfo<TbGroups> getmygroups(Integer robotid, int page, int size, int status);

	List<TbAlltask> gettasksforexecute(Integer hours, Integer minute, Integer status, int page, int size);

	String selectgetalltaskstoexecute(int hours, int minute, int status);

	int addtasklog(TbAlltasklog log);

	TbAlltasklog gettasklog(int id);

	int updatetasklog(TbAlltasklog tasklog);

	TbGroupofhaveaddlog getaddflog(int id);

	int addaddflog(TbGroupofhaveaddlog addflog);

	int updateaddflog(TbGroupofhaveaddlog addflog);

	int updateaddf(TbGroupofhaveadd addf);

	TbGroupofhaveadd getaddfbyid(int id);

	TbGroupofhavezanlog getzanlog(int id);

	int saveaddf(TbGroupofhaveadd addf);

	int addzanlog(TbGroupofhavezanlog zanlog);

	int updatezanlog(TbGroupofhavezanlog zanlog);

	int updatezan(TbGroupofhavezan zan);

	TbGroupofhavezan getzanbyid(int id);

	int savezan(TbGroupofhavezan zan);

	TbGroupofhavezan getzanbyrobotid(long robotid);

//	TbGroupofhaveadd getaddfbyrobotid(long robotid);

	TbGroupofhaveadd getaddfbyrobotidandgroupid(long robotid, int groupid);

	List<TbWeixin> getweixinsbytime(int page, int size, long dt);

	List<TbWeixin> getweixinsbyid(int page, int size, int id);

	List<TbSubject> getsubjects(int page, int size);

	void savealarm(TbSubjectalarm alarm);

	PageInfo<TbSubject> getsubjectsbyorganid(int page, int size, int organid);

//	PageInfo<TbSubjectalarm> getsubjectalarmsbyorganid(int page, int size, int organid);

	void updatesubject(TbSubject subject);

	void delsubject(int id);

	void savesubject(TbSubject subject);

	List<TbWeixin> getweixinsbyorganid(int page, int size, String keyword, Integer organid);

	int updateweixin(TbWeixin weixin);

	int addmemberlog(TbGroupsmemberlog log);

	int updatememberlog(TbGroupsmemberlog log);

	List<TbGroupsmemberlog> getmemberlogbygroupidandname(int groupid, int dt, String name);

	List<TbGroupofhavezanlog> getzanlogsbytime(int zanid, Date begintime, Date endtime);

	int updateUserallinfo(TbUser user);

	PageInfo<TbSubjectalarm> getsubjectalarmsbyorganid(int page, int size, int organid, Long begin, Long end);

	int updatetaskandtaskitem(TbAlltask task);

	void savexgecustomer(TbXgecustomer subject);

	List<TbGroupscategory> getgroupscategory(int page, int size, int organid);

	void updategroupscategory(TbGroupscategory cGroupscategory);

	void deletegroupscategory(int id);

	void savegroupscategory(TbGroupscategory category);

	List<TbGroups> getgroupsbycate(int organid, int cate, int page, int size);

	TbGroupscategory getgroupscategorybyid(int id);

	void saveDyLog(DyLog item);

	void addSystFeedback(TbFeedbackSystem feedback);

	List<TbAlltask> getmytasks(Integer robotid, int page, int size, Integer status);

	PageInfo<TbAlltaskFeedback> getHistoryFeedback(Integer taskId, int page, int size);

	PageInfo<TaskDto> getAlltask(int sort, int page, int size);

	int addFeedback(TaskFeedBack feedBack);

	PageInfo<TbCommands> getAllscript(int sort, int page, int size);

	PageInfo<UserDto> getOnline(int sort, int page, int size);

	TbCommandsDesc getTemplate(String scriptcode);

//	PageInfo<TbCommands> getAllscriptForOnline(int sort, int page, int size);

//	PageInfo<TbCommands> getAllscriptForH5(int sort, int page, int size);
//
//	PageInfo<TbCommands> getAllscriptForClient(int sort, int page, int size);

	PageInfo<TbCommands> getAllscriptForOnline(int sort, int page, int size, int panelid);

//	PageInfo<Map> getmypolicysbypolicys(Integer page, Integer size, Map<String, String> condition);

}
