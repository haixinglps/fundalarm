package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class ZmqVideoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ZmqVideoExample() {
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

        public Criteria andVidIsNull() {
            addCriterion("vid is null");
            return (Criteria) this;
        }

        public Criteria andVidIsNotNull() {
            addCriterion("vid is not null");
            return (Criteria) this;
        }

        public Criteria andVidEqualTo(Integer value) {
            addCriterion("vid =", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotEqualTo(Integer value) {
            addCriterion("vid <>", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidGreaterThan(Integer value) {
            addCriterion("vid >", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidGreaterThanOrEqualTo(Integer value) {
            addCriterion("vid >=", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidLessThan(Integer value) {
            addCriterion("vid <", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidLessThanOrEqualTo(Integer value) {
            addCriterion("vid <=", value, "vid");
            return (Criteria) this;
        }

        public Criteria andVidIn(List<Integer> values) {
            addCriterion("vid in", values, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotIn(List<Integer> values) {
            addCriterion("vid not in", values, "vid");
            return (Criteria) this;
        }

        public Criteria andVidBetween(Integer value1, Integer value2) {
            addCriterion("vid between", value1, value2, "vid");
            return (Criteria) this;
        }

        public Criteria andVidNotBetween(Integer value1, Integer value2) {
            addCriterion("vid not between", value1, value2, "vid");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(String value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(String value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(String value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(String value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(String value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(String value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLike(String value) {
            addCriterion("duration like", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotLike(String value) {
            addCriterion("duration not like", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<String> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<String> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(String value1, String value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(String value1, String value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Integer value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Integer value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Integer value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Integer value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Integer value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Integer> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Integer> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Integer value1, Integer value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNull() {
            addCriterion("addtime is null");
            return (Criteria) this;
        }

        public Criteria andAddtimeIsNotNull() {
            addCriterion("addtime is not null");
            return (Criteria) this;
        }

        public Criteria andAddtimeEqualTo(String value) {
            addCriterion("addtime =", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotEqualTo(String value) {
            addCriterion("addtime <>", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThan(String value) {
            addCriterion("addtime >", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeGreaterThanOrEqualTo(String value) {
            addCriterion("addtime >=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThan(String value) {
            addCriterion("addtime <", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLessThanOrEqualTo(String value) {
            addCriterion("addtime <=", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeLike(String value) {
            addCriterion("addtime like", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotLike(String value) {
            addCriterion("addtime not like", value, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeIn(List<String> values) {
            addCriterion("addtime in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotIn(List<String> values) {
            addCriterion("addtime not in", values, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeBetween(String value1, String value2) {
            addCriterion("addtime between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andAddtimeNotBetween(String value1, String value2) {
            addCriterion("addtime not between", value1, value2, "addtime");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andCoverIsNull() {
            addCriterion("cover is null");
            return (Criteria) this;
        }

        public Criteria andCoverIsNotNull() {
            addCriterion("cover is not null");
            return (Criteria) this;
        }

        public Criteria andCoverEqualTo(String value) {
            addCriterion("cover =", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotEqualTo(String value) {
            addCriterion("cover <>", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverGreaterThan(String value) {
            addCriterion("cover >", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverGreaterThanOrEqualTo(String value) {
            addCriterion("cover >=", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLessThan(String value) {
            addCriterion("cover <", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLessThanOrEqualTo(String value) {
            addCriterion("cover <=", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLike(String value) {
            addCriterion("cover like", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotLike(String value) {
            addCriterion("cover not like", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverIn(List<String> values) {
            addCriterion("cover in", values, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotIn(List<String> values) {
            addCriterion("cover not in", values, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverBetween(String value1, String value2) {
            addCriterion("cover between", value1, value2, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotBetween(String value1, String value2) {
            addCriterion("cover not between", value1, value2, "cover");
            return (Criteria) this;
        }

        public Criteria andTriaIsNull() {
            addCriterion("tria is null");
            return (Criteria) this;
        }

        public Criteria andTriaIsNotNull() {
            addCriterion("tria is not null");
            return (Criteria) this;
        }

        public Criteria andTriaEqualTo(String value) {
            addCriterion("tria =", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaNotEqualTo(String value) {
            addCriterion("tria <>", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaGreaterThan(String value) {
            addCriterion("tria >", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaGreaterThanOrEqualTo(String value) {
            addCriterion("tria >=", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaLessThan(String value) {
            addCriterion("tria <", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaLessThanOrEqualTo(String value) {
            addCriterion("tria <=", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaLike(String value) {
            addCriterion("tria like", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaNotLike(String value) {
            addCriterion("tria not like", value, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaIn(List<String> values) {
            addCriterion("tria in", values, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaNotIn(List<String> values) {
            addCriterion("tria not in", values, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaBetween(String value1, String value2) {
            addCriterion("tria between", value1, value2, "tria");
            return (Criteria) this;
        }

        public Criteria andTriaNotBetween(String value1, String value2) {
            addCriterion("tria not between", value1, value2, "tria");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andAuthorIsNull() {
            addCriterion("author is null");
            return (Criteria) this;
        }

        public Criteria andAuthorIsNotNull() {
            addCriterion("author is not null");
            return (Criteria) this;
        }

        public Criteria andAuthorEqualTo(Integer value) {
            addCriterion("author =", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotEqualTo(Integer value) {
            addCriterion("author <>", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorGreaterThan(Integer value) {
            addCriterion("author >", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorGreaterThanOrEqualTo(Integer value) {
            addCriterion("author >=", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorLessThan(Integer value) {
            addCriterion("author <", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorLessThanOrEqualTo(Integer value) {
            addCriterion("author <=", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorIn(List<Integer> values) {
            addCriterion("author in", values, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotIn(List<Integer> values) {
            addCriterion("author not in", values, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorBetween(Integer value1, Integer value2) {
            addCriterion("author between", value1, value2, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotBetween(Integer value1, Integer value2) {
            addCriterion("author not between", value1, value2, "author");
            return (Criteria) this;
        }

        public Criteria andYunpankeyIsNull() {
            addCriterion("yunpankey is null");
            return (Criteria) this;
        }

        public Criteria andYunpankeyIsNotNull() {
            addCriterion("yunpankey is not null");
            return (Criteria) this;
        }

        public Criteria andYunpankeyEqualTo(String value) {
            addCriterion("yunpankey =", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyNotEqualTo(String value) {
            addCriterion("yunpankey <>", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyGreaterThan(String value) {
            addCriterion("yunpankey >", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyGreaterThanOrEqualTo(String value) {
            addCriterion("yunpankey >=", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyLessThan(String value) {
            addCriterion("yunpankey <", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyLessThanOrEqualTo(String value) {
            addCriterion("yunpankey <=", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyLike(String value) {
            addCriterion("yunpankey like", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyNotLike(String value) {
            addCriterion("yunpankey not like", value, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyIn(List<String> values) {
            addCriterion("yunpankey in", values, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyNotIn(List<String> values) {
            addCriterion("yunpankey not in", values, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyBetween(String value1, String value2) {
            addCriterion("yunpankey between", value1, value2, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andYunpankeyNotBetween(String value1, String value2) {
            addCriterion("yunpankey not between", value1, value2, "yunpankey");
            return (Criteria) this;
        }

        public Criteria andUptagIsNull() {
            addCriterion("uptag is null");
            return (Criteria) this;
        }

        public Criteria andUptagIsNotNull() {
            addCriterion("uptag is not null");
            return (Criteria) this;
        }

        public Criteria andUptagEqualTo(Integer value) {
            addCriterion("uptag =", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagNotEqualTo(Integer value) {
            addCriterion("uptag <>", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagGreaterThan(Integer value) {
            addCriterion("uptag >", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagGreaterThanOrEqualTo(Integer value) {
            addCriterion("uptag >=", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagLessThan(Integer value) {
            addCriterion("uptag <", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagLessThanOrEqualTo(Integer value) {
            addCriterion("uptag <=", value, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagIn(List<Integer> values) {
            addCriterion("uptag in", values, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagNotIn(List<Integer> values) {
            addCriterion("uptag not in", values, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagBetween(Integer value1, Integer value2) {
            addCriterion("uptag between", value1, value2, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptagNotBetween(Integer value1, Integer value2) {
            addCriterion("uptag not between", value1, value2, "uptag");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428IsNull() {
            addCriterion("uptag13681594428 is null");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428IsNotNull() {
            addCriterion("uptag13681594428 is not null");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428EqualTo(Integer value) {
            addCriterion("uptag13681594428 =", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428NotEqualTo(Integer value) {
            addCriterion("uptag13681594428 <>", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428GreaterThan(Integer value) {
            addCriterion("uptag13681594428 >", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428GreaterThanOrEqualTo(Integer value) {
            addCriterion("uptag13681594428 >=", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428LessThan(Integer value) {
            addCriterion("uptag13681594428 <", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428LessThanOrEqualTo(Integer value) {
            addCriterion("uptag13681594428 <=", value, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428In(List<Integer> values) {
            addCriterion("uptag13681594428 in", values, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428NotIn(List<Integer> values) {
            addCriterion("uptag13681594428 not in", values, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428Between(Integer value1, Integer value2) {
            addCriterion("uptag13681594428 between", value1, value2, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUptag13681594428NotBetween(Integer value1, Integer value2) {
            addCriterion("uptag13681594428 not between", value1, value2, "uptag13681594428");
            return (Criteria) this;
        }

        public Criteria andUrlidIsNull() {
            addCriterion("urlid is null");
            return (Criteria) this;
        }

        public Criteria andUrlidIsNotNull() {
            addCriterion("urlid is not null");
            return (Criteria) this;
        }

        public Criteria andUrlidEqualTo(String value) {
            addCriterion("urlid =", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidNotEqualTo(String value) {
            addCriterion("urlid <>", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidGreaterThan(String value) {
            addCriterion("urlid >", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidGreaterThanOrEqualTo(String value) {
            addCriterion("urlid >=", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidLessThan(String value) {
            addCriterion("urlid <", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidLessThanOrEqualTo(String value) {
            addCriterion("urlid <=", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidLike(String value) {
            addCriterion("urlid like", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidNotLike(String value) {
            addCriterion("urlid not like", value, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidIn(List<String> values) {
            addCriterion("urlid in", values, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidNotIn(List<String> values) {
            addCriterion("urlid not in", values, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidBetween(String value1, String value2) {
            addCriterion("urlid between", value1, value2, "urlid");
            return (Criteria) this;
        }

        public Criteria andUrlidNotBetween(String value1, String value2) {
            addCriterion("urlid not between", value1, value2, "urlid");
            return (Criteria) this;
        }

        public Criteria andSalesIsNull() {
            addCriterion("sales is null");
            return (Criteria) this;
        }

        public Criteria andSalesIsNotNull() {
            addCriterion("sales is not null");
            return (Criteria) this;
        }

        public Criteria andSalesEqualTo(Integer value) {
            addCriterion("sales =", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesNotEqualTo(Integer value) {
            addCriterion("sales <>", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesGreaterThan(Integer value) {
            addCriterion("sales >", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesGreaterThanOrEqualTo(Integer value) {
            addCriterion("sales >=", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesLessThan(Integer value) {
            addCriterion("sales <", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesLessThanOrEqualTo(Integer value) {
            addCriterion("sales <=", value, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesIn(List<Integer> values) {
            addCriterion("sales in", values, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesNotIn(List<Integer> values) {
            addCriterion("sales not in", values, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesBetween(Integer value1, Integer value2) {
            addCriterion("sales between", value1, value2, "sales");
            return (Criteria) this;
        }

        public Criteria andSalesNotBetween(Integer value1, Integer value2) {
            addCriterion("sales not between", value1, value2, "sales");
            return (Criteria) this;
        }

        public Criteria andGoodtagIsNull() {
            addCriterion("goodtag is null");
            return (Criteria) this;
        }

        public Criteria andGoodtagIsNotNull() {
            addCriterion("goodtag is not null");
            return (Criteria) this;
        }

        public Criteria andGoodtagEqualTo(Integer value) {
            addCriterion("goodtag =", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagNotEqualTo(Integer value) {
            addCriterion("goodtag <>", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagGreaterThan(Integer value) {
            addCriterion("goodtag >", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagGreaterThanOrEqualTo(Integer value) {
            addCriterion("goodtag >=", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagLessThan(Integer value) {
            addCriterion("goodtag <", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagLessThanOrEqualTo(Integer value) {
            addCriterion("goodtag <=", value, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagIn(List<Integer> values) {
            addCriterion("goodtag in", values, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagNotIn(List<Integer> values) {
            addCriterion("goodtag not in", values, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagBetween(Integer value1, Integer value2) {
            addCriterion("goodtag between", value1, value2, "goodtag");
            return (Criteria) this;
        }

        public Criteria andGoodtagNotBetween(Integer value1, Integer value2) {
            addCriterion("goodtag not between", value1, value2, "goodtag");
            return (Criteria) this;
        }

        public Criteria andUrlkey2IsNull() {
            addCriterion("urlkey2 is null");
            return (Criteria) this;
        }

        public Criteria andUrlkey2IsNotNull() {
            addCriterion("urlkey2 is not null");
            return (Criteria) this;
        }

        public Criteria andUrlkey2EqualTo(String value) {
            addCriterion("urlkey2 =", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2NotEqualTo(String value) {
            addCriterion("urlkey2 <>", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2GreaterThan(String value) {
            addCriterion("urlkey2 >", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2GreaterThanOrEqualTo(String value) {
            addCriterion("urlkey2 >=", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2LessThan(String value) {
            addCriterion("urlkey2 <", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2LessThanOrEqualTo(String value) {
            addCriterion("urlkey2 <=", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2Like(String value) {
            addCriterion("urlkey2 like", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2NotLike(String value) {
            addCriterion("urlkey2 not like", value, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2In(List<String> values) {
            addCriterion("urlkey2 in", values, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2NotIn(List<String> values) {
            addCriterion("urlkey2 not in", values, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2Between(String value1, String value2) {
            addCriterion("urlkey2 between", value1, value2, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUrlkey2NotBetween(String value1, String value2) {
            addCriterion("urlkey2 not between", value1, value2, "urlkey2");
            return (Criteria) this;
        }

        public Criteria andUptag3IsNull() {
            addCriterion("uptag3 is null");
            return (Criteria) this;
        }

        public Criteria andUptag3IsNotNull() {
            addCriterion("uptag3 is not null");
            return (Criteria) this;
        }

        public Criteria andUptag3EqualTo(Integer value) {
            addCriterion("uptag3 =", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3NotEqualTo(Integer value) {
            addCriterion("uptag3 <>", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3GreaterThan(Integer value) {
            addCriterion("uptag3 >", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3GreaterThanOrEqualTo(Integer value) {
            addCriterion("uptag3 >=", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3LessThan(Integer value) {
            addCriterion("uptag3 <", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3LessThanOrEqualTo(Integer value) {
            addCriterion("uptag3 <=", value, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3In(List<Integer> values) {
            addCriterion("uptag3 in", values, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3NotIn(List<Integer> values) {
            addCriterion("uptag3 not in", values, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3Between(Integer value1, Integer value2) {
            addCriterion("uptag3 between", value1, value2, "uptag3");
            return (Criteria) this;
        }

        public Criteria andUptag3NotBetween(Integer value1, Integer value2) {
            addCriterion("uptag3 not between", value1, value2, "uptag3");
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