package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TbUsersAccountChangeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbUsersAccountChangeExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNull() {
            addCriterion("income is null");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNotNull() {
            addCriterion("income is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeEqualTo(BigDecimal value) {
            addCriterion("income =", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotEqualTo(BigDecimal value) {
            addCriterion("income <>", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThan(BigDecimal value) {
            addCriterion("income >", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income >=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThan(BigDecimal value) {
            addCriterion("income <", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income <=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeIn(List<BigDecimal> values) {
            addCriterion("income in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotIn(List<BigDecimal> values) {
            addCriterion("income not in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income not between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andExpendIsNull() {
            addCriterion("expend is null");
            return (Criteria) this;
        }

        public Criteria andExpendIsNotNull() {
            addCriterion("expend is not null");
            return (Criteria) this;
        }

        public Criteria andExpendEqualTo(BigDecimal value) {
            addCriterion("expend =", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotEqualTo(BigDecimal value) {
            addCriterion("expend <>", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendGreaterThan(BigDecimal value) {
            addCriterion("expend >", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("expend >=", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendLessThan(BigDecimal value) {
            addCriterion("expend <", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendLessThanOrEqualTo(BigDecimal value) {
            addCriterion("expend <=", value, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendIn(List<BigDecimal> values) {
            addCriterion("expend in", values, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotIn(List<BigDecimal> values) {
            addCriterion("expend not in", values, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend between", value1, value2, "expend");
            return (Criteria) this;
        }

        public Criteria andExpendNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("expend not between", value1, value2, "expend");
            return (Criteria) this;
        }

        public Criteria andTdayIsNull() {
            addCriterion("tday is null");
            return (Criteria) this;
        }

        public Criteria andTdayIsNotNull() {
            addCriterion("tday is not null");
            return (Criteria) this;
        }

        public Criteria andTdayEqualTo(Long value) {
            addCriterion("tday =", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayNotEqualTo(Long value) {
            addCriterion("tday <>", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayGreaterThan(Long value) {
            addCriterion("tday >", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayGreaterThanOrEqualTo(Long value) {
            addCriterion("tday >=", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayLessThan(Long value) {
            addCriterion("tday <", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayLessThanOrEqualTo(Long value) {
            addCriterion("tday <=", value, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayIn(List<Long> values) {
            addCriterion("tday in", values, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayNotIn(List<Long> values) {
            addCriterion("tday not in", values, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayBetween(Long value1, Long value2) {
            addCriterion("tday between", value1, value2, "tday");
            return (Criteria) this;
        }

        public Criteria andTdayNotBetween(Long value1, Long value2) {
            addCriterion("tday not between", value1, value2, "tday");
            return (Criteria) this;
        }

        public Criteria andYuebeforeIsNull() {
            addCriterion("yuebefore is null");
            return (Criteria) this;
        }

        public Criteria andYuebeforeIsNotNull() {
            addCriterion("yuebefore is not null");
            return (Criteria) this;
        }

        public Criteria andYuebeforeEqualTo(BigDecimal value) {
            addCriterion("yuebefore =", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeNotEqualTo(BigDecimal value) {
            addCriterion("yuebefore <>", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeGreaterThan(BigDecimal value) {
            addCriterion("yuebefore >", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("yuebefore >=", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeLessThan(BigDecimal value) {
            addCriterion("yuebefore <", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("yuebefore <=", value, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeIn(List<BigDecimal> values) {
            addCriterion("yuebefore in", values, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeNotIn(List<BigDecimal> values) {
            addCriterion("yuebefore not in", values, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("yuebefore between", value1, value2, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuebeforeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("yuebefore not between", value1, value2, "yuebefore");
            return (Criteria) this;
        }

        public Criteria andYuealterIsNull() {
            addCriterion("yuealter is null");
            return (Criteria) this;
        }

        public Criteria andYuealterIsNotNull() {
            addCriterion("yuealter is not null");
            return (Criteria) this;
        }

        public Criteria andYuealterEqualTo(BigDecimal value) {
            addCriterion("yuealter =", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterNotEqualTo(BigDecimal value) {
            addCriterion("yuealter <>", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterGreaterThan(BigDecimal value) {
            addCriterion("yuealter >", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("yuealter >=", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterLessThan(BigDecimal value) {
            addCriterion("yuealter <", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterLessThanOrEqualTo(BigDecimal value) {
            addCriterion("yuealter <=", value, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterIn(List<BigDecimal> values) {
            addCriterion("yuealter in", values, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterNotIn(List<BigDecimal> values) {
            addCriterion("yuealter not in", values, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("yuealter between", value1, value2, "yuealter");
            return (Criteria) this;
        }

        public Criteria andYuealterNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("yuealter not between", value1, value2, "yuealter");
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

        public Criteria andOrderidIsNull() {
            addCriterion("orderid is null");
            return (Criteria) this;
        }

        public Criteria andOrderidIsNotNull() {
            addCriterion("orderid is not null");
            return (Criteria) this;
        }

        public Criteria andOrderidEqualTo(String value) {
            addCriterion("orderid =", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotEqualTo(String value) {
            addCriterion("orderid <>", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThan(String value) {
            addCriterion("orderid >", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThanOrEqualTo(String value) {
            addCriterion("orderid >=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThan(String value) {
            addCriterion("orderid <", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThanOrEqualTo(String value) {
            addCriterion("orderid <=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLike(String value) {
            addCriterion("orderid like", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotLike(String value) {
            addCriterion("orderid not like", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidIn(List<String> values) {
            addCriterion("orderid in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotIn(List<String> values) {
            addCriterion("orderid not in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidBetween(String value1, String value2) {
            addCriterion("orderid between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotBetween(String value1, String value2) {
            addCriterion("orderid not between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andRechargeidIsNull() {
            addCriterion("rechargeid is null");
            return (Criteria) this;
        }

        public Criteria andRechargeidIsNotNull() {
            addCriterion("rechargeid is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeidEqualTo(Integer value) {
            addCriterion("rechargeid =", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidNotEqualTo(Integer value) {
            addCriterion("rechargeid <>", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidGreaterThan(Integer value) {
            addCriterion("rechargeid >", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidGreaterThanOrEqualTo(Integer value) {
            addCriterion("rechargeid >=", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidLessThan(Integer value) {
            addCriterion("rechargeid <", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidLessThanOrEqualTo(Integer value) {
            addCriterion("rechargeid <=", value, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidIn(List<Integer> values) {
            addCriterion("rechargeid in", values, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidNotIn(List<Integer> values) {
            addCriterion("rechargeid not in", values, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidBetween(Integer value1, Integer value2) {
            addCriterion("rechargeid between", value1, value2, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andRechargeidNotBetween(Integer value1, Integer value2) {
            addCriterion("rechargeid not between", value1, value2, "rechargeid");
            return (Criteria) this;
        }

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(Integer value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(Integer value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(Integer value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(Integer value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(Integer value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(Integer value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<Integer> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<Integer> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(Integer value1, Integer value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(Integer value1, Integer value2) {
            addCriterion("model not between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("comment is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("comment is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(String value) {
            addCriterion("comment =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(String value) {
            addCriterion("comment <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(String value) {
            addCriterion("comment >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(String value) {
            addCriterion("comment >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(String value) {
            addCriterion("comment <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(String value) {
            addCriterion("comment <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLike(String value) {
            addCriterion("comment like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotLike(String value) {
            addCriterion("comment not like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<String> values) {
            addCriterion("comment in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<String> values) {
            addCriterion("comment not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(String value1, String value2) {
            addCriterion("comment between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(String value1, String value2) {
            addCriterion("comment not between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andYuedirectionIsNull() {
            addCriterion("yuedirection is null");
            return (Criteria) this;
        }

        public Criteria andYuedirectionIsNotNull() {
            addCriterion("yuedirection is not null");
            return (Criteria) this;
        }

        public Criteria andYuedirectionEqualTo(Integer value) {
            addCriterion("yuedirection =", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionNotEqualTo(Integer value) {
            addCriterion("yuedirection <>", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionGreaterThan(Integer value) {
            addCriterion("yuedirection >", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionGreaterThanOrEqualTo(Integer value) {
            addCriterion("yuedirection >=", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionLessThan(Integer value) {
            addCriterion("yuedirection <", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionLessThanOrEqualTo(Integer value) {
            addCriterion("yuedirection <=", value, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionIn(List<Integer> values) {
            addCriterion("yuedirection in", values, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionNotIn(List<Integer> values) {
            addCriterion("yuedirection not in", values, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionBetween(Integer value1, Integer value2) {
            addCriterion("yuedirection between", value1, value2, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andYuedirectionNotBetween(Integer value1, Integer value2) {
            addCriterion("yuedirection not between", value1, value2, "yuedirection");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDongjieafterIsNull() {
            addCriterion("dongjieafter is null");
            return (Criteria) this;
        }

        public Criteria andDongjieafterIsNotNull() {
            addCriterion("dongjieafter is not null");
            return (Criteria) this;
        }

        public Criteria andDongjieafterEqualTo(BigDecimal value) {
            addCriterion("dongjieafter =", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterNotEqualTo(BigDecimal value) {
            addCriterion("dongjieafter <>", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterGreaterThan(BigDecimal value) {
            addCriterion("dongjieafter >", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("dongjieafter >=", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterLessThan(BigDecimal value) {
            addCriterion("dongjieafter <", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterLessThanOrEqualTo(BigDecimal value) {
            addCriterion("dongjieafter <=", value, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterIn(List<BigDecimal> values) {
            addCriterion("dongjieafter in", values, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterNotIn(List<BigDecimal> values) {
            addCriterion("dongjieafter not in", values, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dongjieafter between", value1, value2, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjieafterNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dongjieafter not between", value1, value2, "dongjieafter");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeIsNull() {
            addCriterion("dongjiebefore is null");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeIsNotNull() {
            addCriterion("dongjiebefore is not null");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeEqualTo(BigDecimal value) {
            addCriterion("dongjiebefore =", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeNotEqualTo(BigDecimal value) {
            addCriterion("dongjiebefore <>", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeGreaterThan(BigDecimal value) {
            addCriterion("dongjiebefore >", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("dongjiebefore >=", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeLessThan(BigDecimal value) {
            addCriterion("dongjiebefore <", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("dongjiebefore <=", value, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeIn(List<BigDecimal> values) {
            addCriterion("dongjiebefore in", values, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeNotIn(List<BigDecimal> values) {
            addCriterion("dongjiebefore not in", values, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dongjiebefore between", value1, value2, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiebeforeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dongjiebefore not between", value1, value2, "dongjiebefore");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionIsNull() {
            addCriterion("dongjiedirection is null");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionIsNotNull() {
            addCriterion("dongjiedirection is not null");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionEqualTo(Integer value) {
            addCriterion("dongjiedirection =", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionNotEqualTo(Integer value) {
            addCriterion("dongjiedirection <>", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionGreaterThan(Integer value) {
            addCriterion("dongjiedirection >", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionGreaterThanOrEqualTo(Integer value) {
            addCriterion("dongjiedirection >=", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionLessThan(Integer value) {
            addCriterion("dongjiedirection <", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionLessThanOrEqualTo(Integer value) {
            addCriterion("dongjiedirection <=", value, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionIn(List<Integer> values) {
            addCriterion("dongjiedirection in", values, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionNotIn(List<Integer> values) {
            addCriterion("dongjiedirection not in", values, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionBetween(Integer value1, Integer value2) {
            addCriterion("dongjiedirection between", value1, value2, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andDongjiedirectionNotBetween(Integer value1, Integer value2) {
            addCriterion("dongjiedirection not between", value1, value2, "dongjiedirection");
            return (Criteria) this;
        }

        public Criteria andMajororganidIsNull() {
            addCriterion("majororganid is null");
            return (Criteria) this;
        }

        public Criteria andMajororganidIsNotNull() {
            addCriterion("majororganid is not null");
            return (Criteria) this;
        }

        public Criteria andMajororganidEqualTo(Long value) {
            addCriterion("majororganid =", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidNotEqualTo(Long value) {
            addCriterion("majororganid <>", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidGreaterThan(Long value) {
            addCriterion("majororganid >", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidGreaterThanOrEqualTo(Long value) {
            addCriterion("majororganid >=", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidLessThan(Long value) {
            addCriterion("majororganid <", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidLessThanOrEqualTo(Long value) {
            addCriterion("majororganid <=", value, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidIn(List<Long> values) {
            addCriterion("majororganid in", values, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidNotIn(List<Long> values) {
            addCriterion("majororganid not in", values, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidBetween(Long value1, Long value2) {
            addCriterion("majororganid between", value1, value2, "majororganid");
            return (Criteria) this;
        }

        public Criteria andMajororganidNotBetween(Long value1, Long value2) {
            addCriterion("majororganid not between", value1, value2, "majororganid");
            return (Criteria) this;
        }

        public Criteria andOrganidIsNull() {
            addCriterion("organid is null");
            return (Criteria) this;
        }

        public Criteria andOrganidIsNotNull() {
            addCriterion("organid is not null");
            return (Criteria) this;
        }

        public Criteria andOrganidEqualTo(Long value) {
            addCriterion("organid =", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotEqualTo(Long value) {
            addCriterion("organid <>", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidGreaterThan(Long value) {
            addCriterion("organid >", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidGreaterThanOrEqualTo(Long value) {
            addCriterion("organid >=", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidLessThan(Long value) {
            addCriterion("organid <", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidLessThanOrEqualTo(Long value) {
            addCriterion("organid <=", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidIn(List<Long> values) {
            addCriterion("organid in", values, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotIn(List<Long> values) {
            addCriterion("organid not in", values, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidBetween(Long value1, Long value2) {
            addCriterion("organid between", value1, value2, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotBetween(Long value1, Long value2) {
            addCriterion("organid not between", value1, value2, "organid");
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