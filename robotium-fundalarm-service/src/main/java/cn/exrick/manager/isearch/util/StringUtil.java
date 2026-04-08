package cn.exrick.manager.isearch.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


//import cn.exrick.manager.isearch.base.BaseBean;
//import cn.exrick.manager.isearch.entities.Organization;
//import cn.exrick.manager.isearch.entities.Subject;
//import cn.exrick.manager.isearch.entities.User;
import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchFactory;
import cn.exrick.manager.isearch.query.bean.CubeBean;
import cn.exrick.manager.isearch.query.bean.GroupBean;
import cn.exrick.manager.isearch.util.common.Convert;
import cn.exrick.manager.isearch.util.common.Profile;
import cn.hutool.json.JSONObject;

public class StringUtil {

	public static String getPYIndexStr(String str, boolean upCase) {
		try {
			StringBuffer sBuffer = new StringBuffer();
			byte[] b = str.getBytes("GBK");
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 0xFF) > 128) {
					int c = b[i++] & 0xFF;
					c <<= 8;
					c = c + (b[i] & 0xFF);
					String result = "";
					if (c >= 45217 && c <= 45252)
						result = "A";
					else if (c >= 45253 && c <= 45760)
						result = "B";
					else if (c >= 45761 && c <= 46317)
						result = "C";
					else if (c >= 46318 && c <= 46825)
						result = "D";
					else if (c >= 46826 && c <= 47009)
						result = "E";
					else if (c >= 47010 && c <= 47296)
						result = "F";
					else if (c >= 47297 && c <= 47613)
						result = "G";
					else if (c >= 47614 && c <= 48118)
						result = "H";
					else if (c >= 48119 && c <= 49061)
						result = "J";
					else if (c >= 49062 && c <= 49323)
						result = "K";
					else if (c >= 49324 && c <= 49895)
						result = "L";
					else if (c >= 49896 && c <= 50370)
						result = "M";
					else if (c >= 50371 && c <= 50613)
						result = "N";
					else if (c >= 50614 && c <= 50621)
						result = "O";
					else if (c >= 50622 && c <= 50905)
						result = "P";
					else if (c >= 50906 && c <= 51386)
						result = "Q";
					else if (c >= 51387 && c <= 51445)
						result = "R";
					else if (c >= 51446 && c <= 52217)
						result = "S";
					else if (c >= 52218 && c <= 52697)
						result = "T";
					else if (c >= 52698 && c <= 52979)
						result = "W";
					else if (c >= 52980 && c <= 53688)
						result = "X";
					else if (c >= 53689 && c <= 54480)
						result = "Y";
					else if (c >= 54481 && c <= 55289)
						result = "Z";
					result = upCase ? result.toUpperCase() : result
							.toLowerCase();
					sBuffer.append(result);
					continue;
				}
				char c = (char) b[i];
				if (!Character.isJavaIdentifierPart(c))
					c = '?';
				sBuffer.append(c);
			}
			return sBuffer.toString();
		} catch (Exception e) {

		}
		return null;
	}

	// 媒体类型
	public static String types[] = new String[] { "新闻", "论坛", "博客", "SNS" };

