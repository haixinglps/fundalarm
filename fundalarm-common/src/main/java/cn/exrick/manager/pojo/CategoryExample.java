package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class CategoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CategoryExample() {
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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Integer value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Integer value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Integer value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Integer value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Integer> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Integer> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Integer value1, Integer value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
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

        public Criteria andLevelEqualTo(Byte value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Byte value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Byte value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Byte value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Byte value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Byte value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Byte> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Byte> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Byte value1, Byte value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Byte value1, Byte value2) {
            addCriterion("level not between", value1, value2, "level");
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(String value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(String value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(String value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(String value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(String value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLike(String value) {
            addCriterion("create_time like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotLike(String value) {
            addCriterion("create_time not like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<String> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<String> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(String value1, String value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(String value1, String value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(String value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(String value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(String value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(String value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(String value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLike(String value) {
            addCriterion("update_time like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotLike(String value) {
            addCriterion("update_time not like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<String> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<String> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(String value1, String value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(String value1, String value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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

        public Criteria andBizkeyIsNull() {
            addCriterion("bizKey is null");
            return (Criteria) this;
        }

        public Criteria andBizkeyIsNotNull() {
            addCriterion("bizKey is not null");
            return (Criteria) this;
        }

        public Criteria andBizkeyEqualTo(String value) {
            addCriterion("bizKey =", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyNotEqualTo(String value) {
            addCriterion("bizKey <>", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyGreaterThan(String value) {
            addCriterion("bizKey >", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyGreaterThanOrEqualTo(String value) {
            addCriterion("bizKey >=", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyLessThan(String value) {
            addCriterion("bizKey <", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyLessThanOrEqualTo(String value) {
            addCriterion("bizKey <=", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyLike(String value) {
            addCriterion("bizKey like", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyNotLike(String value) {
            addCriterion("bizKey not like", value, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyIn(List<String> values) {
            addCriterion("bizKey in", values, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyNotIn(List<String> values) {
            addCriterion("bizKey not in", values, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyBetween(String value1, String value2) {
            addCriterion("bizKey between", value1, value2, "bizkey");
            return (Criteria) this;
        }

        public Criteria andBizkeyNotBetween(String value1, String value2) {
            addCriterion("bizKey not between", value1, value2, "bizkey");
            return (Criteria) this;
        }

        public Criteria andFirstletterIsNull() {
            addCriterion("firstLetter is null");
            return (Criteria) this;
        }

        public Criteria andFirstletterIsNotNull() {
            addCriterion("firstLetter is not null");
            return (Criteria) this;
        }

        public Criteria andFirstletterEqualTo(String value) {
            addCriterion("firstLetter =", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterNotEqualTo(String value) {
            addCriterion("firstLetter <>", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterGreaterThan(String value) {
            addCriterion("firstLetter >", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterGreaterThanOrEqualTo(String value) {
            addCriterion("firstLetter >=", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterLessThan(String value) {
            addCriterion("firstLetter <", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterLessThanOrEqualTo(String value) {
            addCriterion("firstLetter <=", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterLike(String value) {
            addCriterion("firstLetter like", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterNotLike(String value) {
            addCriterion("firstLetter not like", value, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterIn(List<String> values) {
            addCriterion("firstLetter in", values, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterNotIn(List<String> values) {
            addCriterion("firstLetter not in", values, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterBetween(String value1, String value2) {
            addCriterion("firstLetter between", value1, value2, "firstletter");
            return (Criteria) this;
        }

        public Criteria andFirstletterNotBetween(String value1, String value2) {
            addCriterion("firstLetter not between", value1, value2, "firstletter");
            return (Criteria) this;
        }

        public Criteria andImgurlIsNull() {
            addCriterion("imgUrl is null");
            return (Criteria) this;
        }

        public Criteria andImgurlIsNotNull() {
            addCriterion("imgUrl is not null");
            return (Criteria) this;
        }

        public Criteria andImgurlEqualTo(String value) {
            addCriterion("imgUrl =", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlNotEqualTo(String value) {
            addCriterion("imgUrl <>", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlGreaterThan(String value) {
            addCriterion("imgUrl >", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlGreaterThanOrEqualTo(String value) {
            addCriterion("imgUrl >=", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlLessThan(String value) {
            addCriterion("imgUrl <", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlLessThanOrEqualTo(String value) {
            addCriterion("imgUrl <=", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlLike(String value) {
            addCriterion("imgUrl like", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlNotLike(String value) {
            addCriterion("imgUrl not like", value, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlIn(List<String> values) {
            addCriterion("imgUrl in", values, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlNotIn(List<String> values) {
            addCriterion("imgUrl not in", values, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlBetween(String value1, String value2) {
            addCriterion("imgUrl between", value1, value2, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImgurlNotBetween(String value1, String value2) {
            addCriterion("imgUrl not between", value1, value2, "imgurl");
            return (Criteria) this;
        }

        public Criteria andImportanceIsNull() {
            addCriterion("importance is null");
            return (Criteria) this;
        }

        public Criteria andImportanceIsNotNull() {
            addCriterion("importance is not null");
            return (Criteria) this;
        }

        public Criteria andImportanceEqualTo(String value) {
            addCriterion("importance =", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceNotEqualTo(String value) {
            addCriterion("importance <>", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceGreaterThan(String value) {
            addCriterion("importance >", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceGreaterThanOrEqualTo(String value) {
            addCriterion("importance >=", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceLessThan(String value) {
            addCriterion("importance <", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceLessThanOrEqualTo(String value) {
            addCriterion("importance <=", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceLike(String value) {
            addCriterion("importance like", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceNotLike(String value) {
            addCriterion("importance not like", value, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceIn(List<String> values) {
            addCriterion("importance in", values, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceNotIn(List<String> values) {
            addCriterion("importance not in", values, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceBetween(String value1, String value2) {
            addCriterion("importance between", value1, value2, "importance");
            return (Criteria) this;
        }

        public Criteria andImportanceNotBetween(String value1, String value2) {
            addCriterion("importance not between", value1, value2, "importance");
            return (Criteria) this;
        }

        public Criteria andLeadtimesIsNull() {
            addCriterion("leadTimes is null");
            return (Criteria) this;
        }

        public Criteria andLeadtimesIsNotNull() {
            addCriterion("leadTimes is not null");
            return (Criteria) this;
        }

        public Criteria andLeadtimesEqualTo(String value) {
            addCriterion("leadTimes =", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesNotEqualTo(String value) {
            addCriterion("leadTimes <>", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesGreaterThan(String value) {
            addCriterion("leadTimes >", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesGreaterThanOrEqualTo(String value) {
            addCriterion("leadTimes >=", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesLessThan(String value) {
            addCriterion("leadTimes <", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesLessThanOrEqualTo(String value) {
            addCriterion("leadTimes <=", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesLike(String value) {
            addCriterion("leadTimes like", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesNotLike(String value) {
            addCriterion("leadTimes not like", value, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesIn(List<String> values) {
            addCriterion("leadTimes in", values, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesNotIn(List<String> values) {
            addCriterion("leadTimes not in", values, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesBetween(String value1, String value2) {
            addCriterion("leadTimes between", value1, value2, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLeadtimesNotBetween(String value1, String value2) {
            addCriterion("leadTimes not between", value1, value2, "leadtimes");
            return (Criteria) this;
        }

        public Criteria andLimitupcountIsNull() {
            addCriterion("limitUpCount is null");
            return (Criteria) this;
        }

        public Criteria andLimitupcountIsNotNull() {
            addCriterion("limitUpCount is not null");
            return (Criteria) this;
        }

        public Criteria andLimitupcountEqualTo(String value) {
            addCriterion("limitUpCount =", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountNotEqualTo(String value) {
            addCriterion("limitUpCount <>", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountGreaterThan(String value) {
            addCriterion("limitUpCount >", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountGreaterThanOrEqualTo(String value) {
            addCriterion("limitUpCount >=", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountLessThan(String value) {
            addCriterion("limitUpCount <", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountLessThanOrEqualTo(String value) {
            addCriterion("limitUpCount <=", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountLike(String value) {
            addCriterion("limitUpCount like", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountNotLike(String value) {
            addCriterion("limitUpCount not like", value, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountIn(List<String> values) {
            addCriterion("limitUpCount in", values, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountNotIn(List<String> values) {
            addCriterion("limitUpCount not in", values, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountBetween(String value1, String value2) {
            addCriterion("limitUpCount between", value1, value2, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimitupcountNotBetween(String value1, String value2) {
            addCriterion("limitUpCount not between", value1, value2, "limitupcount");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIsNull() {
            addCriterion("limitUpTimes is null");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIsNotNull() {
            addCriterion("limitUpTimes is not null");
            return (Criteria) this;
        }

        public Criteria andLimituptimesEqualTo(String value) {
            addCriterion("limitUpTimes =", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotEqualTo(String value) {
            addCriterion("limitUpTimes <>", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesGreaterThan(String value) {
            addCriterion("limitUpTimes >", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesGreaterThanOrEqualTo(String value) {
            addCriterion("limitUpTimes >=", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesLessThan(String value) {
            addCriterion("limitUpTimes <", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesLessThanOrEqualTo(String value) {
            addCriterion("limitUpTimes <=", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesLike(String value) {
            addCriterion("limitUpTimes like", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotLike(String value) {
            addCriterion("limitUpTimes not like", value, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesIn(List<String> values) {
            addCriterion("limitUpTimes in", values, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotIn(List<String> values) {
            addCriterion("limitUpTimes not in", values, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesBetween(String value1, String value2) {
            addCriterion("limitUpTimes between", value1, value2, "limituptimes");
            return (Criteria) this;
        }

        public Criteria andLimituptimesNotBetween(String value1, String value2) {
            addCriterion("limitUpTimes not between", value1, value2, "limituptimes");
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

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andSelectreasonIsNull() {
            addCriterion("selectReason is null");
            return (Criteria) this;
        }

        public Criteria andSelectreasonIsNotNull() {
            addCriterion("selectReason is not null");
            return (Criteria) this;
        }

        public Criteria andSelectreasonEqualTo(String value) {
            addCriterion("selectReason =", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonNotEqualTo(String value) {
            addCriterion("selectReason <>", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonGreaterThan(String value) {
            addCriterion("selectReason >", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonGreaterThanOrEqualTo(String value) {
            addCriterion("selectReason >=", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonLessThan(String value) {
            addCriterion("selectReason <", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonLessThanOrEqualTo(String value) {
            addCriterion("selectReason <=", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonLike(String value) {
            addCriterion("selectReason like", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonNotLike(String value) {
            addCriterion("selectReason not like", value, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonIn(List<String> values) {
            addCriterion("selectReason in", values, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonNotIn(List<String> values) {
            addCriterion("selectReason not in", values, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonBetween(String value1, String value2) {
            addCriterion("selectReason between", value1, value2, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSelectreasonNotBetween(String value1, String value2) {
            addCriterion("selectReason not between", value1, value2, "selectreason");
            return (Criteria) this;
        }

        public Criteria andSortIsNull() {
            addCriterion("sort is null");
            return (Criteria) this;
        }

        public Criteria andSortIsNotNull() {
            addCriterion("sort is not null");
            return (Criteria) this;
        }

        public Criteria andSortEqualTo(Integer value) {
            addCriterion("sort =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Integer value) {
            addCriterion("sort <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Integer value) {
            addCriterion("sort >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Integer value) {
            addCriterion("sort <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Integer value) {
            addCriterion("sort <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Integer> values) {
            addCriterion("sort in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Integer> values) {
            addCriterion("sort not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Integer value1, Integer value2) {
            addCriterion("sort between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Integer value1, Integer value2) {
            addCriterion("sort not between", value1, value2, "sort");
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

        public Criteria andStockcountIsNull() {
            addCriterion("stockCount is null");
            return (Criteria) this;
        }

        public Criteria andStockcountIsNotNull() {
            addCriterion("stockCount is not null");
            return (Criteria) this;
        }

        public Criteria andStockcountEqualTo(String value) {
            addCriterion("stockCount =", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountNotEqualTo(String value) {
            addCriterion("stockCount <>", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountGreaterThan(String value) {
            addCriterion("stockCount >", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountGreaterThanOrEqualTo(String value) {
            addCriterion("stockCount >=", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountLessThan(String value) {
            addCriterion("stockCount <", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountLessThanOrEqualTo(String value) {
            addCriterion("stockCount <=", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountLike(String value) {
            addCriterion("stockCount like", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountNotLike(String value) {
            addCriterion("stockCount not like", value, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountIn(List<String> values) {
            addCriterion("stockCount in", values, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountNotIn(List<String> values) {
            addCriterion("stockCount not in", values, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountBetween(String value1, String value2) {
            addCriterion("stockCount between", value1, value2, "stockcount");
            return (Criteria) this;
        }

        public Criteria andStockcountNotBetween(String value1, String value2) {
            addCriterion("stockCount not between", value1, value2, "stockcount");
            return (Criteria) this;
        }

        public Criteria andCreatebyIsNull() {
            addCriterion("createby is null");
            return (Criteria) this;
        }

        public Criteria andCreatebyIsNotNull() {
            addCriterion("createby is not null");
            return (Criteria) this;
        }

        public Criteria andCreatebyEqualTo(String value) {
            addCriterion("createby =", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyNotEqualTo(String value) {
            addCriterion("createby <>", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyGreaterThan(String value) {
            addCriterion("createby >", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyGreaterThanOrEqualTo(String value) {
            addCriterion("createby >=", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyLessThan(String value) {
            addCriterion("createby <", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyLessThanOrEqualTo(String value) {
            addCriterion("createby <=", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyLike(String value) {
            addCriterion("createby like", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyNotLike(String value) {
            addCriterion("createby not like", value, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyIn(List<String> values) {
            addCriterion("createby in", values, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyNotIn(List<String> values) {
            addCriterion("createby not in", values, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyBetween(String value1, String value2) {
            addCriterion("createby between", value1, value2, "createby");
            return (Criteria) this;
        }

        public Criteria andCreatebyNotBetween(String value1, String value2) {
            addCriterion("createby not between", value1, value2, "createby");
            return (Criteria) this;
        }

        public Criteria andDetailIsNull() {
            addCriterion("detail is null");
            return (Criteria) this;
        }

        public Criteria andDetailIsNotNull() {
            addCriterion("detail is not null");
            return (Criteria) this;
        }

        public Criteria andDetailEqualTo(String value) {
            addCriterion("detail =", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotEqualTo(String value) {
            addCriterion("detail <>", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThan(String value) {
            addCriterion("detail >", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThanOrEqualTo(String value) {
            addCriterion("detail >=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThan(String value) {
            addCriterion("detail <", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThanOrEqualTo(String value) {
            addCriterion("detail <=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLike(String value) {
            addCriterion("detail like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotLike(String value) {
            addCriterion("detail not like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailIn(List<String> values) {
            addCriterion("detail in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotIn(List<String> values) {
            addCriterion("detail not in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailBetween(String value1, String value2) {
            addCriterion("detail between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotBetween(String value1, String value2) {
            addCriterion("detail not between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andParentnameIsNull() {
            addCriterion("parentname is null");
            return (Criteria) this;
        }

        public Criteria andParentnameIsNotNull() {
            addCriterion("parentname is not null");
            return (Criteria) this;
        }

        public Criteria andParentnameEqualTo(String value) {
            addCriterion("parentname =", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameNotEqualTo(String value) {
            addCriterion("parentname <>", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameGreaterThan(String value) {
            addCriterion("parentname >", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameGreaterThanOrEqualTo(String value) {
            addCriterion("parentname >=", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameLessThan(String value) {
            addCriterion("parentname <", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameLessThanOrEqualTo(String value) {
            addCriterion("parentname <=", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameLike(String value) {
            addCriterion("parentname like", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameNotLike(String value) {
            addCriterion("parentname not like", value, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameIn(List<String> values) {
            addCriterion("parentname in", values, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameNotIn(List<String> values) {
            addCriterion("parentname not in", values, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameBetween(String value1, String value2) {
            addCriterion("parentname between", value1, value2, "parentname");
            return (Criteria) this;
        }

        public Criteria andParentnameNotBetween(String value1, String value2) {
            addCriterion("parentname not between", value1, value2, "parentname");
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

        public Criteria andUpdatebyIsNull() {
            addCriterion("updateby is null");
            return (Criteria) this;
        }

        public Criteria andUpdatebyIsNotNull() {
            addCriterion("updateby is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatebyEqualTo(String value) {
            addCriterion("updateby =", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyNotEqualTo(String value) {
            addCriterion("updateby <>", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyGreaterThan(String value) {
            addCriterion("updateby >", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyGreaterThanOrEqualTo(String value) {
            addCriterion("updateby >=", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyLessThan(String value) {
            addCriterion("updateby <", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyLessThanOrEqualTo(String value) {
            addCriterion("updateby <=", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyLike(String value) {
            addCriterion("updateby like", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyNotLike(String value) {
            addCriterion("updateby not like", value, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyIn(List<String> values) {
            addCriterion("updateby in", values, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyNotIn(List<String> values) {
            addCriterion("updateby not in", values, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyBetween(String value1, String value2) {
            addCriterion("updateby between", value1, value2, "updateby");
            return (Criteria) this;
        }

        public Criteria andUpdatebyNotBetween(String value1, String value2) {
            addCriterion("updateby not between", value1, value2, "updateby");
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