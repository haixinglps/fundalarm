package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbCommandsDescExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbCommandsDescExample() {
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

        public Criteria andScriptcodeIsNull() {
            addCriterion("scriptcode is null");
            return (Criteria) this;
        }

        public Criteria andScriptcodeIsNotNull() {
            addCriterion("scriptcode is not null");
            return (Criteria) this;
        }

        public Criteria andScriptcodeEqualTo(String value) {
            addCriterion("scriptcode =", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeNotEqualTo(String value) {
            addCriterion("scriptcode <>", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeGreaterThan(String value) {
            addCriterion("scriptcode >", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeGreaterThanOrEqualTo(String value) {
            addCriterion("scriptcode >=", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeLessThan(String value) {
            addCriterion("scriptcode <", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeLessThanOrEqualTo(String value) {
            addCriterion("scriptcode <=", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeLike(String value) {
            addCriterion("scriptcode like", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeNotLike(String value) {
            addCriterion("scriptcode not like", value, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeIn(List<String> values) {
            addCriterion("scriptcode in", values, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeNotIn(List<String> values) {
            addCriterion("scriptcode not in", values, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeBetween(String value1, String value2) {
            addCriterion("scriptcode between", value1, value2, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andScriptcodeNotBetween(String value1, String value2) {
            addCriterion("scriptcode not between", value1, value2, "scriptcode");
            return (Criteria) this;
        }

        public Criteria andLongDescIsNull() {
            addCriterion("long_desc is null");
            return (Criteria) this;
        }

        public Criteria andLongDescIsNotNull() {
            addCriterion("long_desc is not null");
            return (Criteria) this;
        }

        public Criteria andLongDescEqualTo(String value) {
            addCriterion("long_desc =", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescNotEqualTo(String value) {
            addCriterion("long_desc <>", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescGreaterThan(String value) {
            addCriterion("long_desc >", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescGreaterThanOrEqualTo(String value) {
            addCriterion("long_desc >=", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescLessThan(String value) {
            addCriterion("long_desc <", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescLessThanOrEqualTo(String value) {
            addCriterion("long_desc <=", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescLike(String value) {
            addCriterion("long_desc like", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescNotLike(String value) {
            addCriterion("long_desc not like", value, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescIn(List<String> values) {
            addCriterion("long_desc in", values, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescNotIn(List<String> values) {
            addCriterion("long_desc not in", values, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescBetween(String value1, String value2) {
            addCriterion("long_desc between", value1, value2, "longDesc");
            return (Criteria) this;
        }

        public Criteria andLongDescNotBetween(String value1, String value2) {
            addCriterion("long_desc not between", value1, value2, "longDesc");
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

        public Criteria andExampleIsNull() {
            addCriterion("example is null");
            return (Criteria) this;
        }

        public Criteria andExampleIsNotNull() {
            addCriterion("example is not null");
            return (Criteria) this;
        }

        public Criteria andExampleEqualTo(String value) {
            addCriterion("example =", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleNotEqualTo(String value) {
            addCriterion("example <>", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleGreaterThan(String value) {
            addCriterion("example >", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleGreaterThanOrEqualTo(String value) {
            addCriterion("example >=", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleLessThan(String value) {
            addCriterion("example <", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleLessThanOrEqualTo(String value) {
            addCriterion("example <=", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleLike(String value) {
            addCriterion("example like", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleNotLike(String value) {
            addCriterion("example not like", value, "example");
            return (Criteria) this;
        }

        public Criteria andExampleIn(List<String> values) {
            addCriterion("example in", values, "example");
            return (Criteria) this;
        }

        public Criteria andExampleNotIn(List<String> values) {
            addCriterion("example not in", values, "example");
            return (Criteria) this;
        }

        public Criteria andExampleBetween(String value1, String value2) {
            addCriterion("example between", value1, value2, "example");
            return (Criteria) this;
        }

        public Criteria andExampleNotBetween(String value1, String value2) {
            addCriterion("example not between", value1, value2, "example");
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