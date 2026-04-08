package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbItemPriceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbItemPriceExample() {
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

        public Criteria andTraveltypeidIsNull() {
            addCriterion("traveltypeid is null");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidIsNotNull() {
            addCriterion("traveltypeid is not null");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidEqualTo(Integer value) {
            addCriterion("traveltypeid =", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidNotEqualTo(Integer value) {
            addCriterion("traveltypeid <>", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidGreaterThan(Integer value) {
            addCriterion("traveltypeid >", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidGreaterThanOrEqualTo(Integer value) {
            addCriterion("traveltypeid >=", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidLessThan(Integer value) {
            addCriterion("traveltypeid <", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidLessThanOrEqualTo(Integer value) {
            addCriterion("traveltypeid <=", value, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidIn(List<Integer> values) {
            addCriterion("traveltypeid in", values, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidNotIn(List<Integer> values) {
            addCriterion("traveltypeid not in", values, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidBetween(Integer value1, Integer value2) {
            addCriterion("traveltypeid between", value1, value2, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andTraveltypeidNotBetween(Integer value1, Integer value2) {
            addCriterion("traveltypeid not between", value1, value2, "traveltypeid");
            return (Criteria) this;
        }

        public Criteria andAgeminIsNull() {
            addCriterion("agemin is null");
            return (Criteria) this;
        }

        public Criteria andAgeminIsNotNull() {
            addCriterion("agemin is not null");
            return (Criteria) this;
        }

        public Criteria andAgeminEqualTo(Integer value) {
            addCriterion("agemin =", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminNotEqualTo(Integer value) {
            addCriterion("agemin <>", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminGreaterThan(Integer value) {
            addCriterion("agemin >", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminGreaterThanOrEqualTo(Integer value) {
            addCriterion("agemin >=", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminLessThan(Integer value) {
            addCriterion("agemin <", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminLessThanOrEqualTo(Integer value) {
            addCriterion("agemin <=", value, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminIn(List<Integer> values) {
            addCriterion("agemin in", values, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminNotIn(List<Integer> values) {
            addCriterion("agemin not in", values, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminBetween(Integer value1, Integer value2) {
            addCriterion("agemin between", value1, value2, "agemin");
            return (Criteria) this;
        }

        public Criteria andAgeminNotBetween(Integer value1, Integer value2) {
            addCriterion("agemin not between", value1, value2, "agemin");
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

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
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

        public Criteria andAgemaxIsNull() {
            addCriterion("agemax is null");
            return (Criteria) this;
        }

        public Criteria andAgemaxIsNotNull() {
            addCriterion("agemax is not null");
            return (Criteria) this;
        }

        public Criteria andAgemaxEqualTo(Integer value) {
            addCriterion("agemax =", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxNotEqualTo(Integer value) {
            addCriterion("agemax <>", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxGreaterThan(Integer value) {
            addCriterion("agemax >", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxGreaterThanOrEqualTo(Integer value) {
            addCriterion("agemax >=", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxLessThan(Integer value) {
            addCriterion("agemax <", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxLessThanOrEqualTo(Integer value) {
            addCriterion("agemax <=", value, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxIn(List<Integer> values) {
            addCriterion("agemax in", values, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxNotIn(List<Integer> values) {
            addCriterion("agemax not in", values, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxBetween(Integer value1, Integer value2) {
            addCriterion("agemax between", value1, value2, "agemax");
            return (Criteria) this;
        }

        public Criteria andAgemaxNotBetween(Integer value1, Integer value2) {
            addCriterion("agemax not between", value1, value2, "agemax");
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