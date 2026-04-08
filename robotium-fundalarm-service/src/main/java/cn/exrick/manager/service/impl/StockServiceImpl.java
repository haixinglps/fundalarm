package cn.exrick.manager.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.mapper.CategoryMapper;
import cn.exrick.manager.mapper.DyLogMapper;
import cn.exrick.manager.mapper.Fund1Gaoduanzhuangbei2OkMapper;
import cn.exrick.manager.mapper.FundMapper;
import cn.exrick.manager.mapper.StockDataHistoryMapper;
import cn.exrick.manager.mapper.StockDataMapper;
import cn.exrick.manager.mapper.StockMapper;
import cn.exrick.manager.mapper.SubjectsMapper;
import cn.exrick.manager.mapper.TbAlltaskFeedbackMapper;
import cn.exrick.manager.mapper.TbAlltaskMapper;
import cn.exrick.manager.mapper.TbAlltaskitemMapper;
import cn.exrick.manager.mapper.TbAlltasklogMapper;
import cn.exrick.manager.mapper.TbCommandsDescMapper;
import cn.exrick.manager.mapper.TbCommandsMapper;
import cn.exrick.manager.mapper.TbDestinationMapper;
import cn.exrick.manager.mapper.TbDouyinGzMapper;
import cn.exrick.manager.mapper.TbDouyinMessageMapper;
import cn.exrick.manager.mapper.TbFapiaoMapper;
import cn.exrick.manager.mapper.TbFeedbackSystemMapper;
import cn.exrick.manager.mapper.TbGroupofhaveaddMapper;
import cn.exrick.manager.mapper.TbGroupofhaveaddlogMapper;
import cn.exrick.manager.mapper.TbGroupofhavezanMapper;
import cn.exrick.manager.mapper.TbGroupofhavezanlogMapper;
import cn.exrick.manager.mapper.TbGroupsMapper;
import cn.exrick.manager.mapper.TbGroupscategoryMapper;
import cn.exrick.manager.mapper.TbGroupscountMapper;
import cn.exrick.manager.mapper.TbGroupsforpolicyMapper;
import cn.exrick.manager.mapper.TbGroupsmemberlogMapper;
import cn.exrick.manager.mapper.TbItemPriceMapper;
import cn.exrick.manager.mapper.TbMemberMapper;
import cn.exrick.manager.mapper.TbNewsMapper;
import cn.exrick.manager.mapper.TbOrderMapper;
import cn.exrick.manager.mapper.TbOrganBankCardMapper;
import cn.exrick.manager.mapper.TbOrganizationMapper;
import cn.exrick.manager.mapper.TbPanelContentMapper;
import cn.exrick.manager.mapper.TbPanelMapper;
import cn.exrick.manager.mapper.TbPermissionMapper;
import cn.exrick.manager.mapper.TbPolicysmemberMapper;
import cn.exrick.manager.mapper.TbRechargeManageMapper;
import cn.exrick.manager.mapper.TbRoleMapper;
import cn.exrick.manager.mapper.TbRolePermMapper;
import cn.exrick.manager.mapper.TbRouteMapper;
import cn.exrick.manager.mapper.TbSubjectMapper;
import cn.exrick.manager.mapper.TbSubjectalarmMapper;
import cn.exrick.manager.mapper.TbSummeryMapper;
import cn.exrick.manager.mapper.TbTraveltypeMapper;
import cn.exrick.manager.mapper.TbUserGateidMapper;
import cn.exrick.manager.mapper.TbUserMapper;
import cn.exrick.manager.mapper.TbUserinfoMapper;
import cn.exrick.manager.mapper.TbUsersAccountChangeMapper;
import cn.exrick.manager.mapper.TbUsersAccountChangeTichengMapper;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.mapper.TbWalletTichengMapper;
import cn.exrick.manager.mapper.TbWeixinMapper;
import cn.exrick.manager.mapper.TbXgecustomerMapper;
import cn.exrick.manager.pojo.Category;
import cn.exrick.manager.pojo.CategoryExample;
import cn.exrick.manager.pojo.Stock;
import cn.exrick.manager.pojo.StockDataHistory;
import cn.exrick.manager.pojo.StockDataHistoryExample;
import cn.exrick.manager.pojo.StockDataWithBLOBs;
import cn.exrick.manager.pojo.StockExample;
import cn.exrick.manager.service.StockService;
import cn.exrick.manager.service.util.AlarmPlayer;

