package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbDouyinMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbDouyinMessageExample() {
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

        public Criteria andRobotidIsNull() {
            addCriterion("robotid is null");
            return (Criteria) this;
        }

        public Criteria andRobotidIsNotNull() {
            addCriterion("robotid is not null");
            return (Criteria) this;
        }

        public Criteria andRobotidEqualTo(String value) {
            addCriterion("robotid =", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotEqualTo(String value) {
            addCriterion("robotid <>", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThan(String value) {
            addCriterion("robotid >", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThanOrEqualTo(String value) {
            addCriterion("robotid >=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThan(String value) {
            addCriterion("robotid <", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThanOrEqualTo(String value) {
            addCriterion("robotid <=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLike(String value) {
            addCriterion("robotid like", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotLike(String value) {
            addCriterion("robotid not like", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidIn(List<String> values) {
            addCriterion("robotid in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotIn(List<String> values) {
            addCriterion("robotid not in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidBetween(String value1, String value2) {
            addCriterion("robotid between", value1, value2, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotBetween(String value1, String value2) {
            addCriterion("robotid not between", value1, value2, "robotid");
            return (Criteria) this;
        }

        public Criteria andDouyinidIsNull() {
            addCriterion("douyinid is null");
            return (Criteria) this;
        }

        public Criteria andDouyinidIsNotNull() {
            addCriterion("douyinid is not null");
            return (Criteria) this;
        }

        public Criteria andDouyinidEqualTo(String value) {
            addCriterion("douyinid =", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidNotEqualTo(String value) {
            addCriterion("douyinid <>", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidGreaterThan(String value) {
            addCriterion("douyinid >", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidGreaterThanOrEqualTo(String value) {
            addCriterion("douyinid >=", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidLessThan(String value) {
            addCriterion("douyinid <", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidLessThanOrEqualTo(String value) {
            addCriterion("douyinid <=", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidLike(String value) {
            addCriterion("douyinid like", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidNotLike(String value) {
            addCriterion("douyinid not like", value, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidIn(List<String> values) {
            addCriterion("douyinid in", values, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidNotIn(List<String> values) {
            addCriterion("douyinid not in", values, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidBetween(String value1, String value2) {
            addCriterion("douyinid between", value1, value2, "douyinid");
            return (Criteria) this;
        }

        public Criteria andDouyinidNotBetween(String value1, String value2) {
            addCriterion("douyinid not between", value1, value2, "douyinid");
            return (Criteria) this;
        }

        public Criteria andZhuboidIsNull() {
            addCriterion("zhuboid is null");
            return (Criteria) this;
        }

        public Criteria andZhuboidIsNotNull() {
            addCriterion("zhuboid is not null");
            return (Criteria) this;
        }

        public Criteria andZhuboidEqualTo(String value) {
            addCriterion("zhuboid =", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidNotEqualTo(String value) {
            addCriterion("zhuboid <>", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidGreaterThan(String value) {
            addCriterion("zhuboid >", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidGreaterThanOrEqualTo(String value) {
            addCriterion("zhuboid >=", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidLessThan(String value) {
            addCriterion("zhuboid <", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidLessThanOrEqualTo(String value) {
            addCriterion("zhuboid <=", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidLike(String value) {
            addCriterion("zhuboid like", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidNotLike(String value) {
            addCriterion("zhuboid not like", value, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidIn(List<String> values) {
            addCriterion("zhuboid in", values, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidNotIn(List<String> values) {
            addCriterion("zhuboid not in", values, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidBetween(String value1, String value2) {
            addCriterion("zhuboid between", value1, value2, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andZhuboidNotBetween(String value1, String value2) {
            addCriterion("zhuboid not between", value1, value2, "zhuboid");
            return (Criteria) this;
        }

        public Criteria andGztimeIsNull() {
            addCriterion("gztime is null");
            return (Criteria) this;
        }

        public Criteria andGztimeIsNotNull() {
            addCriterion("gztime is not null");
            return (Criteria) this;
        }

        public Criteria andGztimeEqualTo(Date value) {
            addCriterion("gztime =", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeNotEqualTo(Date value) {
            addCriterion("gztime <>", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeGreaterThan(Date value) {
            addCriterion("gztime >", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeGreaterThanOrEqualTo(Date value) {
            addCriterion("gztime >=", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeLessThan(Date value) {
            addCriterion("gztime <", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeLessThanOrEqualTo(Date value) {
            addCriterion("gztime <=", value, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeIn(List<Date> values) {
            addCriterion("gztime in", values, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeNotIn(List<Date> values) {
            addCriterion("gztime not in", values, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeBetween(Date value1, Date value2) {
            addCriterion("gztime between", value1, value2, "gztime");
            return (Criteria) this;
        }

        public Criteria andGztimeNotBetween(Date value1, Date value2) {
            addCriterion("gztime not between", value1, value2, "gztime");
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