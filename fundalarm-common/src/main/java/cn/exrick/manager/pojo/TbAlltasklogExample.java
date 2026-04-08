package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbAlltasklogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbAlltasklogExample() {
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

        public Criteria andTaskidIsNull() {
            addCriterion("taskid is null");
            return (Criteria) this;
        }

        public Criteria andTaskidIsNotNull() {
            addCriterion("taskid is not null");
            return (Criteria) this;
        }

        public Criteria andTaskidEqualTo(Integer value) {
            addCriterion("taskid =", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidNotEqualTo(Integer value) {
            addCriterion("taskid <>", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidGreaterThan(Integer value) {
            addCriterion("taskid >", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidGreaterThanOrEqualTo(Integer value) {
            addCriterion("taskid >=", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidLessThan(Integer value) {
            addCriterion("taskid <", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidLessThanOrEqualTo(Integer value) {
            addCriterion("taskid <=", value, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidIn(List<Integer> values) {
            addCriterion("taskid in", values, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidNotIn(List<Integer> values) {
            addCriterion("taskid not in", values, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidBetween(Integer value1, Integer value2) {
            addCriterion("taskid between", value1, value2, "taskid");
            return (Criteria) this;
        }

        public Criteria andTaskidNotBetween(Integer value1, Integer value2) {
            addCriterion("taskid not between", value1, value2, "taskid");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeIsNull() {
            addCriterion("workstarttime is null");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeIsNotNull() {
            addCriterion("workstarttime is not null");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeEqualTo(Date value) {
            addCriterion("workstarttime =", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeNotEqualTo(Date value) {
            addCriterion("workstarttime <>", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeGreaterThan(Date value) {
            addCriterion("workstarttime >", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("workstarttime >=", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeLessThan(Date value) {
            addCriterion("workstarttime <", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeLessThanOrEqualTo(Date value) {
            addCriterion("workstarttime <=", value, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeIn(List<Date> values) {
            addCriterion("workstarttime in", values, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeNotIn(List<Date> values) {
            addCriterion("workstarttime not in", values, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeBetween(Date value1, Date value2) {
            addCriterion("workstarttime between", value1, value2, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkstarttimeNotBetween(Date value1, Date value2) {
            addCriterion("workstarttime not between", value1, value2, "workstarttime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeIsNull() {
            addCriterion("workendtime is null");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeIsNotNull() {
            addCriterion("workendtime is not null");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeEqualTo(Date value) {
            addCriterion("workendtime =", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeNotEqualTo(Date value) {
            addCriterion("workendtime <>", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeGreaterThan(Date value) {
            addCriterion("workendtime >", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("workendtime >=", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeLessThan(Date value) {
            addCriterion("workendtime <", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeLessThanOrEqualTo(Date value) {
            addCriterion("workendtime <=", value, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeIn(List<Date> values) {
            addCriterion("workendtime in", values, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeNotIn(List<Date> values) {
            addCriterion("workendtime not in", values, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeBetween(Date value1, Date value2) {
            addCriterion("workendtime between", value1, value2, "workendtime");
            return (Criteria) this;
        }

        public Criteria andWorkendtimeNotBetween(Date value1, Date value2) {
            addCriterion("workendtime not between", value1, value2, "workendtime");
            return (Criteria) this;
        }

        public Criteria andResultIsNull() {
            addCriterion("result is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("result is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(Integer value) {
            addCriterion("result =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(Integer value) {
            addCriterion("result <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(Integer value) {
            addCriterion("result >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("result >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(Integer value) {
            addCriterion("result <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(Integer value) {
            addCriterion("result <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<Integer> values) {
            addCriterion("result in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<Integer> values) {
            addCriterion("result not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(Integer value1, Integer value2) {
            addCriterion("result between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(Integer value1, Integer value2) {
            addCriterion("result not between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andMessageIsNull() {
            addCriterion("message is null");
            return (Criteria) this;
        }

        public Criteria andMessageIsNotNull() {
            addCriterion("message is not null");
            return (Criteria) this;
        }

        public Criteria andMessageEqualTo(String value) {
            addCriterion("message =", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotEqualTo(String value) {
            addCriterion("message <>", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageGreaterThan(String value) {
            addCriterion("message >", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageGreaterThanOrEqualTo(String value) {
            addCriterion("message >=", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLessThan(String value) {
            addCriterion("message <", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLessThanOrEqualTo(String value) {
            addCriterion("message <=", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLike(String value) {
            addCriterion("message like", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotLike(String value) {
            addCriterion("message not like", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageIn(List<String> values) {
            addCriterion("message in", values, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotIn(List<String> values) {
            addCriterion("message not in", values, "message");
            return (Criteria) this;
        }

        public Criteria andMessageBetween(String value1, String value2) {
            addCriterion("message between", value1, value2, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotBetween(String value1, String value2) {
            addCriterion("message not between", value1, value2, "message");
            return (Criteria) this;
        }

        public Criteria andSendcountIsNull() {
            addCriterion("sendcount is null");
            return (Criteria) this;
        }

        public Criteria andSendcountIsNotNull() {
            addCriterion("sendcount is not null");
            return (Criteria) this;
        }

        public Criteria andSendcountEqualTo(Integer value) {
            addCriterion("sendcount =", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountNotEqualTo(Integer value) {
            addCriterion("sendcount <>", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountGreaterThan(Integer value) {
            addCriterion("sendcount >", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("sendcount >=", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountLessThan(Integer value) {
            addCriterion("sendcount <", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountLessThanOrEqualTo(Integer value) {
            addCriterion("sendcount <=", value, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountIn(List<Integer> values) {
            addCriterion("sendcount in", values, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountNotIn(List<Integer> values) {
            addCriterion("sendcount not in", values, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountBetween(Integer value1, Integer value2) {
            addCriterion("sendcount between", value1, value2, "sendcount");
            return (Criteria) this;
        }

        public Criteria andSendcountNotBetween(Integer value1, Integer value2) {
            addCriterion("sendcount not between", value1, value2, "sendcount");
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