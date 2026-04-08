package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbOrganizationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbOrganizationExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrgannameIsNull() {
            addCriterion("organname is null");
            return (Criteria) this;
        }

        public Criteria andOrgannameIsNotNull() {
            addCriterion("organname is not null");
            return (Criteria) this;
        }

        public Criteria andOrgannameEqualTo(String value) {
            addCriterion("organname =", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameNotEqualTo(String value) {
            addCriterion("organname <>", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameGreaterThan(String value) {
            addCriterion("organname >", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameGreaterThanOrEqualTo(String value) {
            addCriterion("organname >=", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameLessThan(String value) {
            addCriterion("organname <", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameLessThanOrEqualTo(String value) {
            addCriterion("organname <=", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameLike(String value) {
            addCriterion("organname like", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameNotLike(String value) {
            addCriterion("organname not like", value, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameIn(List<String> values) {
            addCriterion("organname in", values, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameNotIn(List<String> values) {
            addCriterion("organname not in", values, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameBetween(String value1, String value2) {
            addCriterion("organname between", value1, value2, "organname");
            return (Criteria) this;
        }

        public Criteria andOrgannameNotBetween(String value1, String value2) {
            addCriterion("organname not between", value1, value2, "organname");
            return (Criteria) this;
        }

        public Criteria andParentorganidIsNull() {
            addCriterion("parentorganid is null");
            return (Criteria) this;
        }

        public Criteria andParentorganidIsNotNull() {
            addCriterion("parentorganid is not null");
            return (Criteria) this;
        }

        public Criteria andParentorganidEqualTo(Integer value) {
            addCriterion("parentorganid =", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidNotEqualTo(Integer value) {
            addCriterion("parentorganid <>", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidGreaterThan(Integer value) {
            addCriterion("parentorganid >", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidGreaterThanOrEqualTo(Integer value) {
            addCriterion("parentorganid >=", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidLessThan(Integer value) {
            addCriterion("parentorganid <", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidLessThanOrEqualTo(Integer value) {
            addCriterion("parentorganid <=", value, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidIn(List<Integer> values) {
            addCriterion("parentorganid in", values, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidNotIn(List<Integer> values) {
            addCriterion("parentorganid not in", values, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidBetween(Integer value1, Integer value2) {
            addCriterion("parentorganid between", value1, value2, "parentorganid");
            return (Criteria) this;
        }

        public Criteria andParentorganidNotBetween(Integer value1, Integer value2) {
            addCriterion("parentorganid not between", value1, value2, "parentorganid");
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

        public Criteria andPolicyidIsNull() {
            addCriterion("policyid is null");
            return (Criteria) this;
        }

        public Criteria andPolicyidIsNotNull() {
            addCriterion("policyid is not null");
            return (Criteria) this;
        }

        public Criteria andPolicyidEqualTo(String value) {
            addCriterion("policyid =", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotEqualTo(String value) {
            addCriterion("policyid <>", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidGreaterThan(String value) {
            addCriterion("policyid >", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidGreaterThanOrEqualTo(String value) {
            addCriterion("policyid >=", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidLessThan(String value) {
            addCriterion("policyid <", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidLessThanOrEqualTo(String value) {
            addCriterion("policyid <=", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidLike(String value) {
            addCriterion("policyid like", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotLike(String value) {
            addCriterion("policyid not like", value, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidIn(List<String> values) {
            addCriterion("policyid in", values, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotIn(List<String> values) {
            addCriterion("policyid not in", values, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidBetween(String value1, String value2) {
            addCriterion("policyid between", value1, value2, "policyid");
            return (Criteria) this;
        }

        public Criteria andPolicyidNotBetween(String value1, String value2) {
            addCriterion("policyid not between", value1, value2, "policyid");
            return (Criteria) this;
        }

        public Criteria andMerchantidIsNull() {
            addCriterion("merchantid is null");
            return (Criteria) this;
        }

        public Criteria andMerchantidIsNotNull() {
            addCriterion("merchantid is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantidEqualTo(String value) {
            addCriterion("merchantid =", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidNotEqualTo(String value) {
            addCriterion("merchantid <>", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidGreaterThan(String value) {
            addCriterion("merchantid >", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidGreaterThanOrEqualTo(String value) {
            addCriterion("merchantid >=", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidLessThan(String value) {
            addCriterion("merchantid <", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidLessThanOrEqualTo(String value) {
            addCriterion("merchantid <=", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidLike(String value) {
            addCriterion("merchantid like", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidNotLike(String value) {
            addCriterion("merchantid not like", value, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidIn(List<String> values) {
            addCriterion("merchantid in", values, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidNotIn(List<String> values) {
            addCriterion("merchantid not in", values, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidBetween(String value1, String value2) {
            addCriterion("merchantid between", value1, value2, "merchantid");
            return (Criteria) this;
        }

        public Criteria andMerchantidNotBetween(String value1, String value2) {
            addCriterion("merchantid not between", value1, value2, "merchantid");
            return (Criteria) this;
        }

        public Criteria andAgree1IsNull() {
            addCriterion("agree1 is null");
            return (Criteria) this;
        }

        public Criteria andAgree1IsNotNull() {
            addCriterion("agree1 is not null");
            return (Criteria) this;
        }

        public Criteria andAgree1EqualTo(Integer value) {
            addCriterion("agree1 =", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1NotEqualTo(Integer value) {
            addCriterion("agree1 <>", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1GreaterThan(Integer value) {
            addCriterion("agree1 >", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1GreaterThanOrEqualTo(Integer value) {
            addCriterion("agree1 >=", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1LessThan(Integer value) {
            addCriterion("agree1 <", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1LessThanOrEqualTo(Integer value) {
            addCriterion("agree1 <=", value, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1In(List<Integer> values) {
            addCriterion("agree1 in", values, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1NotIn(List<Integer> values) {
            addCriterion("agree1 not in", values, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1Between(Integer value1, Integer value2) {
            addCriterion("agree1 between", value1, value2, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree1NotBetween(Integer value1, Integer value2) {
            addCriterion("agree1 not between", value1, value2, "agree1");
            return (Criteria) this;
        }

        public Criteria andAgree2IsNull() {
            addCriterion("agree2 is null");
            return (Criteria) this;
        }

        public Criteria andAgree2IsNotNull() {
            addCriterion("agree2 is not null");
            return (Criteria) this;
        }

        public Criteria andAgree2EqualTo(Integer value) {
            addCriterion("agree2 =", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2NotEqualTo(Integer value) {
            addCriterion("agree2 <>", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2GreaterThan(Integer value) {
            addCriterion("agree2 >", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2GreaterThanOrEqualTo(Integer value) {
            addCriterion("agree2 >=", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2LessThan(Integer value) {
            addCriterion("agree2 <", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2LessThanOrEqualTo(Integer value) {
            addCriterion("agree2 <=", value, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2In(List<Integer> values) {
            addCriterion("agree2 in", values, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2NotIn(List<Integer> values) {
            addCriterion("agree2 not in", values, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2Between(Integer value1, Integer value2) {
            addCriterion("agree2 between", value1, value2, "agree2");
            return (Criteria) this;
        }

        public Criteria andAgree2NotBetween(Integer value1, Integer value2) {
            addCriterion("agree2 not between", value1, value2, "agree2");
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

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
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