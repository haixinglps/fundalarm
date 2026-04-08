package cn.exrick.trade.service;

import cn.exrick.common.jedis.JedisClient;
 

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Exrickx
 */

public interface MobileService  {
	
	


String sendmsgofyoujia(String phone, String code);

//String sendDiscount(String phone, String code);

String sendDiscount(String phone, String username, String code,
		String sourcephone);

String sendpolicyofyoujia(String phone, String starttime, String endtime);
String sendpolicyofyoujiav2(String phone, String organname,int routeid);
String sendaccountofyoujia(String phone, String username, String password);
 
	
	
}
