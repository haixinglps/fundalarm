package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TbRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbRecordExample() {
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
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
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

        public Criteria andSafetypeIsNull() {
            addCriterion("safetype is null");
            return (Criteria) this;
        }

        public Criteria andSafetypeIsNotNull() {
            addCriterion("safetype is not null");
            return (Criteria) this;
        }

        public Criteria andSafetypeEqualTo(String value) {
            addCriterion("safetype =", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotEqualTo(String value) {
            addCriterion("safetype <>", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeGreaterThan(String value) {
            addCriterion("safetype >", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeGreaterThanOrEqualTo(String value) {
            addCriterion("safetype >=", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLessThan(String value) {
            addCriterion("safetype <", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLessThanOrEqualTo(String value) {
            addCriterion("safetype <=", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLike(String value) {
            addCriterion("safetype like", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotLike(String value) {
            addCriterion("safetype not like", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeIn(List<String> values) {
            addCriterion("safetype in", values, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotIn(List<String> values) {
            addCriterion("safetype not in", values, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeBetween(String value1, String value2) {
            addCriterion("safetype between", value1, value2, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotBetween(String value1, String value2) {
            addCriterion("safetype not between", value1, value2, "safetype");
            return (Criteria) this;
        }

        public Criteria andPolicyidIsNull() {
            addCriterion("policyid is null");
            return (Criteria) this;
        }

        public Criteria andPolicyidIsNotNull() {
            addCriterion("policyid is not null");
            return (Criteria) this;
        }

        public Criteria andPolicyidEqualTo(Integer value) {
            addCriterion("policyid =", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotEqualTo(Integer value) {
            addCriterion("policyid <>", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidGreaterThan(Integer value) {
            addCriterion("policyid >", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidGreaterThanOrEqualTo(Integer value) {
            addCriterion("policyid >=", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidLessThan(Integer value) {
            addCriterion("policyid <", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidLessThanOrEqualTo(Integer value) {
            addCriterion("policyid <=", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidIn(List<Integer> values) {
            addCriterion("policyid in", values, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotIn(List<Integer> values) {
            addCriterion("policyid not in", values, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidBetween(Integer value1, Integer value2) {
            addCriterion("policyid between", value1, value2, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotBetween(Integer value1, Integer value2) {
            addCriterion("policyid not between", value1, value2, "policyid");
            return (Criteria) this;
        }

        public Criteria andReasonIsNull() {
            addCriterion("reason is null");
            return (Criteria) this;
        }

        public Criteria andReasonIsNotNull() {
            addCriterion("reason is not null");
            return (Criteria) this;
        }

        public Criteria andReasonEqualTo(String value) {
            addCriterion("reason =", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotEqualTo(String value) {
            addCriterion("reason <>", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThan(String value) {
            addCriterion("reason >", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThanOrEqualTo(String value) {
            addCriterion("reason >=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThan(String value) {
            addCriterion("reason <", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThanOrEqualTo(String value) {
            addCriterion("reason <=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLike(String value) {
            addCriterion("reason like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotLike(String value) {
            addCriterion("reason not like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonIn(List<String> values) {
            addCriterion("reason in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotIn(List<String> values) {
            addCriterion("reason not in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonBetween(String value1, String value2) {
            addCriterion("reason between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotBetween(String value1, String value2) {
            addCriterion("reason not between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andEventtimeIsNull() {
            addCriterion("eventtime is null");
            return (Criteria) this;
        }

        public Criteria andEventtimeIsNotNull() {
            addCriterion("eventtime is not null");
            return (Criteria) this;
        }

        public Criteria andEventtimeEqualTo(Long value) {
            addCriterion("eventtime =", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeNotEqualTo(Long value) {
            addCriterion("eventtime <>", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeGreaterThan(Long value) {
            addCriterion("eventtime >", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeGreaterThanOrEqualTo(Long value) {
            addCriterion("eventtime >=", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeLessThan(Long value) {
            addCriterion("eventtime <", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeLessThanOrEqualTo(Long value) {
            addCriterion("eventtime <=", value, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeIn(List<Long> values) {
            addCriterion("eventtime in", values, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeNotIn(List<Long> values) {
            addCriterion("eventtime not in", values, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeBetween(Long value1, Long value2) {
            addCriterion("eventtime between", value1, value2, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventtimeNotBetween(Long value1, Long value2) {
            addCriterion("eventtime not between", value1, value2, "eventtime");
            return (Criteria) this;
        }

        public Criteria andEventaddressIsNull() {
            addCriterion("eventaddress is null");
            return (Criteria) this;
        }

        public Criteria andEventaddressIsNotNull() {
            addCriterion("eventaddress is not null");
            return (Criteria) this;
        }

        public Criteria andEventaddressEqualTo(Integer value) {
            addCriterion("eventaddress =", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressNotEqualTo(Integer value) {
            addCriterion("eventaddress <>", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressGreaterThan(Integer value) {
            addCriterion("eventaddress >", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressGreaterThanOrEqualTo(Integer value) {
            addCriterion("eventaddress >=", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressLessThan(Integer value) {
            addCriterion("eventaddress <", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressLessThanOrEqualTo(Integer value) {
            addCriterion("eventaddress <=", value, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressIn(List<Integer> values) {
            addCriterion("eventaddress in", values, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressNotIn(List<Integer> values) {
            addCriterion("eventaddress not in", values, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressBetween(Integer value1, Integer value2) {
            addCriterion("eventaddress between", value1, value2, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andEventaddressNotBetween(Integer value1, Integer value2) {
            addCriterion("eventaddress not between", value1, value2, "eventaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressIsNull() {
            addCriterion("detailaddress is null");
            return (Criteria) this;
        }

        public Criteria andDetailaddressIsNotNull() {
            addCriterion("detailaddress is not null");
            return (Criteria) this;
        }

        public Criteria andDetailaddressEqualTo(String value) {
            addCriterion("detailaddress =", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressNotEqualTo(String value) {
            addCriterion("detailaddress <>", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressGreaterThan(String value) {
            addCriterion("detailaddress >", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressGreaterThanOrEqualTo(String value) {
            addCriterion("detailaddress >=", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressLessThan(String value) {
            addCriterion("detailaddress <", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressLessThanOrEqualTo(String value) {
            addCriterion("detailaddress <=", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressLike(String value) {
            addCriterion("detailaddress like", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressNotLike(String value) {
            addCriterion("detailaddress not like", value, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressIn(List<String> values) {
            addCriterion("detailaddress in", values, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressNotIn(List<String> values) {
            addCriterion("detailaddress not in", values, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressBetween(String value1, String value2) {
            addCriterion("detailaddress between", value1, value2, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddressNotBetween(String value1, String value2) {
            addCriterion("detailaddress not between", value1, value2, "detailaddress");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2IsNull() {
            addCriterion("detailaddress2 is null");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2IsNotNull() {
            addCriterion("detailaddress2 is not null");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2EqualTo(String value) {
            addCriterion("detailaddress2 =", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2NotEqualTo(String value) {
            addCriterion("detailaddress2 <>", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2GreaterThan(String value) {
            addCriterion("detailaddress2 >", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2GreaterThanOrEqualTo(String value) {
            addCriterion("detailaddress2 >=", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2LessThan(String value) {
            addCriterion("detailaddress2 <", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2LessThanOrEqualTo(String value) {
            addCriterion("detailaddress2 <=", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2Like(String value) {
            addCriterion("detailaddress2 like", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2NotLike(String value) {
            addCriterion("detailaddress2 not like", value, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2In(List<String> values) {
            addCriterion("detailaddress2 in", values, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2NotIn(List<String> values) {
            addCriterion("detailaddress2 not in", values, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2Between(String value1, String value2) {
            addCriterion("detailaddress2 between", value1, value2, "detailaddress2");
            return (Criteria) this;
        }

        public Criteria andDetailaddress2NotBetween(String value1, String value2) {
            addCriterion("detailaddress2 not between", value1, value2, "detailaddress2");
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

        public Criteria andSunshiIsNull() {
            addCriterion("sunshi is null");
            return (Criteria) this;
        }

        public Criteria andSunshiIsNotNull() {
            addCriterion("sunshi is not null");
            return (Criteria) this;
        }

        public Criteria andSunshiEqualTo(BigDecimal value) {
            addCriterion("sunshi =", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiNotEqualTo(BigDecimal value) {
            addCriterion("sunshi <>", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiGreaterThan(BigDecimal value) {
            addCriterion("sunshi >", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sunshi >=", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiLessThan(BigDecimal value) {
            addCriterion("sunshi <", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sunshi <=", value, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiIn(List<BigDecimal> values) {
            addCriterion("sunshi in", values, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiNotIn(List<BigDecimal> values) {
            addCriterion("sunshi not in", values, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sunshi between", value1, value2, "sunshi");
            return (Criteria) this;
        }

        public Criteria andSunshiNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sunshi not between", value1, value2, "sunshi");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Long value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Long value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Long value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Long value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Long value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Long value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Long> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Long> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Long value1, Long value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Long value1, Long value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiIsNull() {
            addCriterion("unitofshunshi is null");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiIsNotNull() {
            addCriterion("unitofshunshi is not null");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiEqualTo(Integer value) {
            addCriterion("unitofshunshi =", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiNotEqualTo(Integer value) {
            addCriterion("unitofshunshi <>", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiGreaterThan(Integer value) {
            addCriterion("unitofshunshi >", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiGreaterThanOrEqualTo(Integer value) {
            addCriterion("unitofshunshi >=", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiLessThan(Integer value) {
            addCriterion("unitofshunshi <", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiLessThanOrEqualTo(Integer value) {
            addCriterion("unitofshunshi <=", value, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiIn(List<Integer> values) {
            addCriterion("unitofshunshi in", values, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiNotIn(List<Integer> values) {
            addCriterion("unitofshunshi not in", values, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiBetween(Integer value1, Integer value2) {
            addCriterion("unitofshunshi between", value1, value2, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andUnitofshunshiNotBetween(Integer value1, Integer value2) {
            addCriterion("unitofshunshi not between", value1, value2, "unitofshunshi");
            return (Criteria) this;
        }

        public Criteria andHumingIsNull() {
            addCriterion("huming is null");
            return (Criteria) this;
        }

        public Criteria andHumingIsNotNull() {
            addCriterion("huming is not null");
            return (Criteria) this;
        }

        public Criteria andHumingEqualTo(String value) {
            addCriterion("huming =", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingNotEqualTo(String value) {
            addCriterion("huming <>", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingGreaterThan(String value) {
            addCriterion("huming >", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingGreaterThanOrEqualTo(String value) {
            addCriterion("huming >=", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingLessThan(String value) {
            addCriterion("huming <", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingLessThanOrEqualTo(String value) {
            addCriterion("huming <=", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingLike(String value) {
            addCriterion("huming like", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingNotLike(String value) {
            addCriterion("huming not like", value, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingIn(List<String> values) {
            addCriterion("huming in", values, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingNotIn(List<String> values) {
            addCriterion("huming not in", values, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingBetween(String value1, String value2) {
            addCriterion("huming between", value1, value2, "huming");
            return (Criteria) this;
        }

        public Criteria andHumingNotBetween(String value1, String value2) {
            addCriterion("huming not between", value1, value2, "huming");
            return (Criteria) this;
        }

        public Criteria andZhanghuIsNull() {
            addCriterion("zhanghu is null");
            return (Criteria) this;
        }

        public Criteria andZhanghuIsNotNull() {
            addCriterion("zhanghu is not null");
            return (Criteria) this;
        }

        public Criteria andZhanghuEqualTo(String value) {
            addCriterion("zhanghu =", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuNotEqualTo(String value) {
            addCriterion("zhanghu <>", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuGreaterThan(String value) {
            addCriterion("zhanghu >", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuGreaterThanOrEqualTo(String value) {
            addCriterion("zhanghu >=", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuLessThan(String value) {
            addCriterion("zhanghu <", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuLessThanOrEqualTo(String value) {
            addCriterion("zhanghu <=", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuLike(String value) {
            addCriterion("zhanghu like", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuNotLike(String value) {
            addCriterion("zhanghu not like", value, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuIn(List<String> values) {
            addCriterion("zhanghu in", values, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuNotIn(List<String> values) {
            addCriterion("zhanghu not in", values, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuBetween(String value1, String value2) {
            addCriterion("zhanghu between", value1, value2, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andZhanghuNotBetween(String value1, String value2) {
            addCriterion("zhanghu not between", value1, value2, "zhanghu");
            return (Criteria) this;
        }

        public Criteria andKaihuhangIsNull() {
            addCriterion("kaihuhang is null");
            return (Criteria) this;
        }

        public Criteria andKaihuhangIsNotNull() {
            addCriterion("kaihuhang is not null");
            return (Criteria) this;
        }

        public Criteria andKaihuhangEqualTo(String value) {
            addCriterion("kaihuhang =", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangNotEqualTo(String value) {
            addCriterion("kaihuhang <>", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangGreaterThan(String value) {
            addCriterion("kaihuhang >", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangGreaterThanOrEqualTo(String value) {
            addCriterion("kaihuhang >=", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangLessThan(String value) {
            addCriterion("kaihuhang <", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangLessThanOrEqualTo(String value) {
            addCriterion("kaihuhang <=", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangLike(String value) {
            addCriterion("kaihuhang like", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangNotLike(String value) {
            addCriterion("kaihuhang not like", value, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangIn(List<String> values) {
            addCriterion("kaihuhang in", values, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangNotIn(List<String> values) {
            addCriterion("kaihuhang not in", values, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangBetween(String value1, String value2) {
            addCriterion("kaihuhang between", value1, value2, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andKaihuhangNotBetween(String value1, String value2) {
            addCriterion("kaihuhang not between", value1, value2, "kaihuhang");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andImagesIsNull() {
            addCriterion("images is null");
            return (Criteria) this;
        }

        public Criteria andImagesIsNotNull() {
            addCriterion("images is not null");
            return (Criteria) this;
        }

        public Criteria andImagesEqualTo(String value) {
            addCriterion("images =", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesNotEqualTo(String value) {
            addCriterion("images <>", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesGreaterThan(String value) {
            addCriterion("images >", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesGreaterThanOrEqualTo(String value) {
            addCriterion("images >=", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesLessThan(String value) {
            addCriterion("images <", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesLessThanOrEqualTo(String value) {
            addCriterion("images <=", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesLike(String value) {
            addCriterion("images like", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesNotLike(String value) {
            addCriterion("images not like", value, "images");
            return (Criteria) this;
        }

        public Criteria andImagesIn(List<String> values) {
            addCriterion("images in", values, "images");
            return (Criteria) this;
        }

        public Criteria andImagesNotIn(List<String> values) {
            addCriterion("images not in", values, "images");
            return (Criteria) this;
        }

        public Criteria andImagesBetween(String value1, String value2) {
            addCriterion("images between", value1, value2, "images");
            return (Criteria) this;
        }

        public Criteria andImagesNotBetween(String value1, String value2) {
            addCriterion("images not between", value1, value2, "images");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanIsNull() {
            addCriterion("zhuanyuan is null");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanIsNotNull() {
            addCriterion("zhuanyuan is not null");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanEqualTo(String value) {
            addCriterion("zhuanyuan =", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanNotEqualTo(String value) {
            addCriterion("zhuanyuan <>", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanGreaterThan(String value) {
            addCriterion("zhuanyuan >", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanGreaterThanOrEqualTo(String value) {
            addCriterion("zhuanyuan >=", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanLessThan(String value) {
            addCriterion("zhuanyuan <", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanLessThanOrEqualTo(String value) {
            addCriterion("zhuanyuan <=", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanLike(String value) {
            addCriterion("zhuanyuan like", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanNotLike(String value) {
            addCriterion("zhuanyuan not like", value, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanIn(List<String> values) {
            addCriterion("zhuanyuan in", values, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanNotIn(List<String> values) {
            addCriterion("zhuanyuan not in", values, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanBetween(String value1, String value2) {
            addCriterion("zhuanyuan between", value1, value2, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanNotBetween(String value1, String value2) {
            addCriterion("zhuanyuan not between", value1, value2, "zhuanyuan");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneIsNull() {
            addCriterion("zhuanyuanphone is null");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneIsNotNull() {
            addCriterion("zhuanyuanphone is not null");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneEqualTo(String value) {
            addCriterion("zhuanyuanphone =", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneNotEqualTo(String value) {
            addCriterion("zhuanyuanphone <>", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneGreaterThan(String value) {
            addCriterion("zhuanyuanphone >", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneGreaterThanOrEqualTo(String value) {
            addCriterion("zhuanyuanphone >=", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneLessThan(String value) {
            addCriterion("zhuanyuanphone <", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneLessThanOrEqualTo(String value) {
            addCriterion("zhuanyuanphone <=", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneLike(String value) {
            addCriterion("zhuanyuanphone like", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneNotLike(String value) {
            addCriterion("zhuanyuanphone not like", value, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneIn(List<String> values) {
            addCriterion("zhuanyuanphone in", values, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneNotIn(List<String> values) {
            addCriterion("zhuanyuanphone not in", values, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneBetween(String value1, String value2) {
            addCriterion("zhuanyuanphone between", value1, value2, "zhuanyuanphone");
            return (Criteria) this;
        }

        public Criteria andZhuanyuanphoneNotBetween(String value1, String value2) {
            addCriterion("zhuanyuanphone not between", value1, value2, "zhuanyuanphone");
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

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Long value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Long value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Long value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Long value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Long value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Long> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Long> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Long value1, Long value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Long value1, Long value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Long value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Long value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Long value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Long value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Long value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Long> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Long> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Long value1, Long value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Long value1, Long value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andBegintimeIsNull() {
            addCriterion("begintime is null");
            return (Criteria) this;
        }

        public Criteria andBegintimeIsNotNull() {
            addCriterion("begintime is not null");
            return (Criteria) this;
        }

        public Criteria andBegintimeEqualTo(Long value) {
            addCriterion("begintime =", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotEqualTo(Long value) {
            addCriterion("begintime <>", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeGreaterThan(Long value) {
            addCriterion("begintime >", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeGreaterThanOrEqualTo(Long value) {
            addCriterion("begintime >=", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeLessThan(Long value) {
            addCriterion("begintime <", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeLessThanOrEqualTo(Long value) {
            addCriterion("begintime <=", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeIn(List<Long> values) {
            addCriterion("begintime in", values, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotIn(List<Long> values) {
            addCriterion("begintime not in", values, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeBetween(Long value1, Long value2) {
            addCriterion("begintime between", value1, value2, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotBetween(Long value1, Long value2) {
            addCriterion("begintime not between", value1, value2, "begintime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNull() {
            addCriterion("endtime is null");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNotNull() {
            addCriterion("endtime is not null");
            return (Criteria) this;
        }

        public Criteria andEndtimeEqualTo(Long value) {
            addCriterion("endtime =", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotEqualTo(Long value) {
            addCriterion("endtime <>", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThan(Long value) {
            addCriterion("endtime >", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThanOrEqualTo(Long value) {
            addCriterion("endtime >=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThan(Long value) {
            addCriterion("endtime <", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThanOrEqualTo(Long value) {
            addCriterion("endtime <=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIn(List<Long> values) {
            addCriterion("endtime in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotIn(List<Long> values) {
            addCriterion("endtime not in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeBetween(Long value1, Long value2) {
            addCriterion("endtime between", value1, value2, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotBetween(Long value1, Long value2) {
            addCriterion("endtime not between", value1, value2, "endtime");
            return (Criteria) this;
        }

        public Criteria andPeopleIsNull() {
            addCriterion("people is null");
            return (Criteria) this;
        }

        public Criteria andPeopleIsNotNull() {
            addCriterion("people is not null");
            return (Criteria) this;
        }

        public Criteria andPeopleEqualTo(String value) {
            addCriterion("people =", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleNotEqualTo(String value) {
            addCriterion("people <>", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleGreaterThan(String value) {
            addCriterion("people >", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleGreaterThanOrEqualTo(String value) {
            addCriterion("people >=", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleLessThan(String value) {
            addCriterion("people <", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleLessThanOrEqualTo(String value) {
            addCriterion("people <=", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleLike(String value) {
            addCriterion("people like", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleNotLike(String value) {
            addCriterion("people not like", value, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleIn(List<String> values) {
            addCriterion("people in", values, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleNotIn(List<String> values) {
            addCriterion("people not in", values, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleBetween(String value1, String value2) {
            addCriterion("people between", value1, value2, "people");
            return (Criteria) this;
        }

        public Criteria andPeopleNotBetween(String value1, String value2) {
            addCriterion("people not between", value1, value2, "people");
            return (Criteria) this;
        }

        public Criteria andAnjianidIsNull() {
            addCriterion("anjianid is null");
            return (Criteria) this;
        }

        public Criteria andAnjianidIsNotNull() {
            addCriterion("anjianid is not null");
            return (Criteria) this;
        }

        public Criteria andAnjianidEqualTo(String value) {
            addCriterion("anjianid =", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidNotEqualTo(String value) {
            addCriterion("anjianid <>", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidGreaterThan(String value) {
            addCriterion("anjianid >", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidGreaterThanOrEqualTo(String value) {
            addCriterion("anjianid >=", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidLessThan(String value) {
            addCriterion("anjianid <", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidLessThanOrEqualTo(String value) {
            addCriterion("anjianid <=", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidLike(String value) {
            addCriterion("anjianid like", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidNotLike(String value) {
            addCriterion("anjianid not like", value, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidIn(List<String> values) {
            addCriterion("anjianid in", values, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidNotIn(List<String> values) {
            addCriterion("anjianid not in", values, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidBetween(String value1, String value2) {
            addCriterion("anjianid between", value1, value2, "anjianid");
            return (Criteria) this;
        }

        public Criteria andAnjianidNotBetween(String value1, String value2) {
            addCriterion("anjianid not between", value1, value2, "anjianid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidIsNull() {
            addCriterion("merchantpoclicyid is null");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidIsNotNull() {
            addCriterion("merchantpoclicyid is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidEqualTo(String value) {
            addCriterion("merchantpoclicyid =", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidNotEqualTo(String value) {
            addCriterion("merchantpoclicyid <>", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidGreaterThan(String value) {
            addCriterion("merchantpoclicyid >", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidGreaterThanOrEqualTo(String value) {
            addCriterion("merchantpoclicyid >=", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidLessThan(String value) {
            addCriterion("merchantpoclicyid <", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidLessThanOrEqualTo(String value) {
            addCriterion("merchantpoclicyid <=", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidLike(String value) {
            addCriterion("merchantpoclicyid like", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidNotLike(String value) {
            addCriterion("merchantpoclicyid not like", value, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidIn(List<String> values) {
            addCriterion("merchantpoclicyid in", values, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidNotIn(List<String> values) {
            addCriterion("merchantpoclicyid not in", values, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidBetween(String value1, String value2) {
            addCriterion("merchantpoclicyid between", value1, value2, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andMerchantpoclicyidNotBetween(String value1, String value2) {
            addCriterion("merchantpoclicyid not between", value1, value2, "merchantpoclicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidIsNull() {
            addCriterion("personpolicyid is null");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidIsNotNull() {
            addCriterion("personpolicyid is not null");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidEqualTo(String value) {
            addCriterion("personpolicyid =", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidNotEqualTo(String value) {
            addCriterion("personpolicyid <>", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidGreaterThan(String value) {
            addCriterion("personpolicyid >", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidGreaterThanOrEqualTo(String value) {
            addCriterion("personpolicyid >=", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidLessThan(String value) {
            addCriterion("personpolicyid <", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidLessThanOrEqualTo(String value) {
            addCriterion("personpolicyid <=", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidLike(String value) {
            addCriterion("personpolicyid like", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidNotLike(String value) {
            addCriterion("personpolicyid not like", value, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidIn(List<String> values) {
            addCriterion("personpolicyid in", values, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidNotIn(List<String> values) {
            addCriterion("personpolicyid not in", values, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidBetween(String value1, String value2) {
            addCriterion("personpolicyid between", value1, value2, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andPersonpolicyidNotBetween(String value1, String value2) {
            addCriterion("personpolicyid not between", value1, value2, "personpolicyid");
            return (Criteria) this;
        }

        public Criteria andJigouhaoIsNull() {
            addCriterion("jigouhao is null");
            return (Criteria) this;
        }

        public Criteria andJigouhaoIsNotNull() {
            addCriterion("jigouhao is not null");
            return (Criteria) this;
        }

        public Criteria andJigouhaoEqualTo(String value) {
            addCriterion("jigouhao =", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoNotEqualTo(String value) {
            addCriterion("jigouhao <>", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoGreaterThan(String value) {
            addCriterion("jigouhao >", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoGreaterThanOrEqualTo(String value) {
            addCriterion("jigouhao >=", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoLessThan(String value) {
            addCriterion("jigouhao <", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoLessThanOrEqualTo(String value) {
            addCriterion("jigouhao <=", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoLike(String value) {
            addCriterion("jigouhao like", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoNotLike(String value) {
            addCriterion("jigouhao not like", value, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoIn(List<String> values) {
            addCriterion("jigouhao in", values, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoNotIn(List<String> values) {
            addCriterion("jigouhao not in", values, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoBetween(String value1, String value2) {
            addCriterion("jigouhao between", value1, value2, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andJigouhaoNotBetween(String value1, String value2) {
            addCriterion("jigouhao not between", value1, value2, "jigouhao");
            return (Criteria) this;
        }

        public Criteria andServerdetailIsNull() {
            addCriterion("serverdetail is null");
            return (Criteria) this;
        }

        public Criteria andServerdetailIsNotNull() {
            addCriterion("serverdetail is not null");
            return (Criteria) this;
        }

        public Criteria andServerdetailEqualTo(String value) {
            addCriterion("serverdetail =", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailNotEqualTo(String value) {
            addCriterion("serverdetail <>", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailGreaterThan(String value) {
            addCriterion("serverdetail >", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailGreaterThanOrEqualTo(String value) {
            addCriterion("serverdetail >=", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailLessThan(String value) {
            addCriterion("serverdetail <", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailLessThanOrEqualTo(String value) {
            addCriterion("serverdetail <=", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailLike(String value) {
            addCriterion("serverdetail like", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailNotLike(String value) {
            addCriterion("serverdetail not like", value, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailIn(List<String> values) {
            addCriterion("serverdetail in", values, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailNotIn(List<String> values) {
            addCriterion("serverdetail not in", values, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailBetween(String value1, String value2) {
            addCriterion("serverdetail between", value1, value2, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andServerdetailNotBetween(String value1, String value2) {
            addCriterion("serverdetail not between", value1, value2, "serverdetail");
            return (Criteria) this;
        }

        public Criteria andProvincecodeIsNull() {
            addCriterion("provincecode is null");
            return (Criteria) this;
        }

        public Criteria andProvincecodeIsNotNull() {
            addCriterion("provincecode is not null");
            return (Criteria) this;
        }

        public Criteria andProvincecodeEqualTo(String value) {
            addCriterion("provincecode =", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeNotEqualTo(String value) {
            addCriterion("provincecode <>", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeGreaterThan(String value) {
            addCriterion("provincecode >", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeGreaterThanOrEqualTo(String value) {
            addCriterion("provincecode >=", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeLessThan(String value) {
            addCriterion("provincecode <", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeLessThanOrEqualTo(String value) {
            addCriterion("provincecode <=", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeLike(String value) {
            addCriterion("provincecode like", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeNotLike(String value) {
            addCriterion("provincecode not like", value, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeIn(List<String> values) {
            addCriterion("provincecode in", values, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeNotIn(List<String> values) {
            addCriterion("provincecode not in", values, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeBetween(String value1, String value2) {
            addCriterion("provincecode between", value1, value2, "provincecode");
            return (Criteria) this;
        }

        public Criteria andProvincecodeNotBetween(String value1, String value2) {
            addCriterion("provincecode not between", value1, value2, "provincecode");
            return (Criteria) this;
        }

        public Criteria andCitycodeIsNull() {
            addCriterion("citycode is null");
            return (Criteria) this;
        }

        public Criteria andCitycodeIsNotNull() {
            addCriterion("citycode is not null");
            return (Criteria) this;
        }

        public Criteria andCitycodeEqualTo(String value) {
            addCriterion("citycode =", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeNotEqualTo(String value) {
            addCriterion("citycode <>", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeGreaterThan(String value) {
            addCriterion("citycode >", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeGreaterThanOrEqualTo(String value) {
            addCriterion("citycode >=", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeLessThan(String value) {
            addCriterion("citycode <", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeLessThanOrEqualTo(String value) {
            addCriterion("citycode <=", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeLike(String value) {
            addCriterion("citycode like", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeNotLike(String value) {
            addCriterion("citycode not like", value, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeIn(List<String> values) {
            addCriterion("citycode in", values, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeNotIn(List<String> values) {
            addCriterion("citycode not in", values, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeBetween(String value1, String value2) {
            addCriterion("citycode between", value1, value2, "citycode");
            return (Criteria) this;
        }

        public Criteria andCitycodeNotBetween(String value1, String value2) {
            addCriterion("citycode not between", value1, value2, "citycode");
            return (Criteria) this;
        }

        public Criteria andAddcodeIsNull() {
            addCriterion("addcode is null");
            return (Criteria) this;
        }

        public Criteria andAddcodeIsNotNull() {
            addCriterion("addcode is not null");
            return (Criteria) this;
        }

        public Criteria andAddcodeEqualTo(String value) {
            addCriterion("addcode =", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeNotEqualTo(String value) {
            addCriterion("addcode <>", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeGreaterThan(String value) {
            addCriterion("addcode >", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeGreaterThanOrEqualTo(String value) {
            addCriterion("addcode >=", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeLessThan(String value) {
            addCriterion("addcode <", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeLessThanOrEqualTo(String value) {
            addCriterion("addcode <=", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeLike(String value) {
            addCriterion("addcode like", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeNotLike(String value) {
            addCriterion("addcode not like", value, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeIn(List<String> values) {
            addCriterion("addcode in", values, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeNotIn(List<String> values) {
            addCriterion("addcode not in", values, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeBetween(String value1, String value2) {
            addCriterion("addcode between", value1, value2, "addcode");
            return (Criteria) this;
        }

        public Criteria andAddcodeNotBetween(String value1, String value2) {
            addCriterion("addcode not between", value1, value2, "addcode");
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