@Service
public class StockServiceImpl implements StockService {
	private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private TbCommandsDescMapper tbCommandsDescMapper;
	@Autowired
	private TbCommandsMapper tbCommandsMapper;
	@Autowired
	AlarmPlayer alarmPlayer;
	@Autowired
	private TbPanelContentMapper tbPanelContentMapper;
	@Autowired
	private TbPanelMapper tbPanelMapper;
	@Autowired
	private TbUserGateidMapper tbUserGateidMapper;
	@Autowired
	private TbGroupsforpolicyMapper tbGroupsforpolicyMapper;
	@Autowired
	private TbWeixinMapper tbWeixinMapper;
	@Autowired
	private TbGroupsmemberlogMapper tbGroupsmemberlogMapper;
	@Autowired
	private TbAlltaskitemMapper tbAlltaskitemMapper;
	@Autowired
	private TbAlltasklogMapper tbAlltasklogMapper;
	@Autowired
	private TbSubjectMapper tbSubjectMapper;
	@Autowired
	private TbXgecustomerMapper tbXgecustomerMapper;
	@Autowired
	private TbGroupscategoryMapper groupscategoryMapper;
	@Autowired
	private TbSubjectalarmMapper tbSubjectalarmMapper;
	@Autowired
	private TbAlltaskMapper tbAlltaskMapper;
	@Autowired
	private TbGroupofhavezanMapper tbGroupofhavezanMapper;
	@Autowired
	private TbGroupofhavezanlogMapper tbGroupofhavezanlogMapper;
	@Autowired
	private TbGroupofhaveaddMapper tbGroupofhaveaddMapper;
	@Autowired
	private TbGroupofhaveaddlogMapper tbGroupofhaveaddlogMapper;
	@Autowired
	private TbGroupsMapper tbGroupsMapper;
	@Autowired
	private TbGroupscountMapper tbGroupscountMapper;
	@Autowired
	private TbDestinationMapper tbDestinationMapper;
	@Autowired
	private TbRouteMapper tbRouteMapper;
	@Autowired
	private TbRoleMapper tbRoleMapper;
	@Autowired
	private TbPermissionMapper tbPermissionMapper;
	@Autowired
	private TbNewsMapper tbNewsMapper;
	@Autowired
	private TbRolePermMapper tbRolePermMapper;
	@Autowired
	private TbOrganizationMapper tbOrganizationMapper;
	@Autowired
	private TbItemPriceMapper tbItemPriceMapper;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrganBankCardMapper tbOrganBankCardMapper;
	@Autowired
	private TbUserinfoMapper tbUserinfoMapper;
	@Autowired
	private TbFapiaoMapper tbFapiaoMapper;
	@Autowired
	private TbUsersAccountChangeMapper tbUsersAccountChangeMapper;
	@Autowired
	private TbWalletMapper tbWalletMapper;
	@Autowired
	private TbTraveltypeMapper tbTraveltypeMapper;
	@Autowired
	private TbMemberMapper tbMemberMapper;
	@Autowired
	private TbAlltaskFeedbackMapper tbAlltaskFeedbackMapper;
	@Autowired
	private TbPolicysmemberMapper tbPolicysmemberMapper;
	@Autowired
	private TbUsersAccountChangeTichengMapper tbUsersAccountChangeTichengMapper;
	@Autowired
	private TbRechargeManageMapper tbRechargeManageMapper;
	@Autowired
	private DyLogMapper dyLogMapper;
	@Autowired
	private TbFeedbackSystemMapper tbFeedbackSystemMapper;
	@Autowired
	private TbSummeryMapper tbSummeryMapper;
	@Autowired
	private TbWalletTichengMapper tbWalletTichengMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${NOTIFYURL}")
	private String NOTIFYURL;
	@Value("${RETURN_URL}")
	private String RETURN_URL;
	@Value("${NOTIFY_URL}")
	private String NOTIFY_URL;
	@Value("${MCH_NAME}")
	private String MCH_NAME;
	@Autowired
	private TbDouyinGzMapper tbDouyinGzMapper;
	@Autowired
	private TbDouyinMessageMapper tbDouyinMessageMapper;
	@Autowired
	private Fund1Gaoduanzhuangbei2OkMapper fund1Gaoduanzhuangbei2OkMapper;
	@Autowired
	private FundMapper fundMapper;

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private SubjectsMapper subjectsMapper;

	@Autowired
	private StockMapper stockMapper;

	@Autowired
	private StockDataMapper stockDataMapper;

	@Autowired
	private StockDataHistoryMapper stockDataHistoryMapper;

	@Override
	public Category getCategory(String subjectId) {
		CategoryExample example = new CategoryExample();
		example.createCriteria().andSubjectidEqualTo(Integer.parseInt(subjectId));
		example.setOrderByClause("id desc");
		List<Category> cates = categoryMapper.selectByExample(example);
		if (cates.size() == 0) {
			return null;
		}

		Category category = cates.get(0);
		// 获取所有子类
		setChildren(category);
		// 设置股票

		return category;

	}

	@Override
	public List<StockDataHistory> getHistory(int counts, String stockid) {
		StockDataHistoryExample example = new StockDataHistoryExample();
		example.createCriteria().andStocknumberEqualTo(stockid);
		example.setOrderByClause("trade_date desc limit " + counts);
//		PageHelper.startPage(1, counts);
		List<StockDataHistory> stocks = stockDataHistoryMapper.selectByExample(example);

		Collections.reverse(stocks);

		return stocks;

	}

	public void setChildren(Category subject) {

		// 获取所有子类
		CategoryExample exampleChildren = new CategoryExample();
		exampleChildren.createCriteria().andParentIdEqualTo(subject.getSubjectid());
		exampleChildren.setOrderByClause("id desc");
		List<Category> subjectsChildren = categoryMapper.selectByExample(exampleChildren);
		for (int i = 0; i < subjectsChildren.size(); i++) {

			setChildren(subjectsChildren.get(i));
		}
		subject.setStocks(new ArrayList<Stock>());
		if (subjectsChildren.size() == 0) {
			StockExample stockExample = new StockExample();
			// 为subject设置股票
			stockExample.createCriteria().andSubjectidEqualTo(subject.getSubjectid());
			List<Stock> stocks = stockMapper.selectByExampleWithBLOBs(stockExample);
			subject.setStocks(stocks);

		}
		subject.setChildren(subjectsChildren);
	}

	@Override
	public StockDataWithBLOBs getStockData(String stockid) {

		StockDataWithBLOBs result = stockDataMapper.selectByPrimaryKey(stockid);
		return result;

	}

}
