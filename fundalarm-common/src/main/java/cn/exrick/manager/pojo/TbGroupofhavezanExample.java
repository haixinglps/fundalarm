package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbGroupofhavezanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbGroupofhavezanExample() {
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

        public Criteria andRobotidEqualTo(Long value) {
            addCriterion("robotid =", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotEqualTo(Long value) {
            addCriterion("robotid <>", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThan(Long value) {
            addCriterion("robotid >", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThanOrEqualTo(Long value) {
            addCriterion("robotid >=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThan(Long value) {
            addCriterion("robotid <", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThanOrEqualTo(Long value) {
            addCriterion("robotid <=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidIn(List<Long> values) {
            addCriterion("robotid in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotIn(List<Long> values) {
            addCriterion("robotid not in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidBetween(Long value1, Long value2) {
            addCriterion("robotid between", value1, value2, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotBetween(Long value1, Long value2) {
            addCriterion("robotid not between", value1, value2, "robotid");
            return (Criteria) this;
        }

        public Criteria andExecuatecountIsNull() {
            addCriterion("execuatecount is null");
            return (Criteria) this;
        }

        public Criteria andExecuatecountIsNotNull() {
            addCriterion("execuatecount is not null");
            return (Criteria) this;
        }

        public Criteria andExecuatecountEqualTo(Integer value) {
            addCriterion("execuatecount =", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountNotEqualTo(Integer value) {
            addCriterion("execuatecount <>", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountGreaterThan(Integer value) {
            addCriterion("execuatecount >", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("execuatecount >=", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountLessThan(Integer value) {
            addCriterion("execuatecount <", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountLessThanOrEqualTo(Integer value) {
            addCriterion("execuatecount <=", value, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountIn(List<Integer> values) {
            addCriterion("execuatecount in", values, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountNotIn(List<Integer> values) {
            addCriterion("execuatecount not in", values, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountBetween(Integer value1, Integer value2) {
            addCriterion("execuatecount between", value1, value2, "execuatecount");
            return (Criteria) this;
        }

        public Criteria andExecuatecountNotBetween(Integer value1, Integer value2) {
            addCriterion("execuatecount not between", value1, value2, "execuatecount");
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

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagIsNull() {
            addCriterion("zansuccesstag is null");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagIsNotNull() {
            addCriterion("zansuccesstag is not null");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagEqualTo(Integer value) {
            addCriterion("zansuccesstag =", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagNotEqualTo(Integer value) {
            addCriterion("zansuccesstag <>", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagGreaterThan(Integer value) {
            addCriterion("zansuccesstag >", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagGreaterThanOrEqualTo(Integer value) {
            addCriterion("zansuccesstag >=", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagLessThan(Integer value) {
            addCriterion("zansuccesstag <", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagLessThanOrEqualTo(Integer value) {
            addCriterion("zansuccesstag <=", value, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagIn(List<Integer> values) {
            addCriterion("zansuccesstag in", values, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagNotIn(List<Integer> values) {
            addCriterion("zansuccesstag not in", values, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagBetween(Integer value1, Integer value2) {
            addCriterion("zansuccesstag between", value1, value2, "zansuccesstag");
            return (Criteria) this;
        }

        public Criteria andZansuccesstagNotBetween(Integer value1, Integer value2) {
            addCriterion("zansuccesstag not between", value1, value2, "zansuccesstag");
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