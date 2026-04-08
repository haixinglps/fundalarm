package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbChatgptExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbChatgptExample() {
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

        public Criteria andTemplateurlIsNull() {
            addCriterion("templateurl is null");
            return (Criteria) this;
        }

        public Criteria andTemplateurlIsNotNull() {
            addCriterion("templateurl is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateurlEqualTo(String value) {
            addCriterion("templateurl =", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlNotEqualTo(String value) {
            addCriterion("templateurl <>", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlGreaterThan(String value) {
            addCriterion("templateurl >", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlGreaterThanOrEqualTo(String value) {
            addCriterion("templateurl >=", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlLessThan(String value) {
            addCriterion("templateurl <", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlLessThanOrEqualTo(String value) {
            addCriterion("templateurl <=", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlLike(String value) {
            addCriterion("templateurl like", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlNotLike(String value) {
            addCriterion("templateurl not like", value, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlIn(List<String> values) {
            addCriterion("templateurl in", values, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlNotIn(List<String> values) {
            addCriterion("templateurl not in", values, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlBetween(String value1, String value2) {
            addCriterion("templateurl between", value1, value2, "templateurl");
            return (Criteria) this;
        }

        public Criteria andTemplateurlNotBetween(String value1, String value2) {
            addCriterion("templateurl not between", value1, value2, "templateurl");
            return (Criteria) this;
        }

        public Criteria andDataurlIsNull() {
            addCriterion("dataurl is null");
            return (Criteria) this;
        }

        public Criteria andDataurlIsNotNull() {
            addCriterion("dataurl is not null");
            return (Criteria) this;
        }

        public Criteria andDataurlEqualTo(String value) {
            addCriterion("dataurl =", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlNotEqualTo(String value) {
            addCriterion("dataurl <>", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlGreaterThan(String value) {
            addCriterion("dataurl >", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlGreaterThanOrEqualTo(String value) {
            addCriterion("dataurl >=", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlLessThan(String value) {
            addCriterion("dataurl <", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlLessThanOrEqualTo(String value) {
            addCriterion("dataurl <=", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlLike(String value) {
            addCriterion("dataurl like", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlNotLike(String value) {
            addCriterion("dataurl not like", value, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlIn(List<String> values) {
            addCriterion("dataurl in", values, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlNotIn(List<String> values) {
            addCriterion("dataurl not in", values, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlBetween(String value1, String value2) {
            addCriterion("dataurl between", value1, value2, "dataurl");
            return (Criteria) this;
        }

        public Criteria andDataurlNotBetween(String value1, String value2) {
            addCriterion("dataurl not between", value1, value2, "dataurl");
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

        public Criteria andPaystatusIsNull() {
            addCriterion("paystatus is null");
            return (Criteria) this;
        }

        public Criteria andPaystatusIsNotNull() {
            addCriterion("paystatus is not null");
            return (Criteria) this;
        }

        public Criteria andPaystatusEqualTo(Integer value) {
            addCriterion("paystatus =", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusNotEqualTo(Integer value) {
            addCriterion("paystatus <>", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusGreaterThan(Integer value) {
            addCriterion("paystatus >", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("paystatus >=", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusLessThan(Integer value) {
            addCriterion("paystatus <", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusLessThanOrEqualTo(Integer value) {
            addCriterion("paystatus <=", value, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusIn(List<Integer> values) {
            addCriterion("paystatus in", values, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusNotIn(List<Integer> values) {
            addCriterion("paystatus not in", values, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusBetween(Integer value1, Integer value2) {
            addCriterion("paystatus between", value1, value2, "paystatus");
            return (Criteria) this;
        }

        public Criteria andPaystatusNotBetween(Integer value1, Integer value2) {
            addCriterion("paystatus not between", value1, value2, "paystatus");
            return (Criteria) this;
        }

        public Criteria andResulturlIsNull() {
            addCriterion("resulturl is null");
            return (Criteria) this;
        }

        public Criteria andResulturlIsNotNull() {
            addCriterion("resulturl is not null");
            return (Criteria) this;
        }

        public Criteria andResulturlEqualTo(String value) {
            addCriterion("resulturl =", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlNotEqualTo(String value) {
            addCriterion("resulturl <>", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlGreaterThan(String value) {
            addCriterion("resulturl >", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlGreaterThanOrEqualTo(String value) {
            addCriterion("resulturl >=", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlLessThan(String value) {
            addCriterion("resulturl <", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlLessThanOrEqualTo(String value) {
            addCriterion("resulturl <=", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlLike(String value) {
            addCriterion("resulturl like", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlNotLike(String value) {
            addCriterion("resulturl not like", value, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlIn(List<String> values) {
            addCriterion("resulturl in", values, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlNotIn(List<String> values) {
            addCriterion("resulturl not in", values, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlBetween(String value1, String value2) {
            addCriterion("resulturl between", value1, value2, "resulturl");
            return (Criteria) this;
        }

        public Criteria andResulturlNotBetween(String value1, String value2) {
            addCriterion("resulturl not between", value1, value2, "resulturl");
            return (Criteria) this;
        }

        public Criteria andFinishtIsNull() {
            addCriterion("finisht is null");
            return (Criteria) this;
        }

        public Criteria andFinishtIsNotNull() {
            addCriterion("finisht is not null");
            return (Criteria) this;
        }

        public Criteria andFinishtEqualTo(Date value) {
            addCriterion("finisht =", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtNotEqualTo(Date value) {
            addCriterion("finisht <>", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtGreaterThan(Date value) {
            addCriterion("finisht >", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtGreaterThanOrEqualTo(Date value) {
            addCriterion("finisht >=", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtLessThan(Date value) {
            addCriterion("finisht <", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtLessThanOrEqualTo(Date value) {
            addCriterion("finisht <=", value, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtIn(List<Date> values) {
            addCriterion("finisht in", values, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtNotIn(List<Date> values) {
            addCriterion("finisht not in", values, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtBetween(Date value1, Date value2) {
            addCriterion("finisht between", value1, value2, "finisht");
            return (Criteria) this;
        }

        public Criteria andFinishtNotBetween(Date value1, Date value2) {
            addCriterion("finisht not between", value1, value2, "finisht");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(String value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(String value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(String value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(String value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(String value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(String value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLike(String value) {
            addCriterion("uid like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotLike(String value) {
            addCriterion("uid not like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<String> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<String> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(String value1, String value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(String value1, String value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andRepeattagIsNull() {
            addCriterion("repeattag is null");
            return (Criteria) this;
        }

        public Criteria andRepeattagIsNotNull() {
            addCriterion("repeattag is not null");
            return (Criteria) this;
        }

        public Criteria andRepeattagEqualTo(Integer value) {
            addCriterion("repeattag =", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagNotEqualTo(Integer value) {
            addCriterion("repeattag <>", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagGreaterThan(Integer value) {
            addCriterion("repeattag >", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagGreaterThanOrEqualTo(Integer value) {
            addCriterion("repeattag >=", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagLessThan(Integer value) {
            addCriterion("repeattag <", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagLessThanOrEqualTo(Integer value) {
            addCriterion("repeattag <=", value, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagIn(List<Integer> values) {
            addCriterion("repeattag in", values, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagNotIn(List<Integer> values) {
            addCriterion("repeattag not in", values, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagBetween(Integer value1, Integer value2) {
            addCriterion("repeattag between", value1, value2, "repeattag");
            return (Criteria) this;
        }

        public Criteria andRepeattagNotBetween(Integer value1, Integer value2) {
            addCriterion("repeattag not between", value1, value2, "repeattag");
            return (Criteria) this;
        }

        public Criteria andTemplatenameIsNull() {
            addCriterion("templatename is null");
            return (Criteria) this;
        }

        public Criteria andTemplatenameIsNotNull() {
            addCriterion("templatename is not null");
            return (Criteria) this;
        }

        public Criteria andTemplatenameEqualTo(String value) {
            addCriterion("templatename =", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameNotEqualTo(String value) {
            addCriterion("templatename <>", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameGreaterThan(String value) {
            addCriterion("templatename >", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameGreaterThanOrEqualTo(String value) {
            addCriterion("templatename >=", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameLessThan(String value) {
            addCriterion("templatename <", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameLessThanOrEqualTo(String value) {
            addCriterion("templatename <=", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameLike(String value) {
            addCriterion("templatename like", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameNotLike(String value) {
            addCriterion("templatename not like", value, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameIn(List<String> values) {
            addCriterion("templatename in", values, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameNotIn(List<String> values) {
            addCriterion("templatename not in", values, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameBetween(String value1, String value2) {
            addCriterion("templatename between", value1, value2, "templatename");
            return (Criteria) this;
        }

        public Criteria andTemplatenameNotBetween(String value1, String value2) {
            addCriterion("templatename not between", value1, value2, "templatename");
            return (Criteria) this;
        }

        public Criteria andDatanameIsNull() {
            addCriterion("dataname is null");
            return (Criteria) this;
        }

        public Criteria andDatanameIsNotNull() {
            addCriterion("dataname is not null");
            return (Criteria) this;
        }

        public Criteria andDatanameEqualTo(String value) {
            addCriterion("dataname =", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameNotEqualTo(String value) {
            addCriterion("dataname <>", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameGreaterThan(String value) {
            addCriterion("dataname >", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameGreaterThanOrEqualTo(String value) {
            addCriterion("dataname >=", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameLessThan(String value) {
            addCriterion("dataname <", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameLessThanOrEqualTo(String value) {
            addCriterion("dataname <=", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameLike(String value) {
            addCriterion("dataname like", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameNotLike(String value) {
            addCriterion("dataname not like", value, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameIn(List<String> values) {
            addCriterion("dataname in", values, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameNotIn(List<String> values) {
            addCriterion("dataname not in", values, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameBetween(String value1, String value2) {
            addCriterion("dataname between", value1, value2, "dataname");
            return (Criteria) this;
        }

        public Criteria andDatanameNotBetween(String value1, String value2) {
            addCriterion("dataname not between", value1, value2, "dataname");
            return (Criteria) this;
        }

        public Criteria andCyclecountIsNull() {
            addCriterion("cyclecount is null");
            return (Criteria) this;
        }

        public Criteria andCyclecountIsNotNull() {
            addCriterion("cyclecount is not null");
            return (Criteria) this;
        }

        public Criteria andCyclecountEqualTo(Integer value) {
            addCriterion("cyclecount =", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountNotEqualTo(Integer value) {
            addCriterion("cyclecount <>", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountGreaterThan(Integer value) {
            addCriterion("cyclecount >", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("cyclecount >=", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountLessThan(Integer value) {
            addCriterion("cyclecount <", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountLessThanOrEqualTo(Integer value) {
            addCriterion("cyclecount <=", value, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountIn(List<Integer> values) {
            addCriterion("cyclecount in", values, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountNotIn(List<Integer> values) {
            addCriterion("cyclecount not in", values, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountBetween(Integer value1, Integer value2) {
            addCriterion("cyclecount between", value1, value2, "cyclecount");
            return (Criteria) this;
        }

        public Criteria andCyclecountNotBetween(Integer value1, Integer value2) {
            addCriterion("cyclecount not between", value1, value2, "cyclecount");
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