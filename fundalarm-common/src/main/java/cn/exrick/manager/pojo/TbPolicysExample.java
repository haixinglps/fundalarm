package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbPolicysExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbPolicysExample() {
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

        public Criteria andCustomeridIsNull() {
            addCriterion("customerid is null");
            return (Criteria) this;
        }

        public Criteria andCustomeridIsNotNull() {
            addCriterion("customerid is not null");
            return (Criteria) this;
        }

        public Criteria andCustomeridEqualTo(Long value) {
            addCriterion("customerid =", value, "customerid");
            return (Criteria) this;
        }
        public Criteria andCustomernamelike(String value) {
            addCriterion("customername like", value, "customername");
            return (Criteria) this;
        }
        public Criteria andproductnamelike(String value) {
            addCriterion("productname like", value, "productname");
            return (Criteria) this;
        }
        public Criteria andCustomertypeEqualTo(Integer value) {
            addCriterion("customertype =", value, "customertype");
            return (Criteria) this;
        }
        public Criteria andtemptagEqualTo(Integer value) {
            addCriterion("temptag =", value, "temptag");
            return (Criteria) this;
        }


        public Criteria andCustomeridNotEqualTo(Long value) {
            addCriterion("customerid <>", value, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridGreaterThan(Long value) {
            addCriterion("customerid >", value, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridGreaterThanOrEqualTo(Long value) {
            addCriterion("customerid >=", value, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridLessThan(Long value) {
            addCriterion("customerid <", value, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridLessThanOrEqualTo(Long value) {
            addCriterion("customerid <=", value, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridIn(List<Long> values) {
            addCriterion("customerid in", values, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridNotIn(List<Long> values) {
            addCriterion("customerid not in", values, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridBetween(Long value1, Long value2) {
            addCriterion("customerid between", value1, value2, "customerid");
            return (Criteria) this;
        }

        public Criteria andCustomeridNotBetween(Long value1, Long value2) {
            addCriterion("customerid not between", value1, value2, "customerid");
            return (Criteria) this;
        }

        public Criteria andBegintimeIsNull() {
            addCriterion("begintime is null");
            return (Criteria) this;
        }

        public Criteria andBegintimeIsNotNull() {
            addCriterion("begintime is not null");
            return (Criteria) this;
        }

        public Criteria andBegintimeEqualTo(Date value) {
            addCriterion("begintime =", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotEqualTo(Date value) {
            addCriterion("begintime <>", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeGreaterThan(Date value) {
            addCriterion("begintime >", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeGreaterThanOrEqualTo(Date value) {
            addCriterion("begintime >=", value, "begintime");
            return (Criteria) this;
        }
        public Criteria andversionEqualTo(int value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }
        public Criteria andBegintimeLessThan(Date value) {
            addCriterion("begintime <", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeLessThanOrEqualTo(Date value) {
            addCriterion("begintime <=", value, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeIn(List<Date> values) {
            addCriterion("begintime in", values, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotIn(List<Date> values) {
            addCriterion("begintime not in", values, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeBetween(Date value1, Date value2) {
            addCriterion("begintime between", value1, value2, "begintime");
            return (Criteria) this;
        }

        public Criteria andBegintimeNotBetween(Date value1, Date value2) {
            addCriterion("begintime not between", value1, value2, "begintime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNull() {
            addCriterion("endtime is null");
            return (Criteria) this;
        }

        public Criteria andEndtimeIsNotNull() {
            addCriterion("endtime is not null");
            return (Criteria) this;
        }

        public Criteria andEndtimeEqualTo(Date value) {
            addCriterion("endtime =", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotEqualTo(Date value) {
            addCriterion("endtime <>", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThan(Date value) {
            addCriterion("endtime >", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("endtime >=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThan(Date value) {
            addCriterion("endtime <", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeLessThanOrEqualTo(Date value) {
            addCriterion("endtime <=", value, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeIn(List<Date> values) {
            addCriterion("endtime in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotIn(List<Date> values) {
            addCriterion("endtime not in", values, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeBetween(Date value1, Date value2) {
            addCriterion("endtime between", value1, value2, "endtime");
            return (Criteria) this;
        }

        public Criteria andEndtimeNotBetween(Date value1, Date value2) {
            addCriterion("endtime not between", value1, value2, "endtime");
            return (Criteria) this;
        }

        public Criteria andBzqxIsNull() {
            addCriterion("bzqx is null");
            return (Criteria) this;
        }

        public Criteria andBzqxIsNotNull() {
            addCriterion("bzqx is not null");
            return (Criteria) this;
        }

        public Criteria andBzqxEqualTo(Integer value) {
            addCriterion("bzqx =", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxNotEqualTo(Integer value) {
            addCriterion("bzqx <>", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxGreaterThan(Integer value) {
            addCriterion("bzqx >", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxGreaterThanOrEqualTo(Integer value) {
            addCriterion("bzqx >=", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxLessThan(Integer value) {
            addCriterion("bzqx <", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxLessThanOrEqualTo(Integer value) {
            addCriterion("bzqx <=", value, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxIn(List<Integer> values) {
            addCriterion("bzqx in", values, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxNotIn(List<Integer> values) {
            addCriterion("bzqx not in", values, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxBetween(Integer value1, Integer value2) {
            addCriterion("bzqx between", value1, value2, "bzqx");
            return (Criteria) this;
        }

        public Criteria andBzqxNotBetween(Integer value1, Integer value2) {
            addCriterion("bzqx not between", value1, value2, "bzqx");
            return (Criteria) this;
        }

        public Criteria andPaymentIsNull() {
            addCriterion("payment is null");
            return (Criteria) this;
        }

        public Criteria andPaymentIsNotNull() {
            addCriterion("payment is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentEqualTo(BigDecimal value) {
            addCriterion("payment =", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotEqualTo(BigDecimal value) {
            addCriterion("payment <>", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentGreaterThan(BigDecimal value) {
            addCriterion("payment >", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("payment >=", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentLessThan(BigDecimal value) {
            addCriterion("payment <", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("payment <=", value, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentIn(List<BigDecimal> values) {
            addCriterion("payment in", values, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotIn(List<BigDecimal> values) {
            addCriterion("payment not in", values, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("payment between", value1, value2, "payment");
            return (Criteria) this;
        }

        public Criteria andPaymentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("payment not between", value1, value2, "payment");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIsNull() {
            addCriterion("peoplecount is null");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIsNotNull() {
            addCriterion("peoplecount is not null");
            return (Criteria) this;
        }

        public Criteria andPeoplecountEqualTo(Integer value) {
            addCriterion("peoplecount =", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotEqualTo(Integer value) {
            addCriterion("peoplecount <>", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountGreaterThan(Integer value) {
            addCriterion("peoplecount >", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountGreaterThanOrEqualTo(Integer value) {
            addCriterion("peoplecount >=", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountLessThan(Integer value) {
            addCriterion("peoplecount <", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountLessThanOrEqualTo(Integer value) {
            addCriterion("peoplecount <=", value, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountIn(List<Integer> values) {
            addCriterion("peoplecount in", values, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotIn(List<Integer> values) {
            addCriterion("peoplecount not in", values, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountBetween(Integer value1, Integer value2) {
            addCriterion("peoplecount between", value1, value2, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPeoplecountNotBetween(Integer value1, Integer value2) {
            addCriterion("peoplecount not between", value1, value2, "peoplecount");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeIsNull() {
            addCriterion("paymenttype is null");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeIsNotNull() {
            addCriterion("paymenttype is not null");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeEqualTo(Integer value) {
            addCriterion("paymenttype =", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeNotEqualTo(Integer value) {
            addCriterion("paymenttype <>", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeGreaterThan(Integer value) {
            addCriterion("paymenttype >", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("paymenttype >=", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeLessThan(Integer value) {
            addCriterion("paymenttype <", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeLessThanOrEqualTo(Integer value) {
            addCriterion("paymenttype <=", value, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeIn(List<Integer> values) {
            addCriterion("paymenttype in", values, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeNotIn(List<Integer> values) {
            addCriterion("paymenttype not in", values, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeBetween(Integer value1, Integer value2) {
            addCriterion("paymenttype between", value1, value2, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andPaymenttypeNotBetween(Integer value1, Integer value2) {
            addCriterion("paymenttype not between", value1, value2, "paymenttype");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageIsNull() {
            addCriterion("havesendmessage is null");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageIsNotNull() {
            addCriterion("havesendmessage is not null");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageEqualTo(Integer value) {
            addCriterion("havesendmessage =", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageNotEqualTo(Integer value) {
            addCriterion("havesendmessage <>", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageGreaterThan(Integer value) {
            addCriterion("havesendmessage >", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageGreaterThanOrEqualTo(Integer value) {
            addCriterion("havesendmessage >=", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageLessThan(Integer value) {
            addCriterion("havesendmessage <", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageLessThanOrEqualTo(Integer value) {
            addCriterion("havesendmessage <=", value, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageIn(List<Integer> values) {
            addCriterion("havesendmessage in", values, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageNotIn(List<Integer> values) {
            addCriterion("havesendmessage not in", values, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageBetween(Integer value1, Integer value2) {
            addCriterion("havesendmessage between", value1, value2, "havesendmessage");
            return (Criteria) this;
        }

        public Criteria andHavesendmessageNotBetween(Integer value1, Integer value2) {
            addCriterion("havesendmessage not between", value1, value2, "havesendmessage");
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

        public Criteria andWaitcountsIsNull() {
            addCriterion("waitcounts is null");
            return (Criteria) this;
        }

        public Criteria andWaitcountsIsNotNull() {
            addCriterion("waitcounts is not null");
            return (Criteria) this;
        }

        public Criteria andWaitcountsEqualTo(Integer value) {
            addCriterion("waitcounts =", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsNotEqualTo(Integer value) {
            addCriterion("waitcounts <>", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsGreaterThan(Integer value) {
            addCriterion("waitcounts >", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsGreaterThanOrEqualTo(Integer value) {
            addCriterion("waitcounts >=", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsLessThan(Integer value) {
            addCriterion("waitcounts <", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsLessThanOrEqualTo(Integer value) {
            addCriterion("waitcounts <=", value, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsIn(List<Integer> values) {
            addCriterion("waitcounts in", values, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsNotIn(List<Integer> values) {
            addCriterion("waitcounts not in", values, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsBetween(Integer value1, Integer value2) {
            addCriterion("waitcounts between", value1, value2, "waitcounts");
            return (Criteria) this;
        }

        public Criteria andWaitcountsNotBetween(Integer value1, Integer value2) {
            addCriterion("waitcounts not between", value1, value2, "waitcounts");
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