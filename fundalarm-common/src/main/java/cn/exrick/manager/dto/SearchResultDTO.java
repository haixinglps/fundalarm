package cn.exrick.manager.dto;

import java.io.Serializable;
import java.util.List;

import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.ZmqVideo;

/**
 * 搜索结果 DTO
 * 用于 Telegram 和 QQ Bot 共用查询结果
 */
public class SearchResultDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 各库搜索结果
    private List<ZmqVideo> zmqVideos;        // 网页搜索 (zmq)
    private List<Waiwang2Video> waiwang2Videos;  // 最新作品 (bc)
    private List<WanwuVideo> wanwuVideos;    // 玩物 (ww)
    private List<Taolu3Video> taolu3Videos;  // 淘露 (tl)
    private List<WaiwangVideo> waiwangVideos; // 电报 (tg)
    
    // 统计信息
    private long zmqTotal;
    private long waiwang2Total;
    private long wanwuTotal;
    private long taolu3Total;
    private long waiwangTotal;
    
    // 搜索关键词
    private String keyword;
    
    // 构造方法
    public SearchResultDTO() {}
    
    public SearchResultDTO(String keyword) {
        this.keyword = keyword;
    }
    
    // Getters and Setters
    public List<ZmqVideo> getZmqVideos() { return zmqVideos; }
    public void setZmqVideos(List<ZmqVideo> zmqVideos) { 
        this.zmqVideos = zmqVideos;
        this.zmqTotal = zmqVideos != null ? zmqVideos.size() : 0;
    }
    
    public List<Waiwang2Video> getWaiwang2Videos() { return waiwang2Videos; }
    public void setWaiwang2Videos(List<Waiwang2Video> waiwang2Videos) { 
        this.waiwang2Videos = waiwang2Videos;
        this.waiwang2Total = waiwang2Videos != null ? waiwang2Videos.size() : 0;
    }
    
    public List<WanwuVideo> getWanwuVideos() { return wanwuVideos; }
    public void setWanwuVideos(List<WanwuVideo> wanwuVideos) { 
        this.wanwuVideos = wanwuVideos;
        this.wanwuTotal = wanwuVideos != null ? wanwuVideos.size() : 0;
    }
    
    public List<Taolu3Video> getTaolu3Videos() { return taolu3Videos; }
    public void setTaolu3Videos(List<Taolu3Video> taolu3Videos) { 
        this.taolu3Videos = taolu3Videos;
        this.taolu3Total = taolu3Videos != null ? taolu3Videos.size() : 0;
    }
    
    public List<WaiwangVideo> getWaiwangVideos() { return waiwangVideos; }
    public void setWaiwangVideos(List<WaiwangVideo> waiwangVideos) { 
        this.waiwangVideos = waiwangVideos;
        this.waiwangTotal = waiwangVideos != null ? waiwangVideos.size() : 0;
    }
    
    public long getZmqTotal() { return zmqTotal; }
    public void setZmqTotal(long zmqTotal) { this.zmqTotal = zmqTotal; }
    
    public long getWaiwang2Total() { return waiwang2Total; }
    public void setWaiwang2Total(long waiwang2Total) { this.waiwang2Total = waiwang2Total; }
    
    public long getWanwuTotal() { return wanwuTotal; }
    public void setWanwuTotal(long wanwuTotal) { this.wanwuTotal = wanwuTotal; }
    
    public long getTaolu3Total() { return taolu3Total; }
    public void setTaolu3Total(long taolu3Total) { this.taolu3Total = taolu3Total; }
    
    public long getWaiwangTotal() { return waiwangTotal; }
    public void setWaiwangTotal(long waiwangTotal) { this.waiwangTotal = waiwangTotal; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    /**
     * 获取总数量
     */
    public long getTotalCount() {
        return zmqTotal + waiwang2Total + wanwuTotal + taolu3Total + waiwangTotal;
    }
    
    /**
     * 是否有结果
     */
    public boolean hasResults() {
        return getTotalCount() > 0;
    }
    
    @Override
    public String toString() {
        return String.format("SearchResultDTO[keyword=%s, zmq=%d, bc=%d, ww=%d, tl=%d, tg=%d]", 
            keyword, zmqTotal, waiwang2Total, wanwuTotal, taolu3Total, waiwangTotal);
    }
}
