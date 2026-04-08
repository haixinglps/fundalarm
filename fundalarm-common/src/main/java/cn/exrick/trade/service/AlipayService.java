package cn.exrick.trade.service;

public interface AlipayService {

	String post(String WIDout_trade_no, String WIDtotal_amount,
			String WIDsubject, String WIDbody, String RETURN_URL,
			String NOTIFY_URL);

	String getgateway();

	String getprivatekey();

	String getcharset();

	String getsigntype();
	String getsigntype2();
	String publickey();

	String getappid();

	String post2(String WIDout_trade_no, String WIDtotal_amount,
			String WIDsubject, String WIDbody, String RETURN_URL,
			String NOTIFY_URL);

	String getOpenidByCodeofzfb(String code);

	String getuserinfo(String accessToken);

	String post3(String string, String string2, String string3, String string4,
			String redirect, String nOTIFY_URL);

	String postpcwebyoujia(String WIDout_trade_no, String WIDtotal_amount, String WIDsubject, String WIDbody,
			String RETURN_URL, String NOTIFY_URL);

	String publickey2();
}
