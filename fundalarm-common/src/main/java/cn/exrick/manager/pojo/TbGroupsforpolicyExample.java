package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbGroupsforpolicyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbGroupsforpolicyExample() {
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

        public Criteria andCompanynameIsNull() {
            addCriterion("companyname is null");
            return (Criteria) this;
        }

        public Criteria andCompanynameIsNotNull() {
            addCriterion("companyname is not null");
            return (Criteria) this;
        }

        public Criteria andCompanynameEqualTo(String value) {
            addCriterion("companyname =", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotEqualTo(String value) {
            addCriterion("companyname <>", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameGreaterThan(String value) {
            addCriterion("companyname >", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameGreaterThanOrEqualTo(String value) {
            addCriterion("companyname >=", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLessThan(String value) {
            addCriterion("companyname <", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLessThanOrEqualTo(String value) {
            addCriterion("companyname <=", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLike(String value) {
            addCriterion("companyname like", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotLike(String value) {
            addCriterion("companyname not like", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameIn(List<String> values) {
            addCriterion("companyname in", values, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotIn(List<String> values) {
            addCriterion("companyname not in", values, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameBetween(String value1, String value2) {
            addCriterion("companyname between", value1, value2, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotBetween(String value1, String value2) {
            addCriterion("companyname not between", value1, value2, "companyname");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIsNull() {
            addCriterion("peoplecount is null");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIsNotNull() {
            addCriterion("peoplecount is not null");
            return (Criteria) this;
        }

        public Criteria andPeoplecountEqualTo(Integer value) {
            addCriterion("peoplecount =", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotEqualTo(Integer value) {
            addCriterion("peoplecount <>", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountGreaterThan(Integer value) {
            addCriterion("peoplecount >", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("peoplecount >=", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountLessThan(Integer value) {
            addCriterion("peoplecount <", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountLessThanOrEqualTo(Integer value) {
            addCriterion("peoplecount <=", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIn(List<Integer> values) {
            addCriterion("peoplecount in", values, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotIn(List<Integer> values) {
            addCriterion("peoplecount not in", values, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountBetween(Integer value1, Integer value2) {
            addCriterion("peoplecount between", value1, value2, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotBetween(Integer value1, Integer value2) {
            addCriterion("peoplecount not between", value1, value2, "peoplecount");
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

        public Criteria andBiliIsNull() {
            addCriterion("bili is null");
            return (Criteria) this;
        }

        public Criteria andBiliIsNotNull() {
            addCriterion("bili is not null");
            return (Criteria) this;
        }

        public Criteria andBiliEqualTo(BigDecimal value) {
            addCriterion("bili =", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliNotEqualTo(BigDecimal value) {
            addCriterion("bili <>", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliGreaterThan(BigDecimal value) {
            addCriterion("bili >", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("bili >=", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliLessThan(BigDecimal value) {
            addCriterion("bili <", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliLessThanOrEqualTo(BigDecimal value) {
            addCriterion("bili <=", value, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliIn(List<BigDecimal> values) {
            addCriterion("bili in", values, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliNotIn(List<BigDecimal> values) {
            addCriterion("bili not in", values, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bili between", value1, value2, "bili");
            return (Criteria) this;
        }

        public Criteria andBiliNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bili not between", value1, value2, "bili");
            return (Criteria) this;
        }

        public Criteria andMannameIsNull() {
            addCriterion("manname is null");
            return (Criteria) this;
        }

        public Criteria andMannameIsNotNull() {
            addCriterion("manname is not null");
            return (Criteria) this;
        }

        public Criteria andMannameEqualTo(String value) {
            addCriterion("manname =", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotEqualTo(String value) {
            addCriterion("manname <>", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameGreaterThan(String value) {
            addCriterion("manname >", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameGreaterThanOrEqualTo(String value) {
            addCriterion("manname >=", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLessThan(String value) {
            addCriterion("manname <", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLessThanOrEqualTo(String value) {
            addCriterion("manname <=", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLike(String value) {
            addCriterion("manname like", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotLike(String value) {
            addCriterion("manname not like", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameIn(List<String> values) {
            addCriterion("manname in", values, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotIn(List<String> values) {
            addCriterion("manname not in", values, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameBetween(String value1, String value2) {
            addCriterion("manname between", value1, value2, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotBetween(String value1, String value2) {
            addCriterion("manname not between", value1, value2, "manname");
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

        public Criteria andDtIsNull() {
            addCriterion("dt is null");
            return (Criteria) this;
        }

        public Criteria andDtIsNotNull() {
            addCriterion("dt is not null");
            return (Criteria) this;
        }

        public Criteria andDtEqualTo(Date value) {
            addCriterion("dt =", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtNotEqualTo(Date value) {
            addCriterion("dt <>", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtGreaterThan(Date value) {
            addCriterion("dt >", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtGreaterThanOrEqualTo(Date value) {
            addCriterion("dt >=", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtLessThan(Date value) {
            addCriterion("dt <", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtLessThanOrEqualTo(Date value) {
            addCriterion("dt <=", value, "dt");
            return (Criteria) this;
        }

        public Criteria andDtIn(List<Date> values) {
            addCriterion("dt in", values, "dt");
            return (Criteria) this;
        }

        public Criteria andDtNotIn(List<Date> values) {
            addCriterion("dt not in", values, "dt");
            return (Criteria) this;
        }

        public Criteria andDtBetween(Date value1, Date value2) {
            addCriterion("dt between", value1, value2, "dt");
            return (Criteria) this;
        }

        public Criteria andDtNotBetween(Date value1, Date value2) {
            addCriterion("dt not between", value1, value2, "dt");
            return (Criteria) this;
        }

        public Criteria andSourceipIsNull() {
            addCriterion("sourceip is null");
            return (Criteria) this;
        }

        public Criteria andSourceipIsNotNull() {
            addCriterion("sourceip is not null");
            return (Criteria) this;
        }

        public Criteria andSourceipEqualTo(String value) {
            addCriterion("sourceip =", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipNotEqualTo(String value) {
            addCriterion("sourceip <>", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipGreaterThan(String value) {
            addCriterion("sourceip >", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipGreaterThanOrEqualTo(String value) {
            addCriterion("sourceip >=", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipLessThan(String value) {
            addCriterion("sourceip <", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipLessThanOrEqualTo(String value) {
            addCriterion("sourceip <=", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipLike(String value) {
            addCriterion("sourceip like", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipNotLike(String value) {
            addCriterion("sourceip not like", value, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipIn(List<String> values) {
            addCriterion("sourceip in", values, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipNotIn(List<String> values) {
            addCriterion("sourceip not in", values, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipBetween(String value1, String value2) {
            addCriterion("sourceip between", value1, value2, "sourceip");
            return (Criteria) this;
        }

        public Criteria andSourceipNotBetween(String value1, String value2) {
            addCriterion("sourceip not between", value1, value2, "sourceip");
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