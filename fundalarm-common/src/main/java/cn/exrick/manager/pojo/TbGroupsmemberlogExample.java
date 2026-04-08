package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbGroupsmemberlogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbGroupsmemberlogExample() {
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

        public Criteria andGroupidIsNull() {
            addCriterion("groupid is null");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNotNull() {
            addCriterion("groupid is not null");
            return (Criteria) this;
        }

        public Criteria andGroupidEqualTo(Integer value) {
            addCriterion("groupid =", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotEqualTo(Integer value) {
            addCriterion("groupid <>", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThan(Integer value) {
            addCriterion("groupid >", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThanOrEqualTo(Integer value) {
            addCriterion("groupid >=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThan(Integer value) {
            addCriterion("groupid <", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThanOrEqualTo(Integer value) {
            addCriterion("groupid <=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidIn(List<Integer> values) {
            addCriterion("groupid in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotIn(List<Integer> values) {
            addCriterion("groupid not in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidBetween(Integer value1, Integer value2) {
            addCriterion("groupid between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotBetween(Integer value1, Integer value2) {
            addCriterion("groupid not between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andDayitemIsNull() {
            addCriterion("dayitem is null");
            return (Criteria) this;
        }

        public Criteria andDayitemIsNotNull() {
            addCriterion("dayitem is not null");
            return (Criteria) this;
        }

        public Criteria andDayitemEqualTo(Integer value) {
            addCriterion("dayitem =", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemNotEqualTo(Integer value) {
            addCriterion("dayitem <>", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemGreaterThan(Integer value) {
            addCriterion("dayitem >", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemGreaterThanOrEqualTo(Integer value) {
            addCriterion("dayitem >=", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemLessThan(Integer value) {
            addCriterion("dayitem <", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemLessThanOrEqualTo(Integer value) {
            addCriterion("dayitem <=", value, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemIn(List<Integer> values) {
            addCriterion("dayitem in", values, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemNotIn(List<Integer> values) {
            addCriterion("dayitem not in", values, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemBetween(Integer value1, Integer value2) {
            addCriterion("dayitem between", value1, value2, "dayitem");
            return (Criteria) this;
        }

        public Criteria andDayitemNotBetween(Integer value1, Integer value2) {
            addCriterion("dayitem not between", value1, value2, "dayitem");
            return (Criteria) this;
        }

        public Criteria andMsgcountIsNull() {
            addCriterion("msgcount is null");
            return (Criteria) this;
        }

        public Criteria andMsgcountIsNotNull() {
            addCriterion("msgcount is not null");
            return (Criteria) this;
        }

        public Criteria andMsgcountEqualTo(Integer value) {
            addCriterion("msgcount =", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountNotEqualTo(Integer value) {
            addCriterion("msgcount <>", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountGreaterThan(Integer value) {
            addCriterion("msgcount >", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("msgcount >=", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountLessThan(Integer value) {
            addCriterion("msgcount <", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountLessThanOrEqualTo(Integer value) {
            addCriterion("msgcount <=", value, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountIn(List<Integer> values) {
            addCriterion("msgcount in", values, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountNotIn(List<Integer> values) {
            addCriterion("msgcount not in", values, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountBetween(Integer value1, Integer value2) {
            addCriterion("msgcount between", value1, value2, "msgcount");
            return (Criteria) this;
        }

        public Criteria andMsgcountNotBetween(Integer value1, Integer value2) {
            addCriterion("msgcount not between", value1, value2, "msgcount");
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

        public Criteria andOrganidEqualTo(Integer value) {
            addCriterion("organid =", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotEqualTo(Integer value) {
            addCriterion("organid <>", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidGreaterThan(Integer value) {
            addCriterion("organid >", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidGreaterThanOrEqualTo(Integer value) {
            addCriterion("organid >=", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidLessThan(Integer value) {
            addCriterion("organid <", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidLessThanOrEqualTo(Integer value) {
            addCriterion("organid <=", value, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidIn(List<Integer> values) {
            addCriterion("organid in", values, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotIn(List<Integer> values) {
            addCriterion("organid not in", values, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidBetween(Integer value1, Integer value2) {
            addCriterion("organid between", value1, value2, "organid");
            return (Criteria) this;
        }

        public Criteria andOrganidNotBetween(Integer value1, Integer value2) {
            addCriterion("organid not between", value1, value2, "organid");
            return (Criteria) this;
        }

        public Criteria andCdIsNull() {
            addCriterion("cd is null");
            return (Criteria) this;
        }

        public Criteria andCdIsNotNull() {
            addCriterion("cd is not null");
            return (Criteria) this;
        }

        public Criteria andCdEqualTo(Long value) {
            addCriterion("cd =", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdNotEqualTo(Long value) {
            addCriterion("cd <>", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdGreaterThan(Long value) {
            addCriterion("cd >", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdGreaterThanOrEqualTo(Long value) {
            addCriterion("cd >=", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdLessThan(Long value) {
            addCriterion("cd <", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdLessThanOrEqualTo(Long value) {
            addCriterion("cd <=", value, "cd");
            return (Criteria) this;
        }

        public Criteria andCdIn(List<Long> values) {
            addCriterion("cd in", values, "cd");
            return (Criteria) this;
        }

        public Criteria andCdNotIn(List<Long> values) {
            addCriterion("cd not in", values, "cd");
            return (Criteria) this;
        }

        public Criteria andCdBetween(Long value1, Long value2) {
            addCriterion("cd between", value1, value2, "cd");
            return (Criteria) this;
        }

        public Criteria andCdNotBetween(Long value1, Long value2) {
            addCriterion("cd not between", value1, value2, "cd");
            return (Criteria) this;
        }

        public Criteria andUdIsNull() {
            addCriterion("ud is null");
            return (Criteria) this;
        }

        public Criteria andUdIsNotNull() {
            addCriterion("ud is not null");
            return (Criteria) this;
        }

        public Criteria andUdEqualTo(Long value) {
            addCriterion("ud =", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdNotEqualTo(Long value) {
            addCriterion("ud <>", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdGreaterThan(Long value) {
            addCriterion("ud >", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdGreaterThanOrEqualTo(Long value) {
            addCriterion("ud >=", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdLessThan(Long value) {
            addCriterion("ud <", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdLessThanOrEqualTo(Long value) {
            addCriterion("ud <=", value, "ud");
            return (Criteria) this;
        }

        public Criteria andUdIn(List<Long> values) {
            addCriterion("ud in", values, "ud");
            return (Criteria) this;
        }

        public Criteria andUdNotIn(List<Long> values) {
            addCriterion("ud not in", values, "ud");
            return (Criteria) this;
        }

        public Criteria andUdBetween(Long value1, Long value2) {
            addCriterion("ud between", value1, value2, "ud");
            return (Criteria) this;
        }

        public Criteria andUdNotBetween(Long value1, Long value2) {
            addCriterion("ud not between", value1, value2, "ud");
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

        public Criteria andRobotidEqualTo(Integer value) {
            addCriterion("robotid =", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotEqualTo(Integer value) {
            addCriterion("robotid <>", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThan(Integer value) {
            addCriterion("robotid >", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidGreaterThanOrEqualTo(Integer value) {
            addCriterion("robotid >=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThan(Integer value) {
            addCriterion("robotid <", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidLessThanOrEqualTo(Integer value) {
            addCriterion("robotid <=", value, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidIn(List<Integer> values) {
            addCriterion("robotid in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotIn(List<Integer> values) {
            addCriterion("robotid not in", values, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidBetween(Integer value1, Integer value2) {
            addCriterion("robotid between", value1, value2, "robotid");
            return (Criteria) this;
        }

        public Criteria andRobotidNotBetween(Integer value1, Integer value2) {
            addCriterion("robotid not between", value1, value2, "robotid");
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

        public Criteria andUsernameNotEqualTo(Integer value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(Integer value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(Integer value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(Integer value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(Integer value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<Integer> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<Integer> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(Integer value1, Integer value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(Integer value1, Integer value2) {
            addCriterion("username not between", value1, value2, "username");
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