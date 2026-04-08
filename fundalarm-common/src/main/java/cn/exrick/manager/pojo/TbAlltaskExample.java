package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbAlltaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbAlltaskExample() {
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

        public Criteria andContentIsNull() {
            addCriterion("content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("content not between", value1, value2, "content");
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

        public Criteria andFeekbackServerIsNull() {
            addCriterion("feekback_server is null");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerIsNotNull() {
            addCriterion("feekback_server is not null");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerEqualTo(String value) {
            addCriterion("feekback_server =", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerNotEqualTo(String value) {
            addCriterion("feekback_server <>", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerGreaterThan(String value) {
            addCriterion("feekback_server >", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerGreaterThanOrEqualTo(String value) {
            addCriterion("feekback_server >=", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerLessThan(String value) {
            addCriterion("feekback_server <", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerLessThanOrEqualTo(String value) {
            addCriterion("feekback_server <=", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerLike(String value) {
            addCriterion("feekback_server like", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerNotLike(String value) {
            addCriterion("feekback_server not like", value, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerIn(List<String> values) {
            addCriterion("feekback_server in", values, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerNotIn(List<String> values) {
            addCriterion("feekback_server not in", values, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerBetween(String value1, String value2) {
            addCriterion("feekback_server between", value1, value2, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andFeekbackServerNotBetween(String value1, String value2) {
            addCriterion("feekback_server not between", value1, value2, "feekbackServer");
            return (Criteria) this;
        }

        public Criteria andRobotnameIsNull() {
            addCriterion("robotname is null");
            return (Criteria) this;
        }

        public Criteria andRobotnameIsNotNull() {
            addCriterion("robotname is not null");
            return (Criteria) this;
        }

        public Criteria andRobotnameEqualTo(String value) {
            addCriterion("robotname =", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameNotEqualTo(String value) {
            addCriterion("robotname <>", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameGreaterThan(String value) {
            addCriterion("robotname >", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameGreaterThanOrEqualTo(String value) {
            addCriterion("robotname >=", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameLessThan(String value) {
            addCriterion("robotname <", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameLessThanOrEqualTo(String value) {
            addCriterion("robotname <=", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameLike(String value) {
            addCriterion("robotname like", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameNotLike(String value) {
            addCriterion("robotname not like", value, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameIn(List<String> values) {
            addCriterion("robotname in", values, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameNotIn(List<String> values) {
            addCriterion("robotname not in", values, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameBetween(String value1, String value2) {
            addCriterion("robotname between", value1, value2, "robotname");
            return (Criteria) this;
        }

        public Criteria andRobotnameNotBetween(String value1, String value2) {
            addCriterion("robotname not between", value1, value2, "robotname");
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

        public Criteria andStarthourIsNull() {
            addCriterion("starthour is null");
            return (Criteria) this;
        }

        public Criteria andStarthourIsNotNull() {
            addCriterion("starthour is not null");
            return (Criteria) this;
        }

        public Criteria andStarthourEqualTo(Integer value) {
            addCriterion("starthour =", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourNotEqualTo(Integer value) {
            addCriterion("starthour <>", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourGreaterThan(Integer value) {
            addCriterion("starthour >", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourGreaterThanOrEqualTo(Integer value) {
            addCriterion("starthour >=", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourLessThan(Integer value) {
            addCriterion("starthour <", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourLessThanOrEqualTo(Integer value) {
            addCriterion("starthour <=", value, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourIn(List<Integer> values) {
            addCriterion("starthour in", values, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourNotIn(List<Integer> values) {
            addCriterion("starthour not in", values, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourBetween(Integer value1, Integer value2) {
            addCriterion("starthour between", value1, value2, "starthour");
            return (Criteria) this;
        }

        public Criteria andStarthourNotBetween(Integer value1, Integer value2) {
            addCriterion("starthour not between", value1, value2, "starthour");
            return (Criteria) this;
        }

        public Criteria andStartminuteIsNull() {
            addCriterion("startminute is null");
            return (Criteria) this;
        }

        public Criteria andStartminuteIsNotNull() {
            addCriterion("startminute is not null");
            return (Criteria) this;
        }

        public Criteria andStartminuteEqualTo(Integer value) {
            addCriterion("startminute =", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteNotEqualTo(Integer value) {
            addCriterion("startminute <>", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteGreaterThan(Integer value) {
            addCriterion("startminute >", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteGreaterThanOrEqualTo(Integer value) {
            addCriterion("startminute >=", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteLessThan(Integer value) {
            addCriterion("startminute <", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteLessThanOrEqualTo(Integer value) {
            addCriterion("startminute <=", value, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteIn(List<Integer> values) {
            addCriterion("startminute in", values, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteNotIn(List<Integer> values) {
            addCriterion("startminute not in", values, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteBetween(Integer value1, Integer value2) {
            addCriterion("startminute between", value1, value2, "startminute");
            return (Criteria) this;
        }

        public Criteria andStartminuteNotBetween(Integer value1, Integer value2) {
            addCriterion("startminute not between", value1, value2, "startminute");
            return (Criteria) this;
        }

        public Criteria andExecucountIsNull() {
            addCriterion("execucount is null");
            return (Criteria) this;
        }

        public Criteria andExecucountIsNotNull() {
            addCriterion("execucount is not null");
            return (Criteria) this;
        }

        public Criteria andExecucountEqualTo(Integer value) {
            addCriterion("execucount =", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountNotEqualTo(Integer value) {
            addCriterion("execucount <>", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountGreaterThan(Integer value) {
            addCriterion("execucount >", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountGreaterThanOrEqualTo(Integer value) {
            addCriterion("execucount >=", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountLessThan(Integer value) {
            addCriterion("execucount <", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountLessThanOrEqualTo(Integer value) {
            addCriterion("execucount <=", value, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountIn(List<Integer> values) {
            addCriterion("execucount in", values, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountNotIn(List<Integer> values) {
            addCriterion("execucount not in", values, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountBetween(Integer value1, Integer value2) {
            addCriterion("execucount between", value1, value2, "execucount");
            return (Criteria) this;
        }

        public Criteria andExecucountNotBetween(Integer value1, Integer value2) {
            addCriterion("execucount not between", value1, value2, "execucount");
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

        public Criteria andSuccesstagIsNull() {
            addCriterion("successtag is null");
            return (Criteria) this;
        }

        public Criteria andSuccesstagIsNotNull() {
            addCriterion("successtag is not null");
            return (Criteria) this;
        }

        public Criteria andSuccesstagEqualTo(Integer value) {
            addCriterion("successtag =", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagNotEqualTo(Integer value) {
            addCriterion("successtag <>", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagGreaterThan(Integer value) {
            addCriterion("successtag >", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagGreaterThanOrEqualTo(Integer value) {
            addCriterion("successtag >=", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagLessThan(Integer value) {
            addCriterion("successtag <", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagLessThanOrEqualTo(Integer value) {
            addCriterion("successtag <=", value, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagIn(List<Integer> values) {
            addCriterion("successtag in", values, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagNotIn(List<Integer> values) {
            addCriterion("successtag not in", values, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagBetween(Integer value1, Integer value2) {
            addCriterion("successtag between", value1, value2, "successtag");
            return (Criteria) this;
        }

        public Criteria andSuccesstagNotBetween(Integer value1, Integer value2) {
            addCriterion("successtag not between", value1, value2, "successtag");
            return (Criteria) this;
        }

        public Criteria andIspublicIsNull() {
            addCriterion("ispublic is null");
            return (Criteria) this;
        }

        public Criteria andIspublicIsNotNull() {
            addCriterion("ispublic is not null");
            return (Criteria) this;
        }

        public Criteria andIspublicEqualTo(String value) {
            addCriterion("ispublic =", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicNotEqualTo(String value) {
            addCriterion("ispublic <>", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicGreaterThan(String value) {
            addCriterion("ispublic >", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicGreaterThanOrEqualTo(String value) {
            addCriterion("ispublic >=", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicLessThan(String value) {
            addCriterion("ispublic <", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicLessThanOrEqualTo(String value) {
            addCriterion("ispublic <=", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicLike(String value) {
            addCriterion("ispublic like", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicNotLike(String value) {
            addCriterion("ispublic not like", value, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicIn(List<String> values) {
            addCriterion("ispublic in", values, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicNotIn(List<String> values) {
            addCriterion("ispublic not in", values, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicBetween(String value1, String value2) {
            addCriterion("ispublic between", value1, value2, "ispublic");
            return (Criteria) this;
        }

        public Criteria andIspublicNotBetween(String value1, String value2) {
            addCriterion("ispublic not between", value1, value2, "ispublic");
            return (Criteria) this;
        }

        public Criteria andPaytagIsNull() {
            addCriterion("paytag is null");
            return (Criteria) this;
        }

        public Criteria andPaytagIsNotNull() {
            addCriterion("paytag is not null");
            return (Criteria) this;
        }

        public Criteria andPaytagEqualTo(Integer value) {
            addCriterion("paytag =", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagNotEqualTo(Integer value) {
            addCriterion("paytag <>", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagGreaterThan(Integer value) {
            addCriterion("paytag >", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagGreaterThanOrEqualTo(Integer value) {
            addCriterion("paytag >=", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagLessThan(Integer value) {
            addCriterion("paytag <", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagLessThanOrEqualTo(Integer value) {
            addCriterion("paytag <=", value, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagIn(List<Integer> values) {
            addCriterion("paytag in", values, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagNotIn(List<Integer> values) {
            addCriterion("paytag not in", values, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagBetween(Integer value1, Integer value2) {
            addCriterion("paytag between", value1, value2, "paytag");
            return (Criteria) this;
        }

        public Criteria andPaytagNotBetween(Integer value1, Integer value2) {
            addCriterion("paytag not between", value1, value2, "paytag");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotIsNull() {
            addCriterion("feekback_robot is null");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotIsNotNull() {
            addCriterion("feekback_robot is not null");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotEqualTo(String value) {
            addCriterion("feekback_robot =", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotNotEqualTo(String value) {
            addCriterion("feekback_robot <>", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotGreaterThan(String value) {
            addCriterion("feekback_robot >", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotGreaterThanOrEqualTo(String value) {
            addCriterion("feekback_robot >=", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotLessThan(String value) {
            addCriterion("feekback_robot <", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotLessThanOrEqualTo(String value) {
            addCriterion("feekback_robot <=", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotLike(String value) {
            addCriterion("feekback_robot like", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotNotLike(String value) {
            addCriterion("feekback_robot not like", value, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotIn(List<String> values) {
            addCriterion("feekback_robot in", values, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotNotIn(List<String> values) {
            addCriterion("feekback_robot not in", values, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotBetween(String value1, String value2) {
            addCriterion("feekback_robot between", value1, value2, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andFeekbackRobotNotBetween(String value1, String value2) {
            addCriterion("feekback_robot not between", value1, value2, "feekbackRobot");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andFuturedateIsNull() {
            addCriterion("futuredate is null");
            return (Criteria) this;
        }

        public Criteria andFuturedateIsNotNull() {
            addCriterion("futuredate is not null");
            return (Criteria) this;
        }

        public Criteria andFuturedateEqualTo(Date value) {
            addCriterion("futuredate =", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateNotEqualTo(Date value) {
            addCriterion("futuredate <>", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateGreaterThan(Date value) {
            addCriterion("futuredate >", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateGreaterThanOrEqualTo(Date value) {
            addCriterion("futuredate >=", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateLessThan(Date value) {
            addCriterion("futuredate <", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateLessThanOrEqualTo(Date value) {
            addCriterion("futuredate <=", value, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateIn(List<Date> values) {
            addCriterion("futuredate in", values, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateNotIn(List<Date> values) {
            addCriterion("futuredate not in", values, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateBetween(Date value1, Date value2) {
            addCriterion("futuredate between", value1, value2, "futuredate");
            return (Criteria) this;
        }

        public Criteria andFuturedateNotBetween(Date value1, Date value2) {
            addCriterion("futuredate not between", value1, value2, "futuredate");
            return (Criteria) this;
        }

        public Criteria andReasonIsNull() {
            addCriterion("reason is null");
            return (Criteria) this;
        }

        public Criteria andReasonIsNotNull() {
            addCriterion("reason is not null");
            return (Criteria) this;
        }

        public Criteria andReasonEqualTo(String value) {
            addCriterion("reason =", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotEqualTo(String value) {
            addCriterion("reason <>", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThan(String value) {
            addCriterion("reason >", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThanOrEqualTo(String value) {
            addCriterion("reason >=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThan(String value) {
            addCriterion("reason <", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThanOrEqualTo(String value) {
            addCriterion("reason <=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLike(String value) {
            addCriterion("reason like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotLike(String value) {
            addCriterion("reason not like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonIn(List<String> values) {
            addCriterion("reason in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotIn(List<String> values) {
            addCriterion("reason not in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonBetween(String value1, String value2) {
            addCriterion("reason between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotBetween(String value1, String value2) {
            addCriterion("reason not between", value1, value2, "reason");
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