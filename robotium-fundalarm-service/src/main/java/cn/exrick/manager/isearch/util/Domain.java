package cn.exrick.manager.isearch.util;

import java.net.URL;
import java.util.StringTokenizer;
public class Domain
{

public String host;

public String domain;


  private static String[] split(String host, String s)
  {
    StringTokenizer st = new StringTokenizer(host, s);
    String[] keys = new String[st.countTokens()];
    int i = 0;
    while (st.hasMoreTokens()) {
      String id = st.nextToken();
      keys[(i++)] = id;
    }

    return keys;
  }







 public static String geturldomain(String url) {
	  String domain2="";
    try {
     String host = new URL(url).getHost();
      domain2 = getDomainFromHost(host);

    }
    catch (Exception e)
    {
		 return null;
    }
	return domain2;
  }


private static String getDomainFromHost(String host)
  {

    if (host == null) return null;

    if (isIP(host))
      return getDmainFromIP(host);
    String[] key = split(host, ".");
    if (key.length == 0) return null;
    if (key.length == 1) return host.toLowerCase();

    int len = key.length;
    String s = key[(len - 1)];
    if (isDomainSuffix(s))
      return (key[(len - 2)] + "." + key[(len - 1)]).toLowerCase();
    s = key[(len - 2)];
    if ((isDomainSuffix(s)) && (len > 2))
      return (key[(len - 3)] + "." + key[(len - 2)] + "." + key[(len - 1)]).toLowerCase();
    return (key[(len - 2)] + "." + key[(len - 1)]).toLowerCase();
  }




  private static final String getDmainFromIP(String host)
  {
    return host;
  }

  private static boolean isIP(String host)
  {
    for (int i = 0; i < host.length(); i++) {
      char c = host.charAt(i);
      if ((c != '.') && ((c < '0') || (c > '9'))) return false;
    }
    return true;
  }




  private static boolean isDomainSuffix(String s)
  {
    s = s.toLowerCase();
    if ("com".equals(s)) return true;
    if ("net".equals(s)) return true;
    if ("gov".equals(s)) return true;
    if ("org".equals(s)) return true;
    if ("cc".equals(s)) return true;
    if ("biz".equals(s)) return true;
    if ("info".equals(s)) return true;
    return "edu".equals(s);
  }


public String spliturl(String urls)
	{


String s2=urls;  
String sarray[]=s2.split("\n"); 
s2=geturldomain(sarray[0]);
System.out.println(s2);
return s2;
	}











public static void main(String []args)
{

 Domain gd=new Domain();

gd.spliturl("www.xfrb.com.cn/npage/newslist.php?t=彩票");


}










}