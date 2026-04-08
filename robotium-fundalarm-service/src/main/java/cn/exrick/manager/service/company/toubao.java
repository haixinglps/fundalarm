package cn.exrick.manager.service.company;

import java.io.IOException;
import java.util.UUID;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class toubao {

	public static void main(String[] args) {

		// JSONObject toubaoRes = toubao();

		// 解密：
		String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String res = "w7mA4Uef+cw694lmslu71E\\/OxKZSzbSYrqntJccO5Ol89jS6lrDO0Rj6pdRFL5iZ1dFfQrVROOUcrQ5qH4loVsRXd2RGqylmKNJlKvADLDqOsYg388ddyfSNZcIfaaNlFfXsYbY7dQ7PP1TlZVVurA==";
		res = res.replace("\\/", "/");
		// System.out.println("base64:" + res);
		String resData = CryptUtil.decrypt(res, aeskey);//
		// System.out.println("响应明文：" + resData);

//		System.out.println("原始请求：" + CryptUtil.decrypt(
//				"RiAG0MRAHEXpcPsx9RPjEJ5wemLiKLozgBwsI5eBXoiAkApFl8p9jAwySVgCocN+3rPw4DWQG8MWs7RDfaGLBoKdhqIU2G2aA9ZCiqXkPQkTEgyii+Nay74wqqZW7BlTc78KDfUuz3QgHvwatL105FRunlpoUkfXcivZqDsoOhzZzWSpmlrNsRbVpM07K+FsHk5j7CoDCJBhlRwwIufG1RhwZkrlVx16GmzbOrpKEFaB8pyQwu2GZt4kUzfs2OErq41noFE57Hnd1h5j24QvwRWj2+rUYcXZAmQ0i5cbIkN+eCnkkHRdUKmnC53BHD1w2Zpxvmd2oJwyCuSIPEuE2bpjcHw47AS8l/Pu2HHBP01gilyWVEC90b4FV0QO8219RfaRwxyip5U3T6t44DuxEP3muElWM5FoRrWdd4V2zDNElQYfQG4BnLcD39Zvdo/ZXq1QLZ7v15/a2ECykBG48Hu62xNd0CEqoeUx1zYVrMyJqUvlUjDcy1lu/emItzgLMsJpGOlAoQD98OkEfVKaCdJcl8yarDUK8As72ijrx78=",
//				aeskey));

		queryPolicy("18478_" + "L202602061944651-20260207-1");
//		getPolicyPdf("13201206600955601651");

		// tuibao("13201206601105158101" + "");
//		toubao();
	}

	public static JSONObject toubao() {

		String urlString = "https://cct.rujian.vip/cct/open/api/ins/order";
		String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String signkey = "f0ec89c4659f344bbfeb9cc401c86f4c";

		JSONArray insured_person_list = new JSONArray();
		JSONObject person = new JSONObject();
		person.put("name", "张加良");
		person.put("pid_type", 1);
		person.put("pid", "411221199506145727");
		person.put("phone", "15000000000");
		JSONObject scoolType = new JSONObject();
		scoolType.put("school_type", "学校类型--学平险必填");
		// person.put("extra_data", scoolType);
		insured_person_list.add(person);
		JSONObject policy_holder = new JSONObject();
		policy_holder.put("name", "上海腾达保消防科技有限公司");
		policy_holder.put("pid_type", 12);
		policy_holder.put("pid", "91320602MAE02QKF4A");
		policy_holder.put("phone", null);

		JSONObject reqData = new JSONObject();
//	    "apply_order_no": "20250410000000002",
//	    "product_code": "MP02320437",
//	    "plan_no": "PK00145828",
//	    "ins_begin_date": "2025-04-12",
//	    "ins_end_date": "2025-07-11",
//	    "total_premium": 398,
		reqData.put("apply_order_no", UUID.randomUUID().toString());
		reqData.put("product_code", "MP03320964");
		reqData.put("plan_no", "PK00131133");
		reqData.put("ins_begin_date", "2025-09-15 00:00:00");
		reqData.put("ins_end_date", "2025-09-15 23:59:59");
		double payment = insured_person_list.size() * 0.9;
		reqData.put("total_premium", payment);
		reqData.put("policy_holder", policy_holder);
		reqData.put("insured_person_list", insured_person_list);
		String reqDataString = reqData.toString();
		System.out.println(reqDataString);
		JSONObject requestBody = new JSONObject();

//	    "appId": "A1744276148",
//	    "method": "order.query",
//	    "sign": "0e905db3da95735427cfd5440e260235dcce1b161b8c3e32ec8f57702d1b55b3",
//	    "timestamp": "20250415145621",
//	    "data": "ZhXP1opjiVMF13F5rgvEfHR7SmgQmdBkT68dCMQaI9sE60U6N5rvw6DKe3yTDXEO"
		requestBody.put("appId", "A1752218641");
		requestBody.put("method", "order.accept");

		requestBody.put("timestamp", "20250415145621296");

		String aesData = CryptUtil.encrypt(reqDataString, aeskey);
		requestBody.put("data", aesData);
		String signBeforeString = requestBody.getStr("appId") + requestBody.getStr("method")
				+ requestBody.getStr("data") + requestBody.getStr("timestamp") + signkey;

		// appId+method+data+timestamp+signKey;
		String sign = CryptUtil.sha256(signBeforeString);
		requestBody.put("sign", sign);

		String body = requestBody.toString();
		System.out.println(body);

		String res = HttpUtil.post(urlString, body, 10000);
		System.out.println(res);

		JSONObject resDataJsonObject = new JSONObject(res);
		System.err.println("code:" + resDataJsonObject.getStr("code"));
		return resDataJsonObject;

	}

	public static void tuibao(String policy_no) {

		String urlString = "https://cct.rujian.vip/cct/open/api/ins/order";
		String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String signkey = "f0ec89c4659f344bbfeb9cc401c86f4c";

		JSONObject reqData = new JSONObject();
//	    "apply_order_no": "20250410000000002",
//	    "product_code": "MP02320437",
//	    "plan_no": "PK00145828",
//	    "ins_begin_date": "2025-04-12",
//	    "ins_end_date": "2025-07-11",
//	    "total_premium": 398,
		reqData.put("policy_no", policy_no);

		String reqDataString = reqData.toString();
		System.out.println(reqDataString);
		JSONObject requestBody = new JSONObject();

//	    "appId": "A1744276148",
//	    "method": "order.query",
//	    "sign": "0e905db3da95735427cfd5440e260235dcce1b161b8c3e32ec8f57702d1b55b3",
//	    "timestamp": "20250415145621",
//	    "data": "ZhXP1opjiVMF13F5rgvEfHR7SmgQmdBkT68dCMQaI9sE60U6N5rvw6DKe3yTDXEO"
		requestBody.put("appId", "A1752218641");
		requestBody.put("method", "order.cancel");

		requestBody.put("timestamp", "20250415145621296");

		String aesData = CryptUtil.encrypt(reqDataString, aeskey);
		requestBody.put("data", aesData);
		String signBeforeString = requestBody.getStr("appId") + requestBody.getStr("method")
				+ requestBody.getStr("data") + requestBody.getStr("timestamp") + signkey;

		// appId+method+data+timestamp+signKey;
		String sign = CryptUtil.sha256(signBeforeString);
		requestBody.put("sign", sign);

		String body = requestBody.toString();
		System.out.println(body);

		String res = HttpUtil.post(urlString, body, 10000);
		System.out.println(res);

		JSONObject resDataJsonObject = new JSONObject(res);
		System.err.println("code:" + resDataJsonObject.getStr("code"));

		// 解密：
		// String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String res2 = resDataJsonObject.getStr("data");
		// "w7mA4Uef+cw694lmslu71E\\/OxKZSzbSYrqntJccO5Ol89jS6lrDO0Rj6pdRFL5iZ1dFfQrVROOUcrQ5qH4loVsRXd2RGqylmKNJlKvADLDqOsYg388ddyfSNZcIfaaNlFfXsYbY7dQ7PP1TlZVVurA==";
		res2 = res2.replace("\\/", "/");
		System.out.println("base64:" + res2);
		String resData = CryptUtil.decrypt(res2, aeskey);//
		System.out.println("响应明文：" + resData);

	}

	public static void queryPolicy(String orderNo) {

		String urlString = "https://cct.rujian.vip/cct/open/api/ins/order";
		String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String signkey = "f0ec89c4659f344bbfeb9cc401c86f4c";

		JSONObject reqData = new JSONObject();
//	    "apply_order_no": "20250410000000002",
//	    "product_code": "MP02320437",
//	    "plan_no": "PK00145828",
//	    "ins_begin_date": "2025-04-12",
//	    "ins_end_date": "2025-07-11",
//	    "total_premium": 398,
		reqData.put("apply_order_no", orderNo);

		String reqDataString = reqData.toString();
		System.out.println(reqDataString);
		JSONObject requestBody = new JSONObject();

//	    "appId": "A1744276148",
//	    "method": "order.query",
//	    "sign": "0e905db3da95735427cfd5440e260235dcce1b161b8c3e32ec8f57702d1b55b3",
//	    "timestamp": "20250415145621",
//	    "data": "ZhXP1opjiVMF13F5rgvEfHR7SmgQmdBkT68dCMQaI9sE60U6N5rvw6DKe3yTDXEO"
		requestBody.put("appId", "A1752218641");
		requestBody.put("method", "order.query");

		requestBody.put("timestamp", "20250415145621296");

		String aesData = CryptUtil.encrypt(reqDataString, aeskey);
		requestBody.put("data", aesData);
		String signBeforeString = requestBody.getStr("appId") + requestBody.getStr("method")
				+ requestBody.getStr("data") + requestBody.getStr("timestamp") + signkey;

		// appId+method+data+timestamp+signKey;
		String sign = CryptUtil.sha256(signBeforeString);
		requestBody.put("sign", sign);

		String body = requestBody.toString();
		System.out.println(body);

		String res = HttpUtil.post(urlString, body, 10000);
		System.out.println(res);

		JSONObject resDataJsonObject = new JSONObject(res);
		System.err.println("code:" + CryptUtil.decrypt(resDataJsonObject.getStr("data"), aeskey));

	}

	public static void getPolicyPdf(String policyNo) {

		String urlString = "https://cct.rujian.vip/cct/open/api/ins/order";
		String aeskey = "d2df4fe22f5053846cf4049b9198b1cd";
		String signkey = "f0ec89c4659f344bbfeb9cc401c86f4c";

		JSONObject reqData = new JSONObject();
//	    "apply_order_no": "20250410000000002",
//	    "product_code": "MP02320437",
//	    "plan_no": "PK00145828",
//	    "ins_begin_date": "2025-04-12",
//	    "ins_end_date": "2025-07-11",
//	    "total_premium": 398,
		reqData.put("policy_no", policyNo);

		String reqDataString = reqData.toString();
//		System.out.println(reqDataString);
		JSONObject requestBody = new JSONObject();

//	    "appId": "A1744276148",
//	    "method": "order.query",
//	    "sign": "0e905db3da95735427cfd5440e260235dcce1b161b8c3e32ec8f57702d1b55b3",
//	    "timestamp": "20250415145621",
//	    "data": "ZhXP1opjiVMF13F5rgvEfHR7SmgQmdBkT68dCMQaI9sE60U6N5rvw6DKe3yTDXEO"
		requestBody.put("appId", "A1752218641");
		requestBody.put("method", "get.e.policy");

		requestBody.put("timestamp", "20250415145621296");

		String aesData = CryptUtil.encrypt(reqDataString, aeskey);
		requestBody.put("data", aesData);
		String signBeforeString = requestBody.getStr("appId") + requestBody.getStr("method")
				+ requestBody.getStr("data") + requestBody.getStr("timestamp") + signkey;

		// appId+method+data+timestamp+signKey;
		String sign = CryptUtil.sha256(signBeforeString);
		requestBody.put("sign", sign);

		String body = requestBody.toString();
//		System.out.println(body);

		String res = HttpUtil.post(urlString, body, 60000);
//		System.out.println(res);

		JSONObject resDataJsonObject = new JSONObject(res);
//		System.err.println("code:" + CryptUtil.decrypt(resDataJsonObject.getStr("data"), aeskey));

		if (resDataJsonObject.getStr("code").contentEquals("0000")) {

			String data = resDataJsonObject.getStr("data");
			data = data.replace("\\/", "/");
			String resData = CryptUtil.decrypt(data, aeskey);//
			System.out.println("响应明文：" + resData);
			JSONObject dataJson = new JSONObject(resData);

			String pdf = dataJson.getStr("pdf_value");
			String name = policyNo + "_hnpa" + ".pdf";
			String filePath = "d:/" + name;// "/home/www/apache-tomcat-8.5.31_gzzrx/webapps/ROOT/static/qishou/" + name;

			try {
				Base64ToPdfConverter.convertWithValidation(pdf, filePath);
				// System.out.println(filePath);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			System.out.println("/home/www/apache-tomcat-8.5.31_gzzrx/webapps/ROOT/static/qishou/");
			String pdfpathweb = "https://shop.bjhfbx.cn/qishou/api/static/qishou";
			System.out.println("null," + policyNo + "," + (pdfpathweb + "/" + name));
			System.out.println(pdfpathweb + "/" + name);
			// 继续通过保单号提取电子保单

		} else {

		}

	}
}
