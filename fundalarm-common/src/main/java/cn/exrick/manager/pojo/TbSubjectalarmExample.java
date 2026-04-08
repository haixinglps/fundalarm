package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbSubjectalarmExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbSubjectalarmExample() {
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

        public Criteria andSubjectidIsNull() {
            addCriterion("subjectid is null");
            return (Criteria) this;
        }

        public Criteria andSubjectidIsNotNull() {
            addCriterion("subjectid is not null");
            return (Criteria) this;
        }

        public Criteria andSubjectidEqualTo(Integer value) {
            addCriterion("subjectid =", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotEqualTo(Integer value) {
            addCriterion("subjectid <>", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidGreaterThan(Integer value) {
            addCriterion("subjectid >", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidGreaterThanOrEqualTo(Integer value) {
            addCriterion("subjectid >=", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidLessThan(Integer value) {
            addCriterion("subjectid <", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidLessThanOrEqualTo(Integer value) {
            addCriterion("subjectid <=", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidIn(List<Integer> values) {
            addCriterion("subjectid in", values, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotIn(List<Integer> values) {
            addCriterion("subjectid not in", values, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidBetween(Integer value1, Integer value2) {
            addCriterion("subjectid between", value1, value2, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotBetween(Integer value1, Integer value2) {
            addCriterion("subjectid not between", value1, value2, "subjectid");
            return (Criteria) this;
        }

        public Criteria andWeixinidIsNull() {
            addCriterion("weixinid is null");
            return (Criteria) this;
        }

        public Criteria andWeixinidIsNotNull() {
            addCriterion("weixinid is not null");
            return (Criteria) this;
        }

        public Criteria andWeixinidEqualTo(Integer value) {
            addCriterion("weixinid =", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidNotEqualTo(Integer value) {
            addCriterion("weixinid <>", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidGreaterThan(Integer value) {
            addCriterion("weixinid >", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidGreaterThanOrEqualTo(Integer value) {
            addCriterion("weixinid >=", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidLessThan(Integer value) {
            addCriterion("weixinid <", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidLessThanOrEqualTo(Integer value) {
            addCriterion("weixinid <=", value, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidIn(List<Integer> values) {
            addCriterion("weixinid in", values, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidNotIn(List<Integer> values) {
            addCriterion("weixinid not in", values, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidBetween(Integer value1, Integer value2) {
            addCriterion("weixinid between", value1, value2, "weixinid");
            return (Criteria) this;
        }

        public Criteria andWeixinidNotBetween(Integer value1, Integer value2) {
            addCriterion("weixinid not between", value1, value2, "weixinid");
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

        public Criteria andKeywordIsNull() {
            addCriterion("keyword is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNotNull() {
            addCriterion("keyword is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordEqualTo(String value) {
            addCriterion("keyword =", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotEqualTo(String value) {
            addCriterion("keyword <>", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThan(String value) {
            addCriterion("keyword >", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThanOrEqualTo(String value) {
            addCriterion("keyword >=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThan(String value) {
            addCriterion("keyword <", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThanOrEqualTo(String value) {
            addCriterion("keyword <=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLike(String value) {
            addCriterion("keyword like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotLike(String value) {
            addCriterion("keyword not like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordIn(List<String> values) {
            addCriterion("keyword in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotIn(List<String> values) {
            addCriterion("keyword not in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordBetween(String value1, String value2) {
            addCriterion("keyword between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotBetween(String value1, String value2) {
            addCriterion("keyword not between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
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

        public Criteria andGroupnameIsNull() {
            addCriterion("groupname is null");
            return (Criteria) this;
        }

        public Criteria andGroupnameIsNotNull() {
            addCriterion("groupname is not null");
            return (Criteria) this;
        }

        public Criteria andGroupnameEqualTo(String value) {
            addCriterion("groupname =", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotEqualTo(String value) {
            addCriterion("groupname <>", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThan(String value) {
            addCriterion("groupname >", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThanOrEqualTo(String value) {
            addCriterion("groupname >=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThan(String value) {
            addCriterion("groupname <", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThanOrEqualTo(String value) {
            addCriterion("groupname <=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLike(String value) {
            addCriterion("groupname like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotLike(String value) {
            addCriterion("groupname not like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameIn(List<String> values) {
            addCriterion("groupname in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotIn(List<String> values) {
            addCriterion("groupname not in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameBetween(String value1, String value2) {
            addCriterion("groupname between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotBetween(String value1, String value2) {
            addCriterion("groupname not between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andPdtIsNull() {
            addCriterion("pdt is null");
            return (Criteria) this;
        }

        public Criteria andPdtIsNotNull() {
            addCriterion("pdt is not null");
            return (Criteria) this;
        }

        public Criteria andPdtEqualTo(Long value) {
            addCriterion("pdt =", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtNotEqualTo(Long value) {
            addCriterion("pdt <>", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtGreaterThan(Long value) {
            addCriterion("pdt >", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtGreaterThanOrEqualTo(Long value) {
            addCriterion("pdt >=", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtLessThan(Long value) {
            addCriterion("pdt <", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtLessThanOrEqualTo(Long value) {
            addCriterion("pdt <=", value, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtIn(List<Long> values) {
            addCriterion("pdt in", values, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtNotIn(List<Long> values) {
            addCriterion("pdt not in", values, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtBetween(Long value1, Long value2) {
            addCriterion("pdt between", value1, value2, "pdt");
            return (Criteria) this;
        }

        public Criteria andPdtNotBetween(Long value1, Long value2) {
            addCriterion("pdt not between", value1, value2, "pdt");
            return (Criteria) this;
        }

        public Criteria andCdtIsNull() {
            addCriterion("cdt is null");
            return (Criteria) this;
        }

        public Criteria andCdtIsNotNull() {
            addCriterion("cdt is not null");
            return (Criteria) this;
        }

        public Criteria andCdtEqualTo(Long value) {
            addCriterion("cdt =", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtNotEqualTo(Long value) {
            addCriterion("cdt <>", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtGreaterThan(Long value) {
            addCriterion("cdt >", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtGreaterThanOrEqualTo(Long value) {
            addCriterion("cdt >=", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtLessThan(Long value) {
            addCriterion("cdt <", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtLessThanOrEqualTo(Long value) {
            addCriterion("cdt <=", value, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtIn(List<Long> values) {
            addCriterion("cdt in", values, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtNotIn(List<Long> values) {
            addCriterion("cdt not in", values, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtBetween(Long value1, Long value2) {
            addCriterion("cdt between", value1, value2, "cdt");
            return (Criteria) this;
        }

        public Criteria andCdtNotBetween(Long value1, Long value2) {
            addCriterion("cdt not between", value1, value2, "cdt");
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