package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbPanelContentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbPanelContentExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPanelIdIsNull() {
            addCriterion("panel_id is null");
            return (Criteria) this;
        }

        public Criteria andPanelIdIsNotNull() {
            addCriterion("panel_id is not null");
            return (Criteria) this;
        }

        public Criteria andPanelIdEqualTo(Integer value) {
            addCriterion("panel_id =", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdNotEqualTo(Integer value) {
            addCriterion("panel_id <>", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdGreaterThan(Integer value) {
            addCriterion("panel_id >", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("panel_id >=", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdLessThan(Integer value) {
            addCriterion("panel_id <", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdLessThanOrEqualTo(Integer value) {
            addCriterion("panel_id <=", value, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdIn(List<Integer> values) {
            addCriterion("panel_id in", values, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdNotIn(List<Integer> values) {
            addCriterion("panel_id not in", values, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdBetween(Integer value1, Integer value2) {
            addCriterion("panel_id between", value1, value2, "panelId");
            return (Criteria) this;
        }

        public Criteria andPanelIdNotBetween(Integer value1, Integer value2) {
            addCriterion("panel_id not between", value1, value2, "panelId");
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

        public Criteria andProductIdIsNull() {
            addCriterion("product_id is null");
            return (Criteria) this;
        }

        public Criteria andProductIdIsNotNull() {
            addCriterion("product_id is not null");
            return (Criteria) this;
        }

        public Criteria andProductIdEqualTo(Long value) {
            addCriterion("product_id =", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotEqualTo(Long value) {
            addCriterion("product_id <>", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThan(Long value) {
            addCriterion("product_id >", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThanOrEqualTo(Long value) {
            addCriterion("product_id >=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThan(Long value) {
            addCriterion("product_id <", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThanOrEqualTo(Long value) {
            addCriterion("product_id <=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdIn(List<Long> values) {
            addCriterion("product_id in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotIn(List<Long> values) {
            addCriterion("product_id not in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdBetween(Long value1, Long value2) {
            addCriterion("product_id between", value1, value2, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotBetween(Long value1, Long value2) {
            addCriterion("product_id not between", value1, value2, "productId");
            return (Criteria) this;
        }

        public Criteria andSortOrderIsNull() {
            addCriterion("sort_order is null");
            return (Criteria) this;
        }

        public Criteria andSortOrderIsNotNull() {
            addCriterion("sort_order is not null");
            return (Criteria) this;
        }

        public Criteria andSortOrderEqualTo(Integer value) {
            addCriterion("sort_order =", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotEqualTo(Integer value) {
            addCriterion("sort_order <>", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderGreaterThan(Integer value) {
            addCriterion("sort_order >", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort_order >=", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderLessThan(Integer value) {
            addCriterion("sort_order <", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderLessThanOrEqualTo(Integer value) {
            addCriterion("sort_order <=", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderIn(List<Integer> values) {
            addCriterion("sort_order in", values, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotIn(List<Integer> values) {
            addCriterion("sort_order not in", values, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderBetween(Integer value1, Integer value2) {
            addCriterion("sort_order between", value1, value2, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("sort_order not between", value1, value2, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andFullUrlIsNull() {
            addCriterion("full_url is null");
            return (Criteria) this;
        }

        public Criteria andFullUrlIsNotNull() {
            addCriterion("full_url is not null");
            return (Criteria) this;
        }

        public Criteria andFullUrlEqualTo(String value) {
            addCriterion("full_url =", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotEqualTo(String value) {
            addCriterion("full_url <>", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlGreaterThan(String value) {
            addCriterion("full_url >", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlGreaterThanOrEqualTo(String value) {
            addCriterion("full_url >=", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLessThan(String value) {
            addCriterion("full_url <", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLessThanOrEqualTo(String value) {
            addCriterion("full_url <=", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLike(String value) {
            addCriterion("full_url like", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotLike(String value) {
            addCriterion("full_url not like", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlIn(List<String> values) {
            addCriterion("full_url in", values, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotIn(List<String> values) {
            addCriterion("full_url not in", values, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlBetween(String value1, String value2) {
            addCriterion("full_url between", value1, value2, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotBetween(String value1, String value2) {
            addCriterion("full_url not between", value1, value2, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNull() {
            addCriterion("pic_url is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNotNull() {
            addCriterion("pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlEqualTo(String value) {
            addCriterion("pic_url =", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotEqualTo(String value) {
            addCriterion("pic_url <>", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThan(String value) {
            addCriterion("pic_url >", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pic_url >=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThan(String value) {
            addCriterion("pic_url <", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThanOrEqualTo(String value) {
            addCriterion("pic_url <=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLike(String value) {
            addCriterion("pic_url like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotLike(String value) {
            addCriterion("pic_url not like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIn(List<String> values) {
            addCriterion("pic_url in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotIn(List<String> values) {
            addCriterion("pic_url not in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlBetween(String value1, String value2) {
            addCriterion("pic_url between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotBetween(String value1, String value2) {
            addCriterion("pic_url not between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrl2IsNull() {
            addCriterion("pic_url2 is null");
            return (Criteria) this;
        }

        public Criteria andPicUrl2IsNotNull() {
            addCriterion("pic_url2 is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrl2EqualTo(String value) {
            addCriterion("pic_url2 =", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2NotEqualTo(String value) {
            addCriterion("pic_url2 <>", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2GreaterThan(String value) {
            addCriterion("pic_url2 >", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2GreaterThanOrEqualTo(String value) {
            addCriterion("pic_url2 >=", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2LessThan(String value) {
            addCriterion("pic_url2 <", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2LessThanOrEqualTo(String value) {
            addCriterion("pic_url2 <=", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2Like(String value) {
            addCriterion("pic_url2 like", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2NotLike(String value) {
            addCriterion("pic_url2 not like", value, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2In(List<String> values) {
            addCriterion("pic_url2 in", values, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2NotIn(List<String> values) {
            addCriterion("pic_url2 not in", values, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2Between(String value1, String value2) {
            addCriterion("pic_url2 between", value1, value2, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl2NotBetween(String value1, String value2) {
            addCriterion("pic_url2 not between", value1, value2, "picUrl2");
            return (Criteria) this;
        }

        public Criteria andPicUrl3IsNull() {
            addCriterion("pic_url3 is null");
            return (Criteria) this;
        }

        public Criteria andPicUrl3IsNotNull() {
            addCriterion("pic_url3 is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrl3EqualTo(String value) {
            addCriterion("pic_url3 =", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3NotEqualTo(String value) {
            addCriterion("pic_url3 <>", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3GreaterThan(String value) {
            addCriterion("pic_url3 >", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3GreaterThanOrEqualTo(String value) {
            addCriterion("pic_url3 >=", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3LessThan(String value) {
            addCriterion("pic_url3 <", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3LessThanOrEqualTo(String value) {
            addCriterion("pic_url3 <=", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3Like(String value) {
            addCriterion("pic_url3 like", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3NotLike(String value) {
            addCriterion("pic_url3 not like", value, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3In(List<String> values) {
            addCriterion("pic_url3 in", values, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3NotIn(List<String> values) {
            addCriterion("pic_url3 not in", values, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3Between(String value1, String value2) {
            addCriterion("pic_url3 between", value1, value2, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andPicUrl3NotBetween(String value1, String value2) {
            addCriterion("pic_url3 not between", value1, value2, "picUrl3");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("updated is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("updated is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Date value) {
            addCriterion("updated =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Date value) {
            addCriterion("updated <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Date value) {
            addCriterion("updated >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("updated >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Date value) {
            addCriterion("updated <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("updated <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Date> values) {
            addCriterion("updated in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Date> values) {
            addCriterion("updated not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Date value1, Date value2) {
            addCriterion("updated between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("updated not between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andClickingRateIsNull() {
            addCriterion("clicking_rate is null");
            return (Criteria) this;
        }

        public Criteria andClickingRateIsNotNull() {
            addCriterion("clicking_rate is not null");
            return (Criteria) this;
        }

        public Criteria andClickingRateEqualTo(BigDecimal value) {
            addCriterion("clicking_rate =", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateNotEqualTo(BigDecimal value) {
            addCriterion("clicking_rate <>", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateGreaterThan(BigDecimal value) {
            addCriterion("clicking_rate >", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("clicking_rate >=", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateLessThan(BigDecimal value) {
            addCriterion("clicking_rate <", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("clicking_rate <=", value, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateIn(List<BigDecimal> values) {
            addCriterion("clicking_rate in", values, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateNotIn(List<BigDecimal> values) {
            addCriterion("clicking_rate not in", values, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("clicking_rate between", value1, value2, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andClickingRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("clicking_rate not between", value1, value2, "clickingRate");
            return (Criteria) this;
        }

        public Criteria andNowpriceIsNull() {
            addCriterion("nowprice is null");
            return (Criteria) this;
        }

        public Criteria andNowpriceIsNotNull() {
            addCriterion("nowprice is not null");
            return (Criteria) this;
        }

        public Criteria andNowpriceEqualTo(BigDecimal value) {
            addCriterion("nowprice =", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceNotEqualTo(BigDecimal value) {
            addCriterion("nowprice <>", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceGreaterThan(BigDecimal value) {
            addCriterion("nowprice >", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("nowprice >=", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceLessThan(BigDecimal value) {
            addCriterion("nowprice <", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("nowprice <=", value, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceIn(List<BigDecimal> values) {
            addCriterion("nowprice in", values, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceNotIn(List<BigDecimal> values) {
            addCriterion("nowprice not in", values, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nowprice between", value1, value2, "nowprice");
            return (Criteria) this;
        }

        public Criteria andNowpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("nowprice not between", value1, value2, "nowprice");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationIsNull() {
            addCriterion("price_fluctuation is null");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationIsNotNull() {
            addCriterion("price_fluctuation is not null");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationEqualTo(BigDecimal value) {
            addCriterion("price_fluctuation =", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationNotEqualTo(BigDecimal value) {
            addCriterion("price_fluctuation <>", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationGreaterThan(BigDecimal value) {
            addCriterion("price_fluctuation >", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price_fluctuation >=", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationLessThan(BigDecimal value) {
            addCriterion("price_fluctuation <", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price_fluctuation <=", value, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationIn(List<BigDecimal> values) {
            addCriterion("price_fluctuation in", values, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationNotIn(List<BigDecimal> values) {
            addCriterion("price_fluctuation not in", values, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_fluctuation between", value1, value2, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andPriceFluctuationNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_fluctuation not between", value1, value2, "priceFluctuation");
            return (Criteria) this;
        }

        public Criteria andVolumeIsNull() {
            addCriterion("volume is null");
            return (Criteria) this;
        }

        public Criteria andVolumeIsNotNull() {
            addCriterion("volume is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeEqualTo(Integer value) {
            addCriterion("volume =", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotEqualTo(Integer value) {
            addCriterion("volume <>", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeGreaterThan(Integer value) {
            addCriterion("volume >", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeGreaterThanOrEqualTo(Integer value) {
            addCriterion("volume >=", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeLessThan(Integer value) {
            addCriterion("volume <", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeLessThanOrEqualTo(Integer value) {
            addCriterion("volume <=", value, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeIn(List<Integer> values) {
            addCriterion("volume in", values, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotIn(List<Integer> values) {
            addCriterion("volume not in", values, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeBetween(Integer value1, Integer value2) {
            addCriterion("volume between", value1, value2, "volume");
            return (Criteria) this;
        }

        public Criteria andVolumeNotBetween(Integer value1, Integer value2) {
            addCriterion("volume not between", value1, value2, "volume");
            return (Criteria) this;
        }

        public Criteria andCirculationIsNull() {
            addCriterion("Circulation is null");
            return (Criteria) this;
        }

        public Criteria andCirculationIsNotNull() {
            addCriterion("Circulation is not null");
            return (Criteria) this;
        }

        public Criteria andCirculationEqualTo(Integer value) {
            addCriterion("Circulation =", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotEqualTo(Integer value) {
            addCriterion("Circulation <>", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationGreaterThan(Integer value) {
            addCriterion("Circulation >", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationGreaterThanOrEqualTo(Integer value) {
            addCriterion("Circulation >=", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationLessThan(Integer value) {
            addCriterion("Circulation <", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationLessThanOrEqualTo(Integer value) {
            addCriterion("Circulation <=", value, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationIn(List<Integer> values) {
            addCriterion("Circulation in", values, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotIn(List<Integer> values) {
            addCriterion("Circulation not in", values, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationBetween(Integer value1, Integer value2) {
            addCriterion("Circulation between", value1, value2, "circulation");
            return (Criteria) this;
        }

        public Criteria andCirculationNotBetween(Integer value1, Integer value2) {
            addCriterion("Circulation not between", value1, value2, "circulation");
            return (Criteria) this;
        }

        public Criteria andIndextextIsNull() {
            addCriterion("indextext is null");
            return (Criteria) this;
        }

        public Criteria andIndextextIsNotNull() {
            addCriterion("indextext is not null");
            return (Criteria) this;
        }

        public Criteria andIndextextEqualTo(String value) {
            addCriterion("indextext =", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextNotEqualTo(String value) {
            addCriterion("indextext <>", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextGreaterThan(String value) {
            addCriterion("indextext >", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextGreaterThanOrEqualTo(String value) {
            addCriterion("indextext >=", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextLessThan(String value) {
            addCriterion("indextext <", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextLessThanOrEqualTo(String value) {
            addCriterion("indextext <=", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextLike(String value) {
            addCriterion("indextext like", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextNotLike(String value) {
            addCriterion("indextext not like", value, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextIn(List<String> values) {
            addCriterion("indextext in", values, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextNotIn(List<String> values) {
            addCriterion("indextext not in", values, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextBetween(String value1, String value2) {
            addCriterion("indextext between", value1, value2, "indextext");
            return (Criteria) this;
        }

        public Criteria andIndextextNotBetween(String value1, String value2) {
            addCriterion("indextext not between", value1, value2, "indextext");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIsNull() {
            addCriterion("marketprice is null");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIsNotNull() {
            addCriterion("marketprice is not null");
            return (Criteria) this;
        }

        public Criteria andMarketpriceEqualTo(BigDecimal value) {
            addCriterion("marketprice =", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotEqualTo(BigDecimal value) {
            addCriterion("marketprice <>", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceGreaterThan(BigDecimal value) {
            addCriterion("marketprice >", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("marketprice >=", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceLessThan(BigDecimal value) {
            addCriterion("marketprice <", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("marketprice <=", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIn(List<BigDecimal> values) {
            addCriterion("marketprice in", values, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotIn(List<BigDecimal> values) {
            addCriterion("marketprice not in", values, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("marketprice between", value1, value2, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("marketprice not between", value1, value2, "marketprice");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkIsNull() {
            addCriterion("recommonemark is null");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkIsNotNull() {
            addCriterion("recommonemark is not null");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkEqualTo(BigDecimal value) {
            addCriterion("recommonemark =", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkNotEqualTo(BigDecimal value) {
            addCriterion("recommonemark <>", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkGreaterThan(BigDecimal value) {
            addCriterion("recommonemark >", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("recommonemark >=", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkLessThan(BigDecimal value) {
            addCriterion("recommonemark <", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkLessThanOrEqualTo(BigDecimal value) {
            addCriterion("recommonemark <=", value, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkIn(List<BigDecimal> values) {
            addCriterion("recommonemark in", values, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkNotIn(List<BigDecimal> values) {
            addCriterion("recommonemark not in", values, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recommonemark between", value1, value2, "recommonemark");
            return (Criteria) this;
        }

        public Criteria andRecommonemarkNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("recommonemark not between", value1, value2, "recommonemark");
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