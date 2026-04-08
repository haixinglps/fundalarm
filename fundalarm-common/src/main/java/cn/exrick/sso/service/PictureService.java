package cn.exrick.sso.service;

public interface PictureService {

	public String createQRcode(String gateId, String filepath);

	String createQRcode2(String url, String filePath);
}
