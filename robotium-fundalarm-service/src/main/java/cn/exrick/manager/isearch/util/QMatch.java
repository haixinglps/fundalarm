package cn.exrick.manager.isearch.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class QMatch
{
  boolean hasSingleWord = false;

  Hashtable<String, String[]> table = new Hashtable();
  Hashtable<String, String> table_single = new Hashtable();

  public void clear()
  {
    this.table.clear();
    this.table_single.clear();
  }
  public static String[] split(String str, String s) {
	    if (str == null) return null;

	    if (s == null) return new String[] { str };

	    StringTokenizer st = new StringTokenizer(str, s);
	    String[] r = new String[st.countTokens()];
	    int i = 0;
	    while (st.hasMoreTokens()) {
	      r[(i++)] = st.nextToken();
	    }
	    return r;
	  }
  public void loadConfig(String cfn) throws Exception {
    File f = new File(cfn);
    if (!f.exists()) throw new Exception("file " + cfn + " does not exist");
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "GBK"));
    String line = in.readLine();
    while (line != null)
    {
      String[] ss = split(line, "=");
      ss[0] = ss[0].trim();
      ss[1] = ss[1].trim();
      if (ss[0].length() == 1)
      {
        this.table_single.put(ss[0], ss[1]);
      }
      else
      {
        String k = ss[0].substring(0, 2);
        if (this.table.get(k) != null)
        {
          String[] vs = (String[])this.table.get(k);
          int vl = vs.length;
          String[] nvs = new String[vl + 2];
          int i = 0;
          for (; i < vl / 2; i++)
          {
            if (vs[(2 * i)].length() <= ss[0].length()) break;
            nvs[(2 * i)] = vs[(2 * i)];
            nvs[(2 * i + 1)] = vs[(2 * i + 1)];
          }

          nvs[(2 * i)] = ss[0];
          nvs[(2 * i + 1)] = ss[1];
          for (; i < vl / 2; i++)
          {
            nvs[(2 * i + 2)] = vs[(2 * i)];
            nvs[(2 * i + 3)] = vs[(2 * i + 1)];
          }

          this.table.put(k, nvs);
        }
        else
        {
          this.table.put(k, ss);
        }
      }

      line = in.readLine();
    }
    in.close();
    if (this.table_single.size() > 0)
      this.hasSingleWord = true;
  }

  public String match(String txt)
  {
    if (txt == null) return null;

    StringBuffer strb = new StringBuffer();

    int num = 0;

    HashMap map = new HashMap();
    for (int i = 0; i < txt.length() - 1; i++)
    {
      String s = txt.substring(i, i + 2);
      String[] vs = (String[])this.table.get(s);
      if (vs == null)
        continue;
      for (int j = 0; j < vs.length / 2; j++)
      {
        int onelen = vs[(j << 1)].length();
        if (onelen == 2) {
          if (map.get(vs[((j << 1) + 1)]) != null)
            continue;
          if (num != 0)
            strb.append(",");
          strb.append(vs[((j << 1) + 1)]);
          map.put(vs[((j << 1) + 1)], vs[((j << 1) + 1)]);
          num++;
        }
        else
        {
          if (i + onelen > txt.length())
            continue;
          boolean found = true;
          for (int k = 2; k < onelen; k++) {
            if (txt.charAt(k + i) == vs[(j << 1)].charAt(k))
              continue;
            found = false;
            break;
          }
          if (!found)
            continue;
          if (map.get(vs[((j << 1) + 1)]) != null)
            break;
          if (num != 0)
            strb.append(",");
          strb.append(vs[((j << 1) + 1)]);
          map.put(vs[((j << 1) + 1)], vs[((j << 1) + 1)]);
          num++;

          break;
        }

      }

    }

    if (this.hasSingleWord) {
      for (int i = 0; i < txt.length(); i++)
      {
        String s = txt.substring(i, i + 1);
        String vs = (String)this.table_single.get(s);
        if (vs == null)
          continue;
        if (map.get(vs) != null)
          continue;
        if (num != 0)
          strb.append(",");
        strb.append(vs);
        map.put(vs, vs);
        num++;
      }

    }

    if (num == 0) {
      return null;
    }
    return strb.toString();
  }

  public HashMap scan(String txt)
  {
    if (txt == null) return null;

    HashMap map = new HashMap();

    for (int i = 0; i < txt.length() - 1; i++)
    {
      String s = txt.substring(i, i + 2);
      String[] vs = (String[])this.table.get(s);
      if (vs == null)
        continue;
      for (int j = 0; j < vs.length / 2; j++)
      {
        int onelen = vs[(j << 1)].length();
        if (onelen == 2)
        {
          putToMap(map, vs[(j << 1)], vs[((j << 1) + 1)]);
        }
        else
        {
          if (i + onelen > txt.length())
            continue;
          boolean found = true;
          for (int k = 2; k < onelen; k++) {
            if (txt.charAt(k + i) == vs[(j << 1)].charAt(k))
              continue;
            found = false;
            break;
          }
          if (!found)
            continue;
          putToMap(map, vs[(j << 1)], vs[((j << 1) + 1)]);
          break;
        }

      }

    }

    if (this.hasSingleWord) {
      for (int i = 0; i < txt.length(); i++)
      {
        String s = txt.substring(i, i + 1);
        String vs = (String)this.table_single.get(s);
        if (vs == null)
          continue;
        putToMap(map, s, vs);
      }
    }

    return map;
  }

  private void putToMap(HashMap map, String keyword, String category)
  {
    HashMap m = (HashMap)map.get(category);
    if (m == null)
    {
      m = new HashMap();
      int[] v = new int[1];
      v[0] = 1;
      m.put(keyword, v);

      map.put(category, m);
      return;
    }

    int[] v = (int[])m.get(keyword);
    if (v == null)
    {
      v = new int[1];
      v[0] = 1;
      m.put(keyword, v);
      return;
    }

    v[0] += 1;
  }

  public static void main(String[] argv) throws Exception {
    QMatch qm = new QMatch();
    qm.loadConfig("conf\\location_keywords.txt");
    String r = qm.match("在拉斯维加斯的第二天，咱去了可口可乐公司。点了18杯全世界不同口味的可口可乐。那味道真的[生病][吐] 后来@笨少爺_好衰 说要剪刀石头布，赢的就要选一杯给输的。。@Loli昕 一直没输，所以在最后咱给她把最后两杯搅在一起让她喝了[偷笑] 我输了4次，喝了杯漱口水味道的可乐[吐][生病]   #Weico拼图#");
    System.out.println(r);
    System.out.println(qm.scan("阿拉善=内蒙古自治区:阿拉善盟铁山港=广西壮族自治区:北海市"));
  }
}