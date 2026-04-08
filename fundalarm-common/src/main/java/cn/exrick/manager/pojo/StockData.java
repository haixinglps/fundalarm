package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockData implements Serializable {
    @ApiModelProperty(value = "股票代码")
    private String stockid;

    private BigDecimal zhangfu;

    @ApiModelProperty(value = "股票名称")
    private String name;

    @ApiModelProperty(value = "市值")
    private BigDecimal marketvalue;

    @ApiModelProperty(value = "交易日期")
    private Date tradedate;

    @ApiModelProperty(value = "当前价格")
    private BigDecimal current;

    @ApiModelProperty(value = "开盘价")
    private BigDecimal open;

    @ApiModelProperty(value = "最高价")
    private BigDecimal high;

    @ApiModelProperty(value = "最低价")
    private BigDecimal low;

    @ApiModelProperty(value = "收盘价")
    private BigDecimal close;

    @ApiModelProperty(value = "前收盘价")
    private BigDecimal preclose;

    @ApiModelProperty(value = "涨跌幅百分比")
    private BigDecimal pctchg;

    @ApiModelProperty(value = "振幅")
    private BigDecimal amplitude;

    @ApiModelProperty(value = "成交量")
    private Long vol;

    @ApiModelProperty(value = "成交额")
    private BigDecimal amount;

    @ApiModelProperty(value = "换手率")
    private BigDecimal turnoverrate;

    @ApiModelProperty(value = "涨停价")
    private String limitup;

    @ApiModelProperty(value = "跌停价")
    private String limitdown;

    @ApiModelProperty(value = "涨停次数")
    private Integer limituptimes;

    @ApiModelProperty(value = "总股本")
    private Long total;

    @ApiModelProperty(value = "流通股本")
    private Long circulating;

    @ApiModelProperty(value = "总市值")
    private String totalmarketvalue;

    @ApiModelProperty(value = "流通市值")
    private BigDecimal circulatingmarketvalue;

    @ApiModelProperty(value = "市盈率")
    private BigDecimal pe;

    @ApiModelProperty(value = "市盈率（动）")
    private BigDecimal pes;

    @ApiModelProperty(value = "内盘")
    private String innerdisk;

    @ApiModelProperty(value = "外盘")
    private String outerdisk;

    @ApiModelProperty(value = "快速涨跌")
    private BigDecimal fastchg;

    @ApiModelProperty(value = "近期涨跌")
    private BigDecimal recentchange;

    @ApiModelProperty(value = "变动次数")
    private Integer changecount;

    @ApiModelProperty(value = "选股日期")
    private String selectdate;

    @ApiModelProperty(value = "自选时间")
    private String selfselectedtime;

    @ApiModelProperty(value = "报告日期")
    private String reportdate;

    @ApiModelProperty(value = "报告数量")
    private Integer reportcount;

    @ApiModelProperty(value = "问答数量")
    private Integer qacount;

    @ApiModelProperty(value = "问答时间")
    private String qatime;

    @ApiModelProperty(value = "券商数量")
    private Integer brokercount;

    @ApiModelProperty(value = "时间")
    private Date time;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private Integer tag;

    private BigDecimal orivalue;

    private BigDecimal nowvalue;

    private static final long serialVersionUID = 1L;

    public String getStockid() {
        return stockid;
    }

    public void setStockid(String stockid) {
        this.stockid = stockid == null ? null : stockid.trim();
    }

    public BigDecimal getZhangfu() {
        return zhangfu;
    }

    public void setZhangfu(BigDecimal zhangfu) {
        this.zhangfu = zhangfu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getMarketvalue() {
        return marketvalue;
    }

    public void setMarketvalue(BigDecimal marketvalue) {
        this.marketvalue = marketvalue;
    }

    public Date getTradedate() {
        return tradedate;
    }

    public void setTradedate(Date tradedate) {
        this.tradedate = tradedate;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getPreclose() {
        return preclose;
    }

    public void setPreclose(BigDecimal preclose) {
        this.preclose = preclose;
    }

    public BigDecimal getPctchg() {
        return pctchg;
    }

    public void setPctchg(BigDecimal pctchg) {
        this.pctchg = pctchg;
    }

    public BigDecimal getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(BigDecimal amplitude) {
        this.amplitude = amplitude;
    }

    public Long getVol() {
        return vol;
    }

    public void setVol(Long vol) {
        this.vol = vol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTurnoverrate() {
        return turnoverrate;
    }

    public void setTurnoverrate(BigDecimal turnoverrate) {
        this.turnoverrate = turnoverrate;
    }

    public String getLimitup() {
        return limitup;
    }

    public void setLimitup(String limitup) {
        this.limitup = limitup == null ? null : limitup.trim();
    }

    public String getLimitdown() {
        return limitdown;
    }

    public void setLimitdown(String limitdown) {
        this.limitdown = limitdown == null ? null : limitdown.trim();
    }

    public Integer getLimituptimes() {
        return limituptimes;
    }

    public void setLimituptimes(Integer limituptimes) {
        this.limituptimes = limituptimes;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCirculating() {
        return circulating;
    }

    public void setCirculating(Long circulating) {
        this.circulating = circulating;
    }

    public String getTotalmarketvalue() {
        return totalmarketvalue;
    }

    public void setTotalmarketvalue(String totalmarketvalue) {
        this.totalmarketvalue = totalmarketvalue == null ? null : totalmarketvalue.trim();
    }

    public BigDecimal getCirculatingmarketvalue() {
        return circulatingmarketvalue;
    }

    public void setCirculatingmarketvalue(BigDecimal circulatingmarketvalue) {
        this.circulatingmarketvalue = circulatingmarketvalue;
    }

    public BigDecimal getPe() {
        return pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPes() {
        return pes;
    }

    public void setPes(BigDecimal pes) {
        this.pes = pes;
    }

    public String getInnerdisk() {
        return innerdisk;
    }

    public void setInnerdisk(String innerdisk) {
        this.innerdisk = innerdisk == null ? null : innerdisk.trim();
    }

    public String getOuterdisk() {
        return outerdisk;
    }

    public void setOuterdisk(String outerdisk) {
        this.outerdisk = outerdisk == null ? null : outerdisk.trim();
    }

    public BigDecimal getFastchg() {
        return fastchg;
    }

    public void setFastchg(BigDecimal fastchg) {
        this.fastchg = fastchg;
    }

    public BigDecimal getRecentchange() {
        return recentchange;
    }

    public void setRecentchange(BigDecimal recentchange) {
        this.recentchange = recentchange;
    }

    public Integer getChangecount() {
        return changecount;
    }

    public void setChangecount(Integer changecount) {
        this.changecount = changecount;
    }

    public String getSelectdate() {
        return selectdate;
    }

    public void setSelectdate(String selectdate) {
        this.selectdate = selectdate == null ? null : selectdate.trim();
    }

    public String getSelfselectedtime() {
        return selfselectedtime;
    }

    public void setSelfselectedtime(String selfselectedtime) {
        this.selfselectedtime = selfselectedtime == null ? null : selfselectedtime.trim();
    }

    public String getReportdate() {
        return reportdate;
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate == null ? null : reportdate.trim();
    }

    public Integer getReportcount() {
        return reportcount;
    }

    public void setReportcount(Integer reportcount) {
        this.reportcount = reportcount;
    }

    public Integer getQacount() {
        return qacount;
    }

    public void setQacount(Integer qacount) {
        this.qacount = qacount;
    }

    public String getQatime() {
        return qatime;
    }

    public void setQatime(String qatime) {
        this.qatime = qatime == null ? null : qatime.trim();
    }

    public Integer getBrokercount() {
        return brokercount;
    }

    public void setBrokercount(Integer brokercount) {
        this.brokercount = brokercount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public BigDecimal getOrivalue() {
        return orivalue;
    }

    public void setOrivalue(BigDecimal orivalue) {
        this.orivalue = orivalue;
    }

    public BigDecimal getNowvalue() {
        return nowvalue;
    }

    public void setNowvalue(BigDecimal nowvalue) {
        this.nowvalue = nowvalue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", stockid=").append(stockid);
        sb.append(", zhangfu=").append(zhangfu);
        sb.append(", name=").append(name);
        sb.append(", marketvalue=").append(marketvalue);
        sb.append(", tradedate=").append(tradedate);
        sb.append(", current=").append(current);
        sb.append(", open=").append(open);
        sb.append(", high=").append(high);
        sb.append(", low=").append(low);
        sb.append(", close=").append(close);
        sb.append(", preclose=").append(preclose);
        sb.append(", pctchg=").append(pctchg);
        sb.append(", amplitude=").append(amplitude);
        sb.append(", vol=").append(vol);
        sb.append(", amount=").append(amount);
        sb.append(", turnoverrate=").append(turnoverrate);
        sb.append(", limitup=").append(limitup);
        sb.append(", limitdown=").append(limitdown);
        sb.append(", limituptimes=").append(limituptimes);
        sb.append(", total=").append(total);
        sb.append(", circulating=").append(circulating);
        sb.append(", totalmarketvalue=").append(totalmarketvalue);
        sb.append(", circulatingmarketvalue=").append(circulatingmarketvalue);
        sb.append(", pe=").append(pe);
        sb.append(", pes=").append(pes);
        sb.append(", innerdisk=").append(innerdisk);
        sb.append(", outerdisk=").append(outerdisk);
        sb.append(", fastchg=").append(fastchg);
        sb.append(", recentchange=").append(recentchange);
        sb.append(", changecount=").append(changecount);
        sb.append(", selectdate=").append(selectdate);
        sb.append(", selfselectedtime=").append(selfselectedtime);
        sb.append(", reportdate=").append(reportdate);
        sb.append(", reportcount=").append(reportcount);
        sb.append(", qacount=").append(qacount);
        sb.append(", qatime=").append(qatime);
        sb.append(", brokercount=").append(brokercount);
        sb.append(", time=").append(time);
        sb.append(", status=").append(status);
        sb.append(", tag=").append(tag);
        sb.append(", orivalue=").append(orivalue);
        sb.append(", nowvalue=").append(nowvalue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}