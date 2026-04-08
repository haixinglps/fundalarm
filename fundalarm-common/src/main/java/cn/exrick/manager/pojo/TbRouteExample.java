package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbRouteExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbRouteExample() {
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

        public Criteria andRoutenameIsNull() {
            addCriterion("routename is null");
            return (Criteria) this;
        }

        public Criteria andRoutenameIsNotNull() {
            addCriterion("routename is not null");
            return (Criteria) this;
        }

        public Criteria andRoutenameEqualTo(String value) {
            addCriterion("routename =", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameNotEqualTo(String value) {
            addCriterion("routename <>", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameGreaterThan(String value) {
            addCriterion("routename >", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameGreaterThanOrEqualTo(String value) {
            addCriterion("routename >=", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameLessThan(String value) {
            addCriterion("routename <", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameLessThanOrEqualTo(String value) {
            addCriterion("routename <=", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameLike(String value) {
            addCriterion("routename like", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameNotLike(String value) {
            addCriterion("routename not like", value, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameIn(List<String> values) {
            addCriterion("routename in", values, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameNotIn(List<String> values) {
            addCriterion("routename not in", values, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameBetween(String value1, String value2) {
            addCriterion("routename between", value1, value2, "routename");
            return (Criteria) this;
        }

        public Criteria andRoutenameNotBetween(String value1, String value2) {
            addCriterion("routename not between", value1, value2, "routename");
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

        public Criteria andTravetypeIsNull() {
            addCriterion("travetype is null");
            return (Criteria) this;
        }

        public Criteria andTravetypeIsNotNull() {
            addCriterion("travetype is not null");
            return (Criteria) this;
        }

        public Criteria andTravetypeEqualTo(Integer value) {
            addCriterion("travetype =", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeNotEqualTo(Integer value) {
            addCriterion("travetype <>", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeGreaterThan(Integer value) {
            addCriterion("travetype >", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("travetype >=", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeLessThan(Integer value) {
            addCriterion("travetype <", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeLessThanOrEqualTo(Integer value) {
            addCriterion("travetype <=", value, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeIn(List<Integer> values) {
            addCriterion("travetype in", values, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeNotIn(List<Integer> values) {
            addCriterion("travetype not in", values, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeBetween(Integer value1, Integer value2) {
            addCriterion("travetype between", value1, value2, "travetype");
            return (Criteria) this;
        }

        public Criteria andTravetypeNotBetween(Integer value1, Integer value2) {
            addCriterion("travetype not between", value1, value2, "travetype");
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

        public Criteria andDaysIsNull() {
            addCriterion("days is null");
            return (Criteria) this;
        }

        public Criteria andDaysIsNotNull() {
            addCriterion("days is not null");
            return (Criteria) this;
        }

        public Criteria andDaysEqualTo(Integer value) {
            addCriterion("days =", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysNotEqualTo(Integer value) {
            addCriterion("days <>", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysGreaterThan(Integer value) {
            addCriterion("days >", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("days >=", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysLessThan(Integer value) {
            addCriterion("days <", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysLessThanOrEqualTo(Integer value) {
            addCriterion("days <=", value, "days");
            return (Criteria) this;
        }

        public Criteria andDaysIn(List<Integer> values) {
            addCriterion("days in", values, "days");
            return (Criteria) this;
        }

        public Criteria andDaysNotIn(List<Integer> values) {
            addCriterion("days not in", values, "days");
            return (Criteria) this;
        }

        public Criteria andDaysBetween(Integer value1, Integer value2) {
            addCriterion("days between", value1, value2, "days");
            return (Criteria) this;
        }

        public Criteria andDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("days not between", value1, value2, "days");
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

        public Criteria andDestinationsIsNull() {
            addCriterion("destinations is null");
            return (Criteria) this;
        }

        public Criteria andDestinationsIsNotNull() {
            addCriterion("destinations is not null");
            return (Criteria) this;
        }

        public Criteria andDestinationsEqualTo(String value) {
            addCriterion("destinations =", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsNotEqualTo(String value) {
            addCriterion("destinations <>", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsGreaterThan(String value) {
            addCriterion("destinations >", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsGreaterThanOrEqualTo(String value) {
            addCriterion("destinations >=", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsLessThan(String value) {
            addCriterion("destinations <", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsLessThanOrEqualTo(String value) {
            addCriterion("destinations <=", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsLike(String value) {
            addCriterion("destinations like", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsNotLike(String value) {
            addCriterion("destinations not like", value, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsIn(List<String> values) {
            addCriterion("destinations in", values, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsNotIn(List<String> values) {
            addCriterion("destinations not in", values, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsBetween(String value1, String value2) {
            addCriterion("destinations between", value1, value2, "destinations");
            return (Criteria) this;
        }

        public Criteria andDestinationsNotBetween(String value1, String value2) {
            addCriterion("destinations not between", value1, value2, "destinations");
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