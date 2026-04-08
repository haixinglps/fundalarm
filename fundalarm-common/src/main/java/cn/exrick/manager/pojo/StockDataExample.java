package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StockDataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StockDataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
        }

        public Criteria andStockidIsNull() {
            addCriterion("stockId is null");
            return (Criteria) this;
        }

        public Criteria andStockidIsNotNull() {
            addCriterion("stockId is not null");
            return (Criteria) this;
        }

        public Criteria andStockidEqualTo(String value) {
            addCriterion("stockId =", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotEqualTo(String value) {
            addCriterion("stockId <>", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidGreaterThan(String value) {
            addCriterion("stockId >", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidGreaterThanOrEqualTo(String value) {
            addCriterion("stockId >=", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidLessThan(String value) {
            addCriterion("stockId <", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidLessThanOrEqualTo(String value) {
            addCriterion("stockId <=", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidLike(String value) {
            addCriterion("stockId like", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotLike(String value) {
            addCriterion("stockId not like", value, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidIn(List<String> values) {
            addCriterion("stockId in", values, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotIn(List<String> values) {
            addCriterion("stockId not in", values, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidBetween(String value1, String value2) {
            addCriterion("stockId between", value1, value2, "stockid");
            return (Criteria) this;
        }

        public Criteria andStockidNotBetween(String value1, String value2) {
            addCriterion("stockId not between", value1, value2, "stockid");
            return (Criteria) this;
        }

        public Criteria andZhangfuIsNull() {
            addCriterion("zhangfu is null");
            return (Criteria) this;
        }

        public Criteria andZhangfuIsNotNull() {
            addCriterion("zhangfu is not null");
            return (Criteria) this;
        }

        public Criteria andZhangfuEqualTo(BigDecimal value) {
            addCriterion("zhangfu =", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuNotEqualTo(BigDecimal value) {
            addCriterion("zhangfu <>", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuGreaterThan(BigDecimal value) {
            addCriterion("zhangfu >", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("zhangfu >=", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuLessThan(BigDecimal value) {
            addCriterion("zhangfu <", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuLessThanOrEqualTo(BigDecimal value) {
            addCriterion("zhangfu <=", value, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuIn(List<BigDecimal> values) {
            addCriterion("zhangfu in", values, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuNotIn(List<BigDecimal> values) {
            addCriterion("zhangfu not in", values, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zhangfu between", value1, value2, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andZhangfuNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("zhangfu not between", value1, value2, "zhangfu");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andMarketvalueIsNull() {
            addCriterion("marketValue is null");
            return (Criteria) this;
        }

        public Criteria andMarketvalueIsNotNull() {
            addCriterion("marketValue is not null");
            return (Criteria) this;
        }

        public Criteria andMarketvalueEqualTo(BigDecimal value) {
            addCriterion("marketValue =", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueNotEqualTo(BigDecimal value) {
            addCriterion("marketValue <>", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueGreaterThan(BigDecimal value) {
            addCriterion("marketValue >", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("marketValue >=", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueLessThan(BigDecimal value) {
            addCriterion("marketValue <", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("marketValue <=", value, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueIn(List<BigDecimal> values) {
            addCriterion("marketValue in", values, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueNotIn(List<BigDecimal> values) {
            addCriterion("marketValue not in", values, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("marketValue between", value1, value2, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andMarketvalueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("marketValue not between", value1, value2, "marketvalue");
            return (Criteria) this;
        }

        public Criteria andTradedateIsNull() {
            addCriterion("tradeDate is null");
            return (Criteria) this;
        }

        public Criteria andTradedateIsNotNull() {
            addCriterion("tradeDate is not null");
            return (Criteria) this;
        }

        public Criteria andTradedateEqualTo(Date value) {
            addCriterionForJDBCDate("tradeDate =", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateNotEqualTo(Date value) {
            addCriterionForJDBCDate("tradeDate <>", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateGreaterThan(Date value) {
            addCriterionForJDBCDate("tradeDate >", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("tradeDate >=", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateLessThan(Date value) {
            addCriterionForJDBCDate("tradeDate <", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("tradeDate <=", value, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateIn(List<Date> values) {
            addCriterionForJDBCDate("tradeDate in", values, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateNotIn(List<Date> values) {
            addCriterionForJDBCDate("tradeDate not in", values, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("tradeDate between", value1, value2, "tradedate");
            return (Criteria) this;
        }

        public Criteria andTradedateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("tradeDate not between", value1, value2, "tradedate");
            return (Criteria) this;
        }

        public Criteria andCurrentIsNull() {
            addCriterion("current is null");
            return (Criteria) this;
        }

        public Criteria andCurrentIsNotNull() {
            addCriterion("current is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentEqualTo(BigDecimal value) {
            addCriterion("current =", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentNotEqualTo(BigDecimal value) {
            addCriterion("current <>", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentGreaterThan(BigDecimal value) {
            addCriterion("current >", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("current >=", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentLessThan(BigDecimal value) {
            addCriterion("current <", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("current <=", value, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentIn(List<BigDecimal> values) {
            addCriterion("current in", values, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentNotIn(List<BigDecimal> values) {
            addCriterion("current not in", values, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("current between", value1, value2, "current");
            return (Criteria) this;
        }

        public Criteria andCurrentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("current not between", value1, value2, "current");
            return (Criteria) this;
        }

        public Criteria andOpenIsNull() {
            addCriterion("open is null");
            return (Criteria) this;
        }

        public Criteria andOpenIsNotNull() {
            addCriterion("open is not null");
            return (Criteria) this;
        }

        public Criteria andOpenEqualTo(BigDecimal value) {
            addCriterion("open =", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotEqualTo(BigDecimal value) {
            addCriterion("open <>", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenGreaterThan(BigDecimal value) {
            addCriterion("open >", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("open >=", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenLessThan(BigDecimal value) {
            addCriterion("open <", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenLessThanOrEqualTo(BigDecimal value) {
            addCriterion("open <=", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenIn(List<BigDecimal> values) {
            addCriterion("open in", values, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotIn(List<BigDecimal> values) {
            addCriterion("open not in", values, "open");
            return (Criteria) this;
        }

        public Criteria andOpenBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open between", value1, value2, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("open not between", value1, value2, "open");
            return (Criteria) this;
        }

        public Criteria andHighIsNull() {
            addCriterion("high is null");
            return (Criteria) this;
        }

        public Criteria andHighIsNotNull() {
            addCriterion("high is not null");
            return (Criteria) this;
        }

        public Criteria andHighEqualTo(BigDecimal value) {
            addCriterion("high =", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighNotEqualTo(BigDecimal value) {
            addCriterion("high <>", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighGreaterThan(BigDecimal value) {
            addCriterion("high >", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("high >=", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighLessThan(BigDecimal value) {
            addCriterion("high <", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighLessThanOrEqualTo(BigDecimal value) {
            addCriterion("high <=", value, "high");
            return (Criteria) this;
        }

        public Criteria andHighIn(List<BigDecimal> values) {
            addCriterion("high in", values, "high");
            return (Criteria) this;
        }

        public Criteria andHighNotIn(List<BigDecimal> values) {
            addCriterion("high not in", values, "high");
            return (Criteria) this;
        }

        public Criteria andHighBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("high between", value1, value2, "high");
            return (Criteria) this;
        }

        public Criteria andHighNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("high not between", value1, value2, "high");
            return (Criteria) this;
        }

        public Criteria andLowIsNull() {
            addCriterion("low is null");
            return (Criteria) this;
        }

        public Criteria andLowIsNotNull() {
            addCriterion("low is not null");
            return (Criteria) this;
        }

        public Criteria andLowEqualTo(BigDecimal value) {
            addCriterion("low =", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowNotEqualTo(BigDecimal value) {
            addCriterion("low <>", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowGreaterThan(BigDecimal value) {
            addCriterion("low >", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("low >=", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowLessThan(BigDecimal value) {
            addCriterion("low <", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowLessThanOrEqualTo(BigDecimal value) {
            addCriterion("low <=", value, "low");
            return (Criteria) this;
        }

        public Criteria andLowIn(List<BigDecimal> values) {
            addCriterion("low in", values, "low");
            return (Criteria) this;
        }

        public Criteria andLowNotIn(List<BigDecimal> values) {
            addCriterion("low not in", values, "low");
            return (Criteria) this;
        }

        public Criteria andLowBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("low between", value1, value2, "low");
            return (Criteria) this;
        }

        public Criteria andLowNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("low not between", value1, value2, "low");
            return (Criteria) this;
        }

        public Criteria andCloseIsNull() {
            addCriterion("close is null");
            return (Criteria) this;
        }

        public Criteria andCloseIsNotNull() {
            addCriterion("close is not null");
            return (Criteria) this;
        }

        public Criteria andCloseEqualTo(BigDecimal value) {
            addCriterion("close =", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotEqualTo(BigDecimal value) {
            addCriterion("close <>", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseGreaterThan(BigDecimal value) {
            addCriterion("close >", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("close >=", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseLessThan(BigDecimal value) {
            addCriterion("close <", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("close <=", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseIn(List<BigDecimal> values) {
            addCriterion("close in", values, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotIn(List<BigDecimal> values) {
            addCriterion("close not in", values, "close");
            return (Criteria) this;
        }

        public Criteria andCloseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("close between", value1, value2, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("close not between", value1, value2, "close");
            return (Criteria) this;
        }

        public Criteria andPrecloseIsNull() {
            addCriterion("preClose is null");
            return (Criteria) this;
        }

        public Criteria andPrecloseIsNotNull() {
            addCriterion("preClose is not null");
            return (Criteria) this;
        }

        public Criteria andPrecloseEqualTo(BigDecimal value) {
            addCriterion("preClose =", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseNotEqualTo(BigDecimal value) {
            addCriterion("preClose <>", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseGreaterThan(BigDecimal value) {
            addCriterion("preClose >", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("preClose >=", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseLessThan(BigDecimal value) {
            addCriterion("preClose <", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("preClose <=", value, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseIn(List<BigDecimal> values) {
            addCriterion("preClose in", values, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseNotIn(List<BigDecimal> values) {
            addCriterion("preClose not in", values, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("preClose between", value1, value2, "preclose");
            return (Criteria) this;
        }

        public Criteria andPrecloseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("preClose not between", value1, value2, "preclose");
            return (Criteria) this;
        }

        public Criteria andPctchgIsNull() {
            addCriterion("pctChg is null");
            return (Criteria) this;
        }

        public Criteria andPctchgIsNotNull() {
            addCriterion("pctChg is not null");
            return (Criteria) this;
        }

        public Criteria andPctchgEqualTo(BigDecimal value) {
            addCriterion("pctChg =", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotEqualTo(BigDecimal value) {
            addCriterion("pctChg <>", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgGreaterThan(BigDecimal value) {
            addCriterion("pctChg >", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pctChg >=", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgLessThan(BigDecimal value) {
            addCriterion("pctChg <", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pctChg <=", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgIn(List<BigDecimal> values) {
            addCriterion("pctChg in", values, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotIn(List<BigDecimal> values) {
            addCriterion("pctChg not in", values, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pctChg between", value1, value2, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pctChg not between", value1, value2, "pctchg");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIsNull() {
            addCriterion("amplitude is null");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIsNotNull() {
            addCriterion("amplitude is not null");
            return (Criteria) this;
        }

        public Criteria andAmplitudeEqualTo(BigDecimal value) {
            addCriterion("amplitude =", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotEqualTo(BigDecimal value) {
            addCriterion("amplitude <>", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeGreaterThan(BigDecimal value) {
            addCriterion("amplitude >", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amplitude >=", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeLessThan(BigDecimal value) {
            addCriterion("amplitude <", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amplitude <=", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIn(List<BigDecimal> values) {
            addCriterion("amplitude in", values, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotIn(List<BigDecimal> values) {
            addCriterion("amplitude not in", values, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amplitude between", value1, value2, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amplitude not between", value1, value2, "amplitude");
            return (Criteria) this;
        }

        public Criteria andVolIsNull() {
            addCriterion("vol is null");
            return (Criteria) this;
        }

        public Criteria andVolIsNotNull() {
            addCriterion("vol is not null");
            return (Criteria) this;
        }

        public Criteria andVolEqualTo(Long value) {
            addCriterion("vol =", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolNotEqualTo(Long value) {
            addCriterion("vol <>", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolGreaterThan(Long value) {
            addCriterion("vol >", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolGreaterThanOrEqualTo(Long value) {
            addCriterion("vol >=", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolLessThan(Long value) {
            addCriterion("vol <", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolLessThanOrEqualTo(Long value) {
            addCriterion("vol <=", value, "vol");
            return (Criteria) this;
        }

        public Criteria andVolIn(List<Long> values) {
            addCriterion("vol in", values, "vol");
            return (Criteria) this;
        }

        public Criteria andVolNotIn(List<Long> values) {
            addCriterion("vol not in", values, "vol");
            return (Criteria) this;
        }

        public Criteria andVolBetween(Long value1, Long value2) {
            addCriterion("vol between", value1, value2, "vol");
            return (Criteria) this;
        }

        public Criteria andVolNotBetween(Long value1, Long value2) {
            addCriterion("vol not between", value1, value2, "vol");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateIsNull() {
            addCriterion("turnoverRate is null");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateIsNotNull() {
            addCriterion("turnoverRate is not null");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateEqualTo(BigDecimal value) {
            addCriterion("turnoverRate =", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateNotEqualTo(BigDecimal value) {
            addCriterion("turnoverRate <>", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateGreaterThan(BigDecimal value) {
            addCriterion("turnoverRate >", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("turnoverRate >=", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateLessThan(BigDecimal value) {
            addCriterion("turnoverRate <", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("turnoverRate <=", value, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateIn(List<BigDecimal> values) {
            addCriterion("turnoverRate in", values, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateNotIn(List<BigDecimal> values) {
            addCriterion("turnoverRate not in", values, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("turnoverRate between", value1, value2, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andTurnoverrateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("turnoverRate not between", value1, value2, "turnoverrate");
            return (Criteria) this;
        }

        public Criteria andLimitupIsNull() {
            addCriterion("limitUp is null");
            return (Criteria) this;
        }

        public Criteria andLimitupIsNotNull() {
            addCriterion("limitUp is not null");
            return (Criteria) this;
        }

        public Criteria andLimitupEqualTo(String value) {
            addCriterion("limitUp =", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupNotEqualTo(String value) {
            addCriterion("limitUp <>", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupGreaterThan(String value) {
            addCriterion("limitUp >", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupGreaterThanOrEqualTo(String value) {
            addCriterion("limitUp >=", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupLessThan(String value) {
            addCriterion("limitUp <", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupLessThanOrEqualTo(String value) {
            addCriterion("limitUp <=", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupLike(String value) {
            addCriterion("limitUp like", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupNotLike(String value) {
            addCriterion("limitUp not like", value, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupIn(List<String> values) {
            addCriterion("limitUp in", values, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupNotIn(List<String> values) {
            addCriterion("limitUp not in", values, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupBetween(String value1, String value2) {
            addCriterion("limitUp between", value1, value2, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitupNotBetween(String value1, String value2) {
            addCriterion("limitUp not between", value1, value2, "limitup");
            return (Criteria) this;
        }

        public Criteria andLimitdownIsNull() {
            addCriterion("limitDown is null");
            return (Criteria) this;
        }

        public Criteria andLimitdownIsNotNull() {
            addCriterion("limitDown is not null");
            return (Criteria) this;
        }

        public Criteria andLimitdownEqualTo(String value) {
            addCriterion("limitDown =", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownNotEqualTo(String value) {
            addCriterion("limitDown <>", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownGreaterThan(String value) {
            addCriterion("limitDown >", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownGreaterThanOrEqualTo(String value) {
            addCriterion("limitDown >=", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownLessThan(String value) {
            addCriterion("limitDown <", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownLessThanOrEqualTo(String value) {
            addCriterion("limitDown <=", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownLike(String value) {
            addCriterion("limitDown like", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownNotLike(String value) {
            addCriterion("limitDown not like", value, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownIn(List<String> values) {
            addCriterion("limitDown in", values, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownNotIn(List<String> values) {
            addCriterion("limitDown not in", values, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownBetween(String value1, String value2) {
            addCriterion("limitDown between", value1, value2, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimitdownNotBetween(String value1, String value2) {
            addCriterion("limitDown not between", value1, value2, "limitdown");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIsNull() {
            addCriterion("limitUpTimes is null");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIsNotNull() {
            addCriterion("limitUpTimes is not null");
            return (Criteria) this;
        }

        public Criteria andLimituptimesEqualTo(Integer value) {
            addCriterion("limitUpTimes =", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotEqualTo(Integer value) {
            addCriterion("limitUpTimes <>", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesGreaterThan(Integer value) {
            addCriterion("limitUpTimes >", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("limitUpTimes >=", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesLessThan(Integer value) {
            addCriterion("limitUpTimes <", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesLessThanOrEqualTo(Integer value) {
            addCriterion("limitUpTimes <=", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIn(List<Integer> values) {
            addCriterion("limitUpTimes in", values, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotIn(List<Integer> values) {
            addCriterion("limitUpTimes not in", values, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesBetween(Integer value1, Integer value2) {
            addCriterion("limitUpTimes between", value1, value2, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotBetween(Integer value1, Integer value2) {
            addCriterion("limitUpTimes not between", value1, value2, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(Long value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(Long value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(Long value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(Long value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(Long value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(Long value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<Long> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<Long> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(Long value1, Long value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(Long value1, Long value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andCirculatingIsNull() {
            addCriterion("circulating is null");
            return (Criteria) this;
        }

        public Criteria andCirculatingIsNotNull() {
            addCriterion("circulating is not null");
            return (Criteria) this;
        }

        public Criteria andCirculatingEqualTo(Long value) {
            addCriterion("circulating =", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingNotEqualTo(Long value) {
            addCriterion("circulating <>", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingGreaterThan(Long value) {
            addCriterion("circulating >", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingGreaterThanOrEqualTo(Long value) {
            addCriterion("circulating >=", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingLessThan(Long value) {
            addCriterion("circulating <", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingLessThanOrEqualTo(Long value) {
            addCriterion("circulating <=", value, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingIn(List<Long> values) {
            addCriterion("circulating in", values, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingNotIn(List<Long> values) {
            addCriterion("circulating not in", values, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingBetween(Long value1, Long value2) {
            addCriterion("circulating between", value1, value2, "circulating");
            return (Criteria) this;
        }

        public Criteria andCirculatingNotBetween(Long value1, Long value2) {
            addCriterion("circulating not between", value1, value2, "circulating");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueIsNull() {
            addCriterion("totalMarketValue is null");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueIsNotNull() {
            addCriterion("totalMarketValue is not null");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueEqualTo(String value) {
            addCriterion("totalMarketValue =", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueNotEqualTo(String value) {
            addCriterion("totalMarketValue <>", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueGreaterThan(String value) {
            addCriterion("totalMarketValue >", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueGreaterThanOrEqualTo(String value) {
            addCriterion("totalMarketValue >=", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueLessThan(String value) {
            addCriterion("totalMarketValue <", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueLessThanOrEqualTo(String value) {
            addCriterion("totalMarketValue <=", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueLike(String value) {
            addCriterion("totalMarketValue like", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueNotLike(String value) {
            addCriterion("totalMarketValue not like", value, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueIn(List<String> values) {
            addCriterion("totalMarketValue in", values, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueNotIn(List<String> values) {
            addCriterion("totalMarketValue not in", values, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueBetween(String value1, String value2) {
            addCriterion("totalMarketValue between", value1, value2, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andTotalmarketvalueNotBetween(String value1, String value2) {
            addCriterion("totalMarketValue not between", value1, value2, "totalmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueIsNull() {
            addCriterion("circulatingMarketValue is null");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueIsNotNull() {
            addCriterion("circulatingMarketValue is not null");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueEqualTo(BigDecimal value) {
            addCriterion("circulatingMarketValue =", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueNotEqualTo(BigDecimal value) {
            addCriterion("circulatingMarketValue <>", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueGreaterThan(BigDecimal value) {
            addCriterion("circulatingMarketValue >", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("circulatingMarketValue >=", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueLessThan(BigDecimal value) {
            addCriterion("circulatingMarketValue <", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("circulatingMarketValue <=", value, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueIn(List<BigDecimal> values) {
            addCriterion("circulatingMarketValue in", values, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueNotIn(List<BigDecimal> values) {
            addCriterion("circulatingMarketValue not in", values, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("circulatingMarketValue between", value1, value2, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andCirculatingmarketvalueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("circulatingMarketValue not between", value1, value2, "circulatingmarketvalue");
            return (Criteria) this;
        }

        public Criteria andPeIsNull() {
            addCriterion("pe is null");
            return (Criteria) this;
        }

        public Criteria andPeIsNotNull() {
            addCriterion("pe is not null");
            return (Criteria) this;
        }

        public Criteria andPeEqualTo(BigDecimal value) {
            addCriterion("pe =", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeNotEqualTo(BigDecimal value) {
            addCriterion("pe <>", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeGreaterThan(BigDecimal value) {
            addCriterion("pe >", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pe >=", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeLessThan(BigDecimal value) {
            addCriterion("pe <", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pe <=", value, "pe");
            return (Criteria) this;
        }

        public Criteria andPeIn(List<BigDecimal> values) {
            addCriterion("pe in", values, "pe");
            return (Criteria) this;
        }

        public Criteria andPeNotIn(List<BigDecimal> values) {
            addCriterion("pe not in", values, "pe");
            return (Criteria) this;
        }

        public Criteria andPeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pe between", value1, value2, "pe");
            return (Criteria) this;
        }

        public Criteria andPeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pe not between", value1, value2, "pe");
            return (Criteria) this;
        }

        public Criteria andPesIsNull() {
            addCriterion("pes is null");
            return (Criteria) this;
        }

        public Criteria andPesIsNotNull() {
            addCriterion("pes is not null");
            return (Criteria) this;
        }

        public Criteria andPesEqualTo(BigDecimal value) {
            addCriterion("pes =", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesNotEqualTo(BigDecimal value) {
            addCriterion("pes <>", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesGreaterThan(BigDecimal value) {
            addCriterion("pes >", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pes >=", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesLessThan(BigDecimal value) {
            addCriterion("pes <", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pes <=", value, "pes");
            return (Criteria) this;
        }

        public Criteria andPesIn(List<BigDecimal> values) {
            addCriterion("pes in", values, "pes");
            return (Criteria) this;
        }

        public Criteria andPesNotIn(List<BigDecimal> values) {
            addCriterion("pes not in", values, "pes");
            return (Criteria) this;
        }

        public Criteria andPesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pes between", value1, value2, "pes");
            return (Criteria) this;
        }

        public Criteria andPesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pes not between", value1, value2, "pes");
            return (Criteria) this;
        }

        public Criteria andInnerdiskIsNull() {
            addCriterion("innerDisk is null");
            return (Criteria) this;
        }

        public Criteria andInnerdiskIsNotNull() {
            addCriterion("innerDisk is not null");
            return (Criteria) this;
        }

        public Criteria andInnerdiskEqualTo(String value) {
            addCriterion("innerDisk =", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskNotEqualTo(String value) {
            addCriterion("innerDisk <>", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskGreaterThan(String value) {
            addCriterion("innerDisk >", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskGreaterThanOrEqualTo(String value) {
            addCriterion("innerDisk >=", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskLessThan(String value) {
            addCriterion("innerDisk <", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskLessThanOrEqualTo(String value) {
            addCriterion("innerDisk <=", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskLike(String value) {
            addCriterion("innerDisk like", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskNotLike(String value) {
            addCriterion("innerDisk not like", value, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskIn(List<String> values) {
            addCriterion("innerDisk in", values, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskNotIn(List<String> values) {
            addCriterion("innerDisk not in", values, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskBetween(String value1, String value2) {
            addCriterion("innerDisk between", value1, value2, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andInnerdiskNotBetween(String value1, String value2) {
            addCriterion("innerDisk not between", value1, value2, "innerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskIsNull() {
            addCriterion("outerDisk is null");
            return (Criteria) this;
        }

        public Criteria andOuterdiskIsNotNull() {
            addCriterion("outerDisk is not null");
            return (Criteria) this;
        }

        public Criteria andOuterdiskEqualTo(String value) {
            addCriterion("outerDisk =", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskNotEqualTo(String value) {
            addCriterion("outerDisk <>", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskGreaterThan(String value) {
            addCriterion("outerDisk >", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskGreaterThanOrEqualTo(String value) {
            addCriterion("outerDisk >=", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskLessThan(String value) {
            addCriterion("outerDisk <", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskLessThanOrEqualTo(String value) {
            addCriterion("outerDisk <=", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskLike(String value) {
            addCriterion("outerDisk like", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskNotLike(String value) {
            addCriterion("outerDisk not like", value, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskIn(List<String> values) {
            addCriterion("outerDisk in", values, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskNotIn(List<String> values) {
            addCriterion("outerDisk not in", values, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskBetween(String value1, String value2) {
            addCriterion("outerDisk between", value1, value2, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andOuterdiskNotBetween(String value1, String value2) {
            addCriterion("outerDisk not between", value1, value2, "outerdisk");
            return (Criteria) this;
        }

        public Criteria andFastchgIsNull() {
            addCriterion("fastChg is null");
            return (Criteria) this;
        }

        public Criteria andFastchgIsNotNull() {
            addCriterion("fastChg is not null");
            return (Criteria) this;
        }

        public Criteria andFastchgEqualTo(BigDecimal value) {
            addCriterion("fastChg =", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgNotEqualTo(BigDecimal value) {
            addCriterion("fastChg <>", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgGreaterThan(BigDecimal value) {
            addCriterion("fastChg >", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fastChg >=", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgLessThan(BigDecimal value) {
            addCriterion("fastChg <", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fastChg <=", value, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgIn(List<BigDecimal> values) {
            addCriterion("fastChg in", values, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgNotIn(List<BigDecimal> values) {
            addCriterion("fastChg not in", values, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fastChg between", value1, value2, "fastchg");
            return (Criteria) this;
        }

        public Criteria andFastchgNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fastChg not between", value1, value2, "fastchg");
            return (Criteria) this;
        }

        public Criteria andRecentchangeIsNull() {
            addCriterion("recentChange is null");
            return (Criteria) this;
        }

        public Criteria andRecentchangeIsNotNull() {
            addCriterion("recentChange is not null");
            return (Criteria) this;
        }

        public Criteria andRecentchangeEqualTo(BigDecimal value) {
            addCriterion("recentChange =", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeNotEqualTo(BigDecimal value) {
            addCriterion("recentChange <>", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeGreaterThan(BigDecimal value) {
            addCriterion("recentChange >", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("recentChange >=", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeLessThan(BigDecimal value) {
            addCriterion("recentChange <", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("recentChange <=", value, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeIn(List<BigDecimal> values) {
            addCriterion("recentChange in", values, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeNotIn(List<BigDecimal> values) {
            addCriterion("recentChange not in", values, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recentChange between", value1, value2, "recentchange");
            return (Criteria) this;
        }

        public Criteria andRecentchangeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recentChange not between", value1, value2, "recentchange");
            return (Criteria) this;
        }

        public Criteria andChangecountIsNull() {
            addCriterion("changeCount is null");
            return (Criteria) this;
        }

        public Criteria andChangecountIsNotNull() {
            addCriterion("changeCount is not null");
            return (Criteria) this;
        }

        public Criteria andChangecountEqualTo(Integer value) {
            addCriterion("changeCount =", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountNotEqualTo(Integer value) {
            addCriterion("changeCount <>", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountGreaterThan(Integer value) {
            addCriterion("changeCount >", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("changeCount >=", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountLessThan(Integer value) {
            addCriterion("changeCount <", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountLessThanOrEqualTo(Integer value) {
            addCriterion("changeCount <=", value, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountIn(List<Integer> values) {
            addCriterion("changeCount in", values, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountNotIn(List<Integer> values) {
            addCriterion("changeCount not in", values, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountBetween(Integer value1, Integer value2) {
            addCriterion("changeCount between", value1, value2, "changecount");
            return (Criteria) this;
        }

        public Criteria andChangecountNotBetween(Integer value1, Integer value2) {
            addCriterion("changeCount not between", value1, value2, "changecount");
            return (Criteria) this;
        }

        public Criteria andSelectdateIsNull() {
            addCriterion("selectDate is null");
            return (Criteria) this;
        }

        public Criteria andSelectdateIsNotNull() {
            addCriterion("selectDate is not null");
            return (Criteria) this;
        }

        public Criteria andSelectdateEqualTo(String value) {
            addCriterion("selectDate =", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateNotEqualTo(String value) {
            addCriterion("selectDate <>", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateGreaterThan(String value) {
            addCriterion("selectDate >", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateGreaterThanOrEqualTo(String value) {
            addCriterion("selectDate >=", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateLessThan(String value) {
            addCriterion("selectDate <", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateLessThanOrEqualTo(String value) {
            addCriterion("selectDate <=", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateLike(String value) {
            addCriterion("selectDate like", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateNotLike(String value) {
            addCriterion("selectDate not like", value, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateIn(List<String> values) {
            addCriterion("selectDate in", values, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateNotIn(List<String> values) {
            addCriterion("selectDate not in", values, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateBetween(String value1, String value2) {
            addCriterion("selectDate between", value1, value2, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelectdateNotBetween(String value1, String value2) {
            addCriterion("selectDate not between", value1, value2, "selectdate");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeIsNull() {
            addCriterion("selfSelectedTime is null");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeIsNotNull() {
            addCriterion("selfSelectedTime is not null");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeEqualTo(String value) {
            addCriterion("selfSelectedTime =", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeNotEqualTo(String value) {
            addCriterion("selfSelectedTime <>", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeGreaterThan(String value) {
            addCriterion("selfSelectedTime >", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeGreaterThanOrEqualTo(String value) {
            addCriterion("selfSelectedTime >=", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeLessThan(String value) {
            addCriterion("selfSelectedTime <", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeLessThanOrEqualTo(String value) {
            addCriterion("selfSelectedTime <=", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeLike(String value) {
            addCriterion("selfSelectedTime like", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeNotLike(String value) {
            addCriterion("selfSelectedTime not like", value, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeIn(List<String> values) {
            addCriterion("selfSelectedTime in", values, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeNotIn(List<String> values) {
            addCriterion("selfSelectedTime not in", values, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeBetween(String value1, String value2) {
            addCriterion("selfSelectedTime between", value1, value2, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andSelfselectedtimeNotBetween(String value1, String value2) {
            addCriterion("selfSelectedTime not between", value1, value2, "selfselectedtime");
            return (Criteria) this;
        }

        public Criteria andReportdateIsNull() {
            addCriterion("reportDate is null");
            return (Criteria) this;
        }

        public Criteria andReportdateIsNotNull() {
            addCriterion("reportDate is not null");
            return (Criteria) this;
        }

        public Criteria andReportdateEqualTo(String value) {
            addCriterion("reportDate =", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateNotEqualTo(String value) {
            addCriterion("reportDate <>", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateGreaterThan(String value) {
            addCriterion("reportDate >", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateGreaterThanOrEqualTo(String value) {
            addCriterion("reportDate >=", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateLessThan(String value) {
            addCriterion("reportDate <", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateLessThanOrEqualTo(String value) {
            addCriterion("reportDate <=", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateLike(String value) {
            addCriterion("reportDate like", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateNotLike(String value) {
            addCriterion("reportDate not like", value, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateIn(List<String> values) {
            addCriterion("reportDate in", values, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateNotIn(List<String> values) {
            addCriterion("reportDate not in", values, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateBetween(String value1, String value2) {
            addCriterion("reportDate between", value1, value2, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportdateNotBetween(String value1, String value2) {
            addCriterion("reportDate not between", value1, value2, "reportdate");
            return (Criteria) this;
        }

        public Criteria andReportcountIsNull() {
            addCriterion("reportCount is null");
            return (Criteria) this;
        }

        public Criteria andReportcountIsNotNull() {
            addCriterion("reportCount is not null");
            return (Criteria) this;
        }

        public Criteria andReportcountEqualTo(Integer value) {
            addCriterion("reportCount =", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountNotEqualTo(Integer value) {
            addCriterion("reportCount <>", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountGreaterThan(Integer value) {
            addCriterion("reportCount >", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("reportCount >=", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountLessThan(Integer value) {
            addCriterion("reportCount <", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountLessThanOrEqualTo(Integer value) {
            addCriterion("reportCount <=", value, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountIn(List<Integer> values) {
            addCriterion("reportCount in", values, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountNotIn(List<Integer> values) {
            addCriterion("reportCount not in", values, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountBetween(Integer value1, Integer value2) {
            addCriterion("reportCount between", value1, value2, "reportcount");
            return (Criteria) this;
        }

        public Criteria andReportcountNotBetween(Integer value1, Integer value2) {
            addCriterion("reportCount not between", value1, value2, "reportcount");
            return (Criteria) this;
        }

        public Criteria andQacountIsNull() {
            addCriterion("qaCount is null");
            return (Criteria) this;
        }

        public Criteria andQacountIsNotNull() {
            addCriterion("qaCount is not null");
            return (Criteria) this;
        }

        public Criteria andQacountEqualTo(Integer value) {
            addCriterion("qaCount =", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountNotEqualTo(Integer value) {
            addCriterion("qaCount <>", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountGreaterThan(Integer value) {
            addCriterion("qaCount >", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountGreaterThanOrEqualTo(Integer value) {
            addCriterion("qaCount >=", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountLessThan(Integer value) {
            addCriterion("qaCount <", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountLessThanOrEqualTo(Integer value) {
            addCriterion("qaCount <=", value, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountIn(List<Integer> values) {
            addCriterion("qaCount in", values, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountNotIn(List<Integer> values) {
            addCriterion("qaCount not in", values, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountBetween(Integer value1, Integer value2) {
            addCriterion("qaCount between", value1, value2, "qacount");
            return (Criteria) this;
        }

        public Criteria andQacountNotBetween(Integer value1, Integer value2) {
            addCriterion("qaCount not between", value1, value2, "qacount");
            return (Criteria) this;
        }

        public Criteria andQatimeIsNull() {
            addCriterion("qaTime is null");
            return (Criteria) this;
        }

        public Criteria andQatimeIsNotNull() {
            addCriterion("qaTime is not null");
            return (Criteria) this;
        }

        public Criteria andQatimeEqualTo(String value) {
            addCriterion("qaTime =", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeNotEqualTo(String value) {
            addCriterion("qaTime <>", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeGreaterThan(String value) {
            addCriterion("qaTime >", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeGreaterThanOrEqualTo(String value) {
            addCriterion("qaTime >=", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeLessThan(String value) {
            addCriterion("qaTime <", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeLessThanOrEqualTo(String value) {
            addCriterion("qaTime <=", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeLike(String value) {
            addCriterion("qaTime like", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeNotLike(String value) {
            addCriterion("qaTime not like", value, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeIn(List<String> values) {
            addCriterion("qaTime in", values, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeNotIn(List<String> values) {
            addCriterion("qaTime not in", values, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeBetween(String value1, String value2) {
            addCriterion("qaTime between", value1, value2, "qatime");
            return (Criteria) this;
        }

        public Criteria andQatimeNotBetween(String value1, String value2) {
            addCriterion("qaTime not between", value1, value2, "qatime");
            return (Criteria) this;
        }

        public Criteria andBrokercountIsNull() {
            addCriterion("brokerCount is null");
            return (Criteria) this;
        }

        public Criteria andBrokercountIsNotNull() {
            addCriterion("brokerCount is not null");
            return (Criteria) this;
        }

        public Criteria andBrokercountEqualTo(Integer value) {
            addCriterion("brokerCount =", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountNotEqualTo(Integer value) {
            addCriterion("brokerCount <>", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountGreaterThan(Integer value) {
            addCriterion("brokerCount >", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountGreaterThanOrEqualTo(Integer value) {
            addCriterion("brokerCount >=", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountLessThan(Integer value) {
            addCriterion("brokerCount <", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountLessThanOrEqualTo(Integer value) {
            addCriterion("brokerCount <=", value, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountIn(List<Integer> values) {
            addCriterion("brokerCount in", values, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountNotIn(List<Integer> values) {
            addCriterion("brokerCount not in", values, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountBetween(Integer value1, Integer value2) {
            addCriterion("brokerCount between", value1, value2, "brokercount");
            return (Criteria) this;
        }

        public Criteria andBrokercountNotBetween(Integer value1, Integer value2) {
            addCriterion("brokerCount not between", value1, value2, "brokercount");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterionForJDBCTime("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterionForJDBCTime("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterionForJDBCTime("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("time not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTagIsNull() {
            addCriterion("tag is null");
            return (Criteria) this;
        }

        public Criteria andTagIsNotNull() {
            addCriterion("tag is not null");
            return (Criteria) this;
        }

        public Criteria andTagEqualTo(Integer value) {
            addCriterion("tag =", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotEqualTo(Integer value) {
            addCriterion("tag <>", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThan(Integer value) {
            addCriterion("tag >", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThanOrEqualTo(Integer value) {
            addCriterion("tag >=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThan(Integer value) {
            addCriterion("tag <", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThanOrEqualTo(Integer value) {
            addCriterion("tag <=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagIn(List<Integer> values) {
            addCriterion("tag in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotIn(List<Integer> values) {
            addCriterion("tag not in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagBetween(Integer value1, Integer value2) {
            addCriterion("tag between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotBetween(Integer value1, Integer value2) {
            addCriterion("tag not between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andOrivalueIsNull() {
            addCriterion("orivalue is null");
            return (Criteria) this;
        }

        public Criteria andOrivalueIsNotNull() {
            addCriterion("orivalue is not null");
            return (Criteria) this;
        }

        public Criteria andOrivalueEqualTo(BigDecimal value) {
            addCriterion("orivalue =", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueNotEqualTo(BigDecimal value) {
            addCriterion("orivalue <>", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueGreaterThan(BigDecimal value) {
            addCriterion("orivalue >", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("orivalue >=", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueLessThan(BigDecimal value) {
            addCriterion("orivalue <", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("orivalue <=", value, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueIn(List<BigDecimal> values) {
            addCriterion("orivalue in", values, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueNotIn(List<BigDecimal> values) {
            addCriterion("orivalue not in", values, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("orivalue between", value1, value2, "orivalue");
            return (Criteria) this;
        }

        public Criteria andOrivalueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("orivalue not between", value1, value2, "orivalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueIsNull() {
            addCriterion("nowvalue is null");
            return (Criteria) this;
        }

        public Criteria andNowvalueIsNotNull() {
            addCriterion("nowvalue is not null");
            return (Criteria) this;
        }

        public Criteria andNowvalueEqualTo(BigDecimal value) {
            addCriterion("nowvalue =", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueNotEqualTo(BigDecimal value) {
            addCriterion("nowvalue <>", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueGreaterThan(BigDecimal value) {
            addCriterion("nowvalue >", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("nowvalue >=", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueLessThan(BigDecimal value) {
            addCriterion("nowvalue <", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("nowvalue <=", value, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueIn(List<BigDecimal> values) {
            addCriterion("nowvalue in", values, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueNotIn(List<BigDecimal> values) {
            addCriterion("nowvalue not in", values, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nowvalue between", value1, value2, "nowvalue");
            return (Criteria) this;
        }

        public Criteria andNowvalueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nowvalue not between", value1, value2, "nowvalue");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}