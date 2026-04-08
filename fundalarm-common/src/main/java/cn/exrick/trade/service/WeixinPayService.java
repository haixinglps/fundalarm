package cn.exrick.trade.service;

import java.util.Map;
import java.util.SortedMap;

public interface WeixinPayService {

	String weixinPay(String mch_name, String price, String tradeNo,
			String terminalip, String notifyurl, String attach)
			throws Exception;

	boolean isTenpaySign(SortedMap<Object, Object> packageParams);

	Map<String, String> doXMLParse(String sb) throws Exception;

	String weixinPayjsapi(String openid, String mch_name, String price,
			String tradeNo, String terminalip, String notifyurl, String attach)
			throws Exception;

	String weixinPayapp(String mch_name, String price, String tradeNo,
			String terminalip, String notifyurl, String attach)
			throws Exception;

	String weixinPayNative(String mCH_NAME, String string, String string2, String terminalip, String nOTIFYURL,
			String string3);

	

	String weixintransfer(String price, String tradeNo, String terminalip,
			String openid) throws Exception;

 
	String tuikuan(String price, String tradeNo, int gzhindex) throws Exception;

}
