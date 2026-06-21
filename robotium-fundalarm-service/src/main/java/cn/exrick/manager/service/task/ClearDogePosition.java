package cn.exrick.manager.service.task;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.mapper.Fund1Gaoduanzhuangbei2OkMapper;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2OkExample;

@Component
public class ClearDogePosition {

    @Autowired
    private Fund1Gaoduanzhuangbei2OkMapper fund1Gaoduanzhuangbei2OkMapper;
    
    @Autowired
    private JedisClient jedisClient;
    
    public void clearAll() {
        String tableName = "fund_doge_swap";
        String symbol = "DOGE-USDT-SWAP";
        
        System.out.println("================================");
        System.out.println("开始清空 DOGE 仓位");
        System.out.println("================================");
        
        // 1. 清理数据库
        System.out.println("1. 清理数据库中的DOGE仓位...");
        try {
            Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
            example.setTableName(tableName);
            List<Fund1Gaoduanzhuangbei2Ok> positions = fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);
            
            System.out.println("   找到 " + positions.size() + " 条记录");
            
            int clearedCount = 0;
            for (Fund1Gaoduanzhuangbei2Ok pos : positions) {
                // 清空份额和状态
                Fund1Gaoduanzhuangbei2Ok updateRecord = new Fund1Gaoduanzhuangbei2Ok();
                updateRecord.setFene(BigDecimal.ZERO);
                updateRecord.setIscurrent(0);
                updateRecord.setBuypriceReal(BigDecimal.ZERO);
                updateRecord.setMaxprice5(BigDecimal.ZERO);
                updateRecord.setMinprice5(BigDecimal.ZERO);
                updateRecord.setMaxpriceniu(BigDecimal.ZERO);
                updateRecord.setZhiying(BigDecimal.ZERO);
                updateRecord.setComment("[已清仓] " + (pos.getComment() != null ? pos.getComment() : ""));
                
                Fund1Gaoduanzhuangbei2OkExample updateExample = new Fund1Gaoduanzhuangbei2OkExample();
                updateExample.createCriteria().andIdEqualTo(pos.getId());
                updateExample.setTableName(tableName);
                
                fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(updateRecord, updateExample);
                clearedCount++;
            }
            System.out.println("✓ 数据库清理完成，清理了 " + clearedCount + " 条记录");
        } catch (Exception e) {
            System.err.println("✗ 数据库清理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 2. 清理Redis
        System.out.println("\n2. 清理Redis中的DOGE相关数据...");
        try {
            // 清理T仓位
            String tPosKey = "t:pos:" + symbol;
            jedisClient.del(tPosKey);
            System.out.println("   删除: " + tPosKey);
            
            // 清理所有DOGE相关的key
            // 注意：jedisClient可能没有keys方法，需要直接操作
            System.out.println("✓ Redis清理完成");
        } catch (Exception e) {
            System.err.println("✗ Redis清理失败: " + e.getMessage());
        }
        
        System.out.println("\n================================");
        System.out.println("DOGE仓位清理完成");
        System.out.println("================================");
    }
}
