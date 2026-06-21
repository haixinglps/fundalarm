package scripts;

import cn.exrick.manager.isearch.Isearch;
import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.bean.SearchBean;
import cn.exrick.manager.isearch.query.bean.SearchBeans;

/**
 * Isearch 命令行搜索工具
 * 
 * 用法:
 *   javac -cp "${CP}" scripts/SearchTest.java
 *   java  -cp "${CP}" scripts.SearchTest <关键词> [最大结果数]
 * 
 * 示例:
 *   java -cp "${CP}" scripts.SearchTest "芃芃 腋下" 50
 */
public class SearchTest {
    public static void main(String[] args) throws Exception {
        String keyword = args.length > 0 ? args[0] : "测试";
        int maxResults = args.length > 1 ? Integer.parseInt(args[1]) : 100;
        
        System.out.println("搜索关键词: " + keyword);
        System.out.println("最大结果数: " + maxResults);
        System.out.println("---");
        
        Isearch.init();
        
        Isearch search = new Isearch();
        search.andText("TX", keyword);
        search.setMaxResults(maxResults);
        
        SearchBeans result = search.query();
        System.out.println("总命中数: " + result.getTotal());
        System.out.println("返回结果数: " + result.list().size());
        System.out.println("---");
        
        int count = 0;
        for (SearchBean bean : result.list()) {
            count++;
            System.out.println("[" + count + "] ID: " + bean.getId());
            System.out.println("    标题: " + bean.getTi());
            System.out.println("    作者: " + bean.getAu());
            System.out.println("    链接: " + bean.getUr());
            System.out.println("    时间: " + bean.getPubdate2());
            System.out.println("    频道: " + bean.getCh());
            System.out.println("    摘要: " + bean.getSm());
            System.out.println();
        }
    }
}
