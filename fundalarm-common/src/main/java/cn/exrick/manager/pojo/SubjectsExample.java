package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class SubjectsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SubjectsExample() {
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

        public Criteria andAncestorsIsNull() {
            addCriterion("ancestors is null");
            return (Criteria) this;
        }

        public Criteria andAncestorsIsNotNull() {
            addCriterion("ancestors is not null");
            return (Criteria) this;
        }

        public Criteria andAncestorsEqualTo(String value) {
            addCriterion("ancestors =", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsNotEqualTo(String value) {
            addCriterion("ancestors <>", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsGreaterThan(String value) {
            addCriterion("ancestors >", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsGreaterThanOrEqualTo(String value) {
            addCriterion("ancestors >=", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsLessThan(String value) {
            addCriterion("ancestors <", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsLessThanOrEqualTo(String value) {
            addCriterion("ancestors <=", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsLike(String value) {
            addCriterion("ancestors like", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsNotLike(String value) {
            addCriterion("ancestors not like", value, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsIn(List<String> values) {
            addCriterion("ancestors in", values, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsNotIn(List<String> values) {
            addCriterion("ancestors not in", values, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsBetween(String value1, String value2) {
            addCriterion("ancestors between", value1, value2, "ancestors");
            return (Criteria) this;
        }

        public Criteria andAncestorsNotBetween(String value1, String value2) {
            addCriterion("ancestors not between", value1, value2, "ancestors");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(String value) {
            addCriterion("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(String value) {
            addCriterion("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(String value) {
            addCriterion("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(String value) {
            addCriterion("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(String value) {
            addCriterion("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(String value) {
            addCriterion("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLike(String value) {
            addCriterion("date like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotLike(String value) {
            addCriterion("date not like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<String> values) {
            addCriterion("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<String> values) {
            addCriterion("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(String value1, String value2) {
            addCriterion("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(String value1, String value2) {
            addCriterion("date not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andFullnameIsNull() {
            addCriterion("fullName is null");
            return (Criteria) this;
        }

        public Criteria andFullnameIsNotNull() {
            addCriterion("fullName is not null");
            return (Criteria) this;
        }

        public Criteria andFullnameEqualTo(String value) {
            addCriterion("fullName =", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameNotEqualTo(String value) {
            addCriterion("fullName <>", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameGreaterThan(String value) {
            addCriterion("fullName >", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameGreaterThanOrEqualTo(String value) {
            addCriterion("fullName >=", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameLessThan(String value) {
            addCriterion("fullName <", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameLessThanOrEqualTo(String value) {
            addCriterion("fullName <=", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameLike(String value) {
            addCriterion("fullName like", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameNotLike(String value) {
            addCriterion("fullName not like", value, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameIn(List<String> values) {
            addCriterion("fullName in", values, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameNotIn(List<String> values) {
            addCriterion("fullName not in", values, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameBetween(String value1, String value2) {
            addCriterion("fullName between", value1, value2, "fullname");
            return (Criteria) this;
        }

        public Criteria andFullnameNotBetween(String value1, String value2) {
            addCriterion("fullName not between", value1, value2, "fullname");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectIsNull() {
            addCriterion("hasSubSubject is null");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectIsNotNull() {
            addCriterion("hasSubSubject is not null");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectEqualTo(String value) {
            addCriterion("hasSubSubject =", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectNotEqualTo(String value) {
            addCriterion("hasSubSubject <>", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectGreaterThan(String value) {
            addCriterion("hasSubSubject >", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectGreaterThanOrEqualTo(String value) {
            addCriterion("hasSubSubject >=", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectLessThan(String value) {
            addCriterion("hasSubSubject <", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectLessThanOrEqualTo(String value) {
            addCriterion("hasSubSubject <=", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectLike(String value) {
            addCriterion("hasSubSubject like", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectNotLike(String value) {
            addCriterion("hasSubSubject not like", value, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectIn(List<String> values) {
            addCriterion("hasSubSubject in", values, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectNotIn(List<String> values) {
            addCriterion("hasSubSubject not in", values, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectBetween(String value1, String value2) {
            addCriterion("hasSubSubject between", value1, value2, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andHassubsubjectNotBetween(String value1, String value2) {
            addCriterion("hasSubSubject not between", value1, value2, "hassubsubject");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(String value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(String value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(String value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(String value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(String value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(String value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLike(String value) {
            addCriterion("level like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotLike(String value) {
            addCriterion("level not like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<String> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<String> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(String value1, String value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(String value1, String value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andParentidIsNull() {
            addCriterion("parentId is null");
            return (Criteria) this;
        }

        public Criteria andParentidIsNotNull() {
            addCriterion("parentId is not null");
            return (Criteria) this;
        }

        public Criteria andParentidEqualTo(String value) {
            addCriterion("parentId =", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotEqualTo(String value) {
            addCriterion("parentId <>", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidGreaterThan(String value) {
            addCriterion("parentId >", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidGreaterThanOrEqualTo(String value) {
            addCriterion("parentId >=", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidLessThan(String value) {
            addCriterion("parentId <", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidLessThanOrEqualTo(String value) {
            addCriterion("parentId <=", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidLike(String value) {
            addCriterion("parentId like", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotLike(String value) {
            addCriterion("parentId not like", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidIn(List<String> values) {
            addCriterion("parentId in", values, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotIn(List<String> values) {
            addCriterion("parentId not in", values, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidBetween(String value1, String value2) {
            addCriterion("parentId between", value1, value2, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotBetween(String value1, String value2) {
            addCriterion("parentId not between", value1, value2, "parentid");
            return (Criteria) this;
        }

        public Criteria andPctchgIsNull() {
            addCriterion("pctChg is null");
            return (Criteria) this;
        }

        public Criteria andPctchgIsNotNull() {
            addCriterion("pctChg is not null");
            return (Criteria) this;
        }

        public Criteria andPctchgEqualTo(String value) {
            addCriterion("pctChg =", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotEqualTo(String value) {
            addCriterion("pctChg <>", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgGreaterThan(String value) {
            addCriterion("pctChg >", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgGreaterThanOrEqualTo(String value) {
            addCriterion("pctChg >=", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgLessThan(String value) {
            addCriterion("pctChg <", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgLessThanOrEqualTo(String value) {
            addCriterion("pctChg <=", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgLike(String value) {
            addCriterion("pctChg like", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotLike(String value) {
            addCriterion("pctChg not like", value, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgIn(List<String> values) {
            addCriterion("pctChg in", values, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotIn(List<String> values) {
            addCriterion("pctChg not in", values, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgBetween(String value1, String value2) {
            addCriterion("pctChg between", value1, value2, "pctchg");
            return (Criteria) this;
        }

        public Criteria andPctchgNotBetween(String value1, String value2) {
            addCriterion("pctChg not between", value1, value2, "pctchg");
            return (Criteria) this;
        }

        public Criteria andRanktimesIsNull() {
            addCriterion("rankTimes is null");
            return (Criteria) this;
        }

        public Criteria andRanktimesIsNotNull() {
            addCriterion("rankTimes is not null");
            return (Criteria) this;
        }

        public Criteria andRanktimesEqualTo(String value) {
            addCriterion("rankTimes =", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesNotEqualTo(String value) {
            addCriterion("rankTimes <>", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesGreaterThan(String value) {
            addCriterion("rankTimes >", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesGreaterThanOrEqualTo(String value) {
            addCriterion("rankTimes >=", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesLessThan(String value) {
            addCriterion("rankTimes <", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesLessThanOrEqualTo(String value) {
            addCriterion("rankTimes <=", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesLike(String value) {
            addCriterion("rankTimes like", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesNotLike(String value) {
            addCriterion("rankTimes not like", value, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesIn(List<String> values) {
            addCriterion("rankTimes in", values, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesNotIn(List<String> values) {
            addCriterion("rankTimes not in", values, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesBetween(String value1, String value2) {
            addCriterion("rankTimes between", value1, value2, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andRanktimesNotBetween(String value1, String value2) {
            addCriterion("rankTimes not between", value1, value2, "ranktimes");
            return (Criteria) this;
        }

        public Criteria andSubjectidIsNull() {
            addCriterion("subjectId is null");
            return (Criteria) this;
        }

        public Criteria andSubjectidIsNotNull() {
            addCriterion("subjectId is not null");
            return (Criteria) this;
        }

        public Criteria andSubjectidEqualTo(String value) {
            addCriterion("subjectId =", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotEqualTo(String value) {
            addCriterion("subjectId <>", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidGreaterThan(String value) {
            addCriterion("subjectId >", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidGreaterThanOrEqualTo(String value) {
            addCriterion("subjectId >=", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidLessThan(String value) {
            addCriterion("subjectId <", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidLessThanOrEqualTo(String value) {
            addCriterion("subjectId <=", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidLike(String value) {
            addCriterion("subjectId like", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotLike(String value) {
            addCriterion("subjectId not like", value, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidIn(List<String> values) {
            addCriterion("subjectId in", values, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotIn(List<String> values) {
            addCriterion("subjectId not in", values, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidBetween(String value1, String value2) {
            addCriterion("subjectId between", value1, value2, "subjectid");
            return (Criteria) this;
        }

        public Criteria andSubjectidNotBetween(String value1, String value2) {
            addCriterion("subjectId not between", value1, value2, "subjectid");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
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