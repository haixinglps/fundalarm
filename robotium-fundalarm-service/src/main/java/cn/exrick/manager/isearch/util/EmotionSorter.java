package cn.exrick.manager.isearch.util;


import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class EmotionSorter
{
  QMatch qm;
  static final String POSITIVE_LABEL = "正面";
  static final String NEGATIVE_LABEL = "负面";
  public static final int POSITIVE = 1;
  public static final int NEGATIVE = 2;
  public static final int NONE = 0;
  public static final int BOTH = 3;

  public void initailize(String conffilename)
    throws Exception
  {
    this.qm = new QMatch();
    this.qm.loadConfig(conffilename);
  }

  public void clear()
  {
    if (this.qm != null)
      this.qm.clear();
  }

  public int scan(String txt) throws Exception {
    if (txt == null)
      return 0;
    if (txt.length() < 300)
      return scan_short(txt);
    return scan_long(txt);
  }

  public int scan_long(String txt) throws Exception {
    int txtlen = txt.length();
    HashMap rmap = this.qm.scan(txt);
    if (rmap != null)
    {
      HashMap pm = (HashMap)rmap.get("正面");
      int ptotal = 0;
      if (pm != null)
      {
        Iterator it = pm.values().iterator();
        while (it.hasNext())
        {
          int[] v = (int[])it.next();
          ptotal += v[0];
        }
      }
      HashMap nm = (HashMap)rmap.get("负面");
      int ntotal = 0;
      if (nm != null)
      {
        Iterator it = nm.values().iterator();
        while (it.hasNext())
        {
          int[] v = (int[])it.next();
          ntotal += v[0];
        }
      }
      if ((nm != null) && (nm.size() >= 6) && (txtlen / 300 < nm.size()))
      {
        if ((pm == null) || (pm.size() <= nm.size() / 2) || (ptotal < ntotal / 2))
          return 2;
        if (nm.size() > pm.size())
          return 3;
      }
      if ((pm != null) && (pm.size() >= 6) && (txtlen / 300 < pm.size()))
      {
        if ((nm == null) || (nm.size() <= pm.size() / 2) || (ntotal < ptotal / 2))
          return 1;
        return 3;
      }

    }

    return 0;
  }

  public int scan_short(String txt) throws Exception {
    int txtlen = txt.length();
    HashMap rmap = this.qm.scan(txt);
    if (rmap != null)
    {
      HashMap pm = (HashMap)rmap.get("正面");
      int ptotal = 0;
      if (pm != null)
      {
        Iterator it = pm.values().iterator();
        while (it.hasNext())
        {
          int[] v = (int[])it.next();
          ptotal += v[0];
        }
      }
      HashMap nm = (HashMap)rmap.get("负面");
      int ntotal = 0;
      if (nm != null)
      {
        Iterator it = nm.values().iterator();
        while (it.hasNext())
        {
          int[] v = (int[])it.next();
          ntotal += v[0];
        }
      }
      if ((nm != null) && (nm.size() >= 3) && (txtlen / 300 < nm.size()))
      {
        if ((pm == null) || (pm.size() <= nm.size() / 2) || (ptotal < ntotal / 2))
          return 2;
        if (nm.size() > pm.size())
          return 3;
      }
      if ((pm != null) && (pm.size() >= 3) && (txtlen / 300 < pm.size()))
      {
        if ((nm == null) || (nm.size() <= pm.size() / 2) || (ntotal < ptotal / 2))
          return 1;
        return 3;
      }

    }

    return 0;
  }

  public static void main(String[] argv) throws Exception {
    String conf_file = "conf\\step4.emotion_keywords.2.txt";
    EmotionSorter sorter = new EmotionSorter();
    sorter.initailize(conf_file);

    String txt = "《学生营养餐既然有人暗箱操作，只手遮天！》学生营养餐是什么？听说瑞金市的学生营养餐招标暗箱操作，招标公司明显的违反招标文件，违规招标，对反映的问题采用糊弄，应付的方式。有关部门得到举报也不进行处理。在当前全面反腐的形势之下，难道瑞金不是共产...畅读版【http://t.cn/8DDOEO5】";

    if (argv.length > 0)
    {
      txt = argv[0];
    }
    System.out.println(txt);
    int s = sorter.scan(txt);
    switch (s) {
    case 0:
      System.out.println("NONE"); break;
    case 1:
      System.out.println("POSITIVE"); break;
    case 2:
      System.out.println("NEGATIVE"); break;
    case 3:
      System.out.println("BOTH");
    }
  }
  
  
  
  
  public static String getemotion(String argv) throws Exception {
	  ClassLoader cl = EmotionSorter.class.getClassLoader();  
	  String conf_file = cl.getResource("step4.emotion_keywords.2.txt").toURI().getPath();
	  //  String conf_file = "conf\\step4.emotion_keywords.2.txt";
	    EmotionSorter sorter = new EmotionSorter();
	    sorter.initailize(conf_file);

	    String txt = "《学生营养餐既然有人暗箱操作，只手遮天！》学生营养餐是什么？听说瑞金市的学生营养餐招标暗箱操作，招标公司明显的违反招标文件，违规招标，对反映的问题采用糊弄，应付的方式。有关部门得到举报也不进行处理。在当前全面反腐的形势之下，难道瑞金不是共产...畅读版【http://t.cn/8DDOEO5】";

	   txt=argv;
	    System.out.println(txt);
	    int s = sorter.scan(txt);
	    switch (s) {
	    case 0:
	    return "无正负";
	    case 1:
	     return "正面";
	    case 2:
	     return "负面";
	    case 3:
	      return "有正有负";
	    }
	    return "正面";
	  }
  
  
  
  
  
  
  
  
  
  
  
  
}