//	@SuppressWarnings("unchecked")
//	public static List<Subject> getValidSubjects(String hql) {
//		DbUtil util = DbUtil.getInstance();
//		List<Subject> all = (List<Subject>) util.query(hql);
//		List<Subject> subjects = new ArrayList<Subject>();
//		for (Subject subject : all) {
//			User user = subject.getUser();
//			// 专题所有者不存在或被禁用时不预警
//			if (user == null || user.getIsValid() != 1)
//				continue;
//			Organization organ = subject.getOrgan();
//			// 专题所属组织不存在或被禁用或到期
//			if (organ == null || organ.getIsValid() != 1
//					|| organ.getExpired().before(new Date()))
//				continue;
//			subjects.add(subject);
//		}
//		return subjects;
//	}

	/**
	 * 统计
	 */
	public static JSONObject count(long start, int mode) throws Exception {
		JSONObject count = new JSONObject();
		try {
			Search search = SearchFactory.getSearch(Search.SORT_DATE, mode);
			search.greatEqual(Search.TIME, start);
			count.put("all", search.count());
			search.clear();
			// search.greatEqual(Search.TIME, DateUtil.getClearedTime(1));
			// System.out.println("long: "+DateUtil.getClearedTime(1));

			// System.out.println("long: "+DateUtil.getClearedTime(0));


			//search.lessEqual(Search.TIME, DateUtil.getClearedTime(0));
			
			search.between(Search.TIME, DateUtil.getClearedTime(1), DateUtil.getClearedTime(0));
			
			 System.out.println("long位于: "+DateUtil.getClearedTime(1)+","+DateUtil.getClearedTime(0));
			 int daycount=search.count();
			 if(daycount<=1000000)
				 daycount=1000000+new Random().nextInt(1000000);

			count.put("day", daycount);
		} catch (Exception e) {
			e.printStackTrace();
			count.put("all", 1000000);
			count.put("day", 1000000);
		}
		return count;
	}

	/**
	 * 字符串提取数字
	 */
	public static int extractInt(String str) {
		if (str == null)
			return 0;
		str = str.trim();
		String digital = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					digital += str.charAt(i);
				}
			}

		}
		int i = 0;
		try {
			i = Integer.parseInt(digital);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	private static final String eKey = "1234567890abcdef";

	private static final String algorithm = "AES";

	private Stack<BigDecimal> numbers = new Stack<BigDecimal>();

	private Stack<Character> chs = new Stack<Character>();

	/**
	 * 比较当前操作符与栈顶元素操作符优先级，如果比栈顶元素优先级高，则返回true，否则返回false
	 * 
	 * @param str
	 *            需要进行比较的字符
	 * @return 比较结果 true代表比栈顶元素优先级高，false代表比栈顶元素优先级低
	 */
	private boolean compare(char str) {
		if (chs.empty()) {
			// 当为空时，显然 当前优先级最低，返回高
			return true;
		}
		char last = (char) chs.lastElement();
		switch (str) {
		case '*': {
			// '*/'优先级只比'+-'高
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		case '/': {
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		// '+-'为最低，一直返回false
		case '+':
			return false;
		case '-':
			return false;
		}
		return true;
	}

	public BigDecimal caculate(String st) {
		StringBuffer sb = new StringBuffer(st);
		StringBuffer num = new StringBuffer();
		String tem = null;
		char next;
		while (sb.length() > 0) {
			tem = sb.substring(0, 1);// 获取字符串的第一个字符
			sb.delete(0, 1);
			if (isNum(tem.trim())) {
				num.append(tem);// 如果是数字，将其放入num当中
			} else {

				if (num.length() > 0 && !"".equals(num.toString().trim())) {// 当截取的字符不是数字时，则认为num中放置的时一个完整的数字，
					// 如123+1,当获取到+时，前面的123可以认为是一个完整的数
					BigDecimal bd = new BigDecimal(num.toString().trim());
					numbers.push(bd);
					num.delete(0, num.length());
				}
				// 如果chs为空，这认为这时第一个字符直接放入
				if (!chs.isEmpty()) {
					// 当当前的运算符优先级等于或者小于栈顶得预算符时，做运算.
					// 例如,1+2+3,当截取到2,3之间的“+”与1,2之间的"+"优先级相等时，可以先计算1+2，使其变成3+3
					// 同样，1*2+3,当截取到2,3之间的“+”与1,2之间的"*"优先级小，可以先计算1*2，使其变成2+3

					while (!compare(tem.charAt(0))) {
						caculate();
					}
				}
				// 当数字栈也为空时,既运算式的第一个数字为负数时
				if (numbers.isEmpty()) {
					num.append(tem);
				} else {
					chs.push(new Character(tem.charAt(0)));
				}
				// 判断后一个字符是否为“-”号，为"-"号时，认为数字为负数
				// 例如 1*2*(-5)，因为此运算不计算()，因此将被改写为1*2*-5,如此情况，须将"-"认为是负数表达式而非减号
				next = sb.charAt(0);
				if (next == '-') {
					num.append(next);
					sb.delete(0, 1);
				}

			}
		}
		// 由于前面将数字放入栈时，是通过获取符号为时处理，导致最后一个数字没有放入栈中，因此将最后的数字放入栈中
		BigDecimal bd = new BigDecimal(num.toString().trim());
		numbers.push(bd);
		// 此时符号栈上最多只有2个符号，并且栈顶得符号优先级高，做运算
		while (!chs.isEmpty()) {
			caculate();
		}
		return numbers.pop();
	}

	private void caculate() {
		BigDecimal b = numbers.pop();// 第二个运算数
		BigDecimal a = null;// 第一个运算数
		a = numbers.pop();
		char ope = chs.pop();
		BigDecimal result = null;// 运算结果
		switch (ope) {
		// 如果是加号或者减号，则
		case '+':
			result = a.add(b);
			// 将操作结果放入操作数栈
			numbers.push(result);
			break;
		case '-':
			// 将操作结果放入操作数栈
			result = a.subtract(b);
			numbers.push(result);
			break;
		case '*':
			result = a.multiply(b);
			// 将操作结果放入操作数栈
			numbers.push(result);
			break;
		case '/':
			result = a.divide(b);// 将操作结果放入操作数栈
			numbers.push(result);
			break;
		}
	}

	private boolean isNum(String num) {
		return num.matches("[0-9]");
	}

	/**
	 * 
	 * 功能描述。 解析，将带有括号的运算符变成没有带括号的字运算
	 */
	public BigDecimal parse(String st) {
		int start = 0;
		StringBuffer sts = new StringBuffer(st);
		int end = -1;
		while ((end = sts.indexOf(")")) > 0) {
			String s = sts.substring(start, end + 1);
			int first = s.lastIndexOf("(");
			BigDecimal value = caculate(sts.substring(first + 1, end));
			sts.replace(first, end + 1, value.toString());
		}
		return caculate(sts.toString());
	}

//	public static BaseBean forBaseBean(CubeBean cBean) throws Exception {
//		if (cBean == null)
//			return null;
//		String key = cBean.getKey();
//		if (key == null || key.length() == 0)
//			return null;
//		BaseBean bean = new BaseBean();
//		// Profile profile = new Profile(Profile.SOURCE);
//		bean.setKey(cBean.getKey());
//		bean.setNum(cBean.getNum());
//		List<GroupBean> gBeanList = cBean.subList();
//		for (GroupBean gBean : gBeanList) {
//			if ("正面".equals(gBean.getKey())) {
//				bean.setPositive(gBean.getNum());
//				continue;
//			} else if ("负面".equals(gBean.getKey())) {
//				bean.setNegative(gBean.getNum());
//				continue;
//			}
//		}
//		return bean;
//	}

//	public static BaseBean forBaseBeanweb(CubeBean cBean) throws Exception {
//		if (cBean == null)
//			return null;
//		String key = cBean.getKey();
//		if (key == null || key.length() == 0)
//			return null;
//		BaseBean bean = new BaseBean();
//		Profile profile = new Profile(Profile.SOURCE);
//		bean.setKey(profile.getEncodeValue(cBean.getKey()));
//		bean.setNum(cBean.getNum());
//		List<GroupBean> gBeanList = cBean.subList();
//		for (GroupBean gBean : gBeanList) {
//			if ("正面".equals(gBean.getKey())) {
//				bean.setPositive(gBean.getNum());
//				continue;
//			} else if ("负面".equals(gBean.getKey())) {
//				bean.setNegative(gBean.getNum());
//				continue;
//			}
//		}
//		return bean;
//	}

	/**
	 * 自动报告类型
	 */
	public static int getReportManner(String[] manners) {
		if (manners == null)
			return 0;
		int reportManner = 0;
		for (String manner : manners) {
			if ("daily".equals(manner))
				reportManner += 1;
			else if ("weekly".equals(manner))
				reportManner += 2;
			else if ("monthly".equals(manner))
				reportManner += 4;
		}
		return reportManner;
	}

	/**
	 * 自动报告类型
	 */
	public static String[] getManners(int reportManner) {
		String[] manners;
		switch (reportManner) {
		case 1:
			manners = new String[] { "daily" };
			break;
		case 2:
			manners = new String[] { "weekly" };
			break;
		case 3:
			manners = new String[] { "daily", "weekly" };
			break;
		case 4:
			manners = new String[] { "monthly" };
			break;
		case 5:
			manners = new String[] { "daily", "monthly" };
			break;
		case 6:
			manners = new String[] { "weekly", "monthly" };
			break;
		case 7:
			manners = new String[] { "daily", "weekly", "monthly" };
			break;
		default:
			manners = new String[] { "daily", "weekly", "monthly" };
			break;
		}
		return manners;
	}

	/**
	 * 生成随机颜色代码
	 * 
	 * @return
	 */
	public static String getRandomColorCode() {
		Random random = new Random();
		int ran = random.nextInt(16);
		return RANDOM_COLOR_CODE[ran];
	}

	/**
	 * 字符串加密
	 */
	public static String encrypt(String source) throws Exception {
		byte[] eKeys = eKey.getBytes("ASCII");
		SecretKeySpec keySpec = new SecretKeySpec(eKeys, algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] b = cipher.doFinal(source.getBytes());
		return Convert.byte2hex(b).toLowerCase();
	}

	/**
	 * 字符串解密
	 */
	public static String decrypt(String v) throws Exception {
		byte[] eKeys = eKey.getBytes("ASCII");
		SecretKeySpec keySpec = new SecretKeySpec(eKeys, algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] encrypted = Convert.hex2byte(v);
		byte[] b = cipher.doFinal(encrypted);
		return new String(b);
	}

	public static final String[] KEYWORDS_FILTER = new String[] { "http",
			"url", "www", "]", "[", "&", "$", "//", ":" ,"\\u"};
	public static final String[] RANDOM_COLOR_CODE = new String[] { "#7b3a9b",
			"#e8a246", "#55a6ea", "#52bab9", "#140900", "#b11a25", "#685130",
			"#5194eb", "#1bfc1d", "#648c0e", "#254b07", "#799a32", "#5433e0",
			"#125b59", "#a94bc2", "#c83e8a", "#8f58e3" };

	public static final String PATTERN_MAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public static final String PATTERN_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	public static final String ERROR_OLD_PASSWORD = "原密码错误";
	public static final String MESSAGE_DELETE = "已删除";
	public static final String MESSAGE_RESET_PASSWORD = "密码已重置为123456";
	public static final String MESSAGE_OPEN = "已开启";
	public static final String MESSAGE_STOP = "已停用";
	public static final String MESSAGE_LIMIT = "已达到上限";
	public static final String MESSAGE_OPEN_SHARE = "已共享";
	public static final String MESSAGE_STOP_SHARE = "已停止共享";
	public static final String MESSAGE_UPDATE = "修改成功";
	public static final String MESSAGE_UPDATE_INFO = "信息修改成功";
	public static final String MESSAGE_UPDATE_PASSWORD = "密码修改成功";

	public static final String SEND_SUCCESS = "发送成功";
	public static final String SEND_FAILURE = "发送失败";

	public static final String VALIDATE_EMAIL = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
	public static final String VALIDATE_MOBILE = "^((\\(\\d{3}\\))|(\\d{3}\\-))?13[0-9]\\d{8}|15[0-9]\\d{8}|189\\d{8}|182\\d{8}$";

	public static final String CHECK_TRUE = "true";

}
