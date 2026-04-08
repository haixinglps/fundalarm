package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TbSummeryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbSummeryExample() {
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

        public Criteria andBegindateIsNull() {
            addCriterion("begindate is null");
            return (Criteria) this;
        }

        public Criteria andBegindateIsNotNull() {
            addCriterion("begindate is not null");
            return (Criteria) this;
        }

        public Criteria andBegindateEqualTo(String value) {
            addCriterion("begindate =", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateNotEqualTo(String value) {
            addCriterion("begindate <>", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateGreaterThan(String value) {
            addCriterion("begindate >", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateGreaterThanOrEqualTo(String value) {
            addCriterion("begindate >=", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateLessThan(String value) {
            addCriterion("begindate <", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateLessThanOrEqualTo(String value) {
            addCriterion("begindate <=", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateLike(String value) {
            addCriterion("begindate like", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateNotLike(String value) {
            addCriterion("begindate not like", value, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateIn(List<String> values) {
            addCriterion("begindate in", values, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateNotIn(List<String> values) {
            addCriterion("begindate not in", values, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateBetween(String value1, String value2) {
            addCriterion("begindate between", value1, value2, "begindate");
            return (Criteria) this;
        }

        public Criteria andBegindateNotBetween(String value1, String value2) {
            addCriterion("begindate not between", value1, value2, "begindate");
            return (Criteria) this;
        }

        public Criteria andEnddateIsNull() {
            addCriterion("enddate is null");
            return (Criteria) this;
        }

        public Criteria andEnddateIsNotNull() {
            addCriterion("enddate is not null");
            return (Criteria) this;
        }

        public Criteria andEnddateEqualTo(String value) {
            addCriterion("enddate =", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateNotEqualTo(String value) {
            addCriterion("enddate <>", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateGreaterThan(String value) {
            addCriterion("enddate >", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateGreaterThanOrEqualTo(String value) {
            addCriterion("enddate >=", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateLessThan(String value) {
            addCriterion("enddate <", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateLessThanOrEqualTo(String value) {
            addCriterion("enddate <=", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateLike(String value) {
            addCriterion("enddate like", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateNotLike(String value) {
            addCriterion("enddate not like", value, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateIn(List<String> values) {
            addCriterion("enddate in", values, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateNotIn(List<String> values) {
            addCriterion("enddate not in", values, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateBetween(String value1, String value2) {
            addCriterion("enddate between", value1, value2, "enddate");
            return (Criteria) this;
        }

        public Criteria andEnddateNotBetween(String value1, String value2) {
            addCriterion("enddate not between", value1, value2, "enddate");
            return (Criteria) this;
        }

        public Criteria andLzxIsNull() {
            addCriterion("lzx is null");
            return (Criteria) this;
        }

        public Criteria andLzxIsNotNull() {
            addCriterion("lzx is not null");
            return (Criteria) this;
        }

        public Criteria andLzxEqualTo(BigDecimal value) {
            addCriterion("lzx =", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxNotEqualTo(BigDecimal value) {
            addCriterion("lzx <>", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxGreaterThan(BigDecimal value) {
            addCriterion("lzx >", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lzx >=", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxLessThan(BigDecimal value) {
            addCriterion("lzx <", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lzx <=", value, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxIn(List<BigDecimal> values) {
            addCriterion("lzx in", values, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxNotIn(List<BigDecimal> values) {
            addCriterion("lzx not in", values, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzx between", value1, value2, "lzx");
            return (Criteria) this;
        }

        public Criteria andLzxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzx not between", value1, value2, "lzx");
            return (Criteria) this;
        }

        public Criteria andYwxIsNull() {
            addCriterion("ywx is null");
            return (Criteria) this;
        }

        public Criteria andYwxIsNotNull() {
            addCriterion("ywx is not null");
            return (Criteria) this;
        }

        public Criteria andYwxEqualTo(BigDecimal value) {
            addCriterion("ywx =", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxNotEqualTo(BigDecimal value) {
            addCriterion("ywx <>", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxGreaterThan(BigDecimal value) {
            addCriterion("ywx >", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ywx >=", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxLessThan(BigDecimal value) {
            addCriterion("ywx <", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ywx <=", value, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxIn(List<BigDecimal> values) {
            addCriterion("ywx in", values, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxNotIn(List<BigDecimal> values) {
            addCriterion("ywx not in", values, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywx between", value1, value2, "ywx");
            return (Criteria) this;
        }

        public Criteria andYwxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywx not between", value1, value2, "ywx");
            return (Criteria) this;
        }

        public Criteria andSummeryIsNull() {
            addCriterion("summery is null");
            return (Criteria) this;
        }

        public Criteria andSummeryIsNotNull() {
            addCriterion("summery is not null");
            return (Criteria) this;
        }

        public Criteria andSummeryEqualTo(BigDecimal value) {
            addCriterion("summery =", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryNotEqualTo(BigDecimal value) {
            addCriterion("summery <>", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryGreaterThan(BigDecimal value) {
            addCriterion("summery >", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("summery >=", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryLessThan(BigDecimal value) {
            addCriterion("summery <", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryLessThanOrEqualTo(BigDecimal value) {
            addCriterion("summery <=", value, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryIn(List<BigDecimal> values) {
            addCriterion("summery in", values, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryNotIn(List<BigDecimal> values) {
            addCriterion("summery not in", values, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("summery between", value1, value2, "summery");
            return (Criteria) this;
        }

        public Criteria andSummeryNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("summery not between", value1, value2, "summery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryIsNull() {
            addCriterion("paysummery is null");
            return (Criteria) this;
        }

        public Criteria andPaysummeryIsNotNull() {
            addCriterion("paysummery is not null");
            return (Criteria) this;
        }

        public Criteria andPaysummeryEqualTo(BigDecimal value) {
            addCriterion("paysummery =", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryNotEqualTo(BigDecimal value) {
            addCriterion("paysummery <>", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryGreaterThan(BigDecimal value) {
            addCriterion("paysummery >", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("paysummery >=", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryLessThan(BigDecimal value) {
            addCriterion("paysummery <", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryLessThanOrEqualTo(BigDecimal value) {
            addCriterion("paysummery <=", value, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryIn(List<BigDecimal> values) {
            addCriterion("paysummery in", values, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryNotIn(List<BigDecimal> values) {
            addCriterion("paysummery not in", values, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("paysummery between", value1, value2, "paysummery");
            return (Criteria) this;
        }

        public Criteria andPaysummeryNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("paysummery not between", value1, value2, "paysummery");
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