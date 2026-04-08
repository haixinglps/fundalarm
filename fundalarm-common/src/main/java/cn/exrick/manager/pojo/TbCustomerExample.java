package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbCustomerExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbCustomerExample() {
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

        public Criteria andMannameIsNull() {
            addCriterion("manname is null");
            return (Criteria) this;
        }

        public Criteria andMannameIsNotNull() {
            addCriterion("manname is not null");
            return (Criteria) this;
        }

        public Criteria andMannameEqualTo(String value) {
            addCriterion("manname =", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotEqualTo(String value) {
            addCriterion("manname <>", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameGreaterThan(String value) {
            addCriterion("manname >", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameGreaterThanOrEqualTo(String value) {
            addCriterion("manname >=", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLessThan(String value) {
            addCriterion("manname <", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLessThanOrEqualTo(String value) {
            addCriterion("manname <=", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameLike(String value) {
            addCriterion("manname like", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotLike(String value) {
            addCriterion("manname not like", value, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameIn(List<String> values) {
            addCriterion("manname in", values, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotIn(List<String> values) {
            addCriterion("manname not in", values, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameBetween(String value1, String value2) {
            addCriterion("manname between", value1, value2, "manname");
            return (Criteria) this;
        }

        public Criteria andMannameNotBetween(String value1, String value2) {
            addCriterion("manname not between", value1, value2, "manname");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andSystemnumIsNull() {
            addCriterion("systemnum is null");
            return (Criteria) this;
        }

        public Criteria andSystemnumIsNotNull() {
            addCriterion("systemnum is not null");
            return (Criteria) this;
        }

        public Criteria andSystemnumEqualTo(String value) {
            addCriterion("systemnum =", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumNotEqualTo(String value) {
            addCriterion("systemnum <>", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumGreaterThan(String value) {
            addCriterion("systemnum >", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumGreaterThanOrEqualTo(String value) {
            addCriterion("systemnum >=", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumLessThan(String value) {
            addCriterion("systemnum <", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumLessThanOrEqualTo(String value) {
            addCriterion("systemnum <=", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumLike(String value) {
            addCriterion("systemnum like", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumNotLike(String value) {
            addCriterion("systemnum not like", value, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumIn(List<String> values) {
            addCriterion("systemnum in", values, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumNotIn(List<String> values) {
            addCriterion("systemnum not in", values, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumBetween(String value1, String value2) {
            addCriterion("systemnum between", value1, value2, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSystemnumNotBetween(String value1, String value2) {
            addCriterion("systemnum not between", value1, value2, "systemnum");
            return (Criteria) this;
        }

        public Criteria andSignpicIsNull() {
            addCriterion("signpic is null");
            return (Criteria) this;
        }

        public Criteria andSignpicIsNotNull() {
            addCriterion("signpic is not null");
            return (Criteria) this;
        }

        public Criteria andSignpicEqualTo(String value) {
            addCriterion("signpic =", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicNotEqualTo(String value) {
            addCriterion("signpic <>", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicGreaterThan(String value) {
            addCriterion("signpic >", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicGreaterThanOrEqualTo(String value) {
            addCriterion("signpic >=", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicLessThan(String value) {
            addCriterion("signpic <", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicLessThanOrEqualTo(String value) {
            addCriterion("signpic <=", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicLike(String value) {
            addCriterion("signpic like", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicNotLike(String value) {
            addCriterion("signpic not like", value, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicIn(List<String> values) {
            addCriterion("signpic in", values, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicNotIn(List<String> values) {
            addCriterion("signpic not in", values, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicBetween(String value1, String value2) {
            addCriterion("signpic between", value1, value2, "signpic");
            return (Criteria) this;
        }

        public Criteria andSignpicNotBetween(String value1, String value2) {
            addCriterion("signpic not between", value1, value2, "signpic");
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

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("updated is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("updated is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Date value) {
            addCriterion("updated =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Date value) {
            addCriterion("updated <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Date value) {
            addCriterion("updated >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("updated >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Date value) {
            addCriterion("updated <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("updated <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Date> values) {
            addCriterion("updated in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Date> values) {
            addCriterion("updated not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Date value1, Date value2) {
            addCriterion("updated between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("updated not between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(String value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(String value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(String value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(String value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(String value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(String value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLike(String value) {
            addCriterion("sex like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotLike(String value) {
            addCriterion("sex not like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<String> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<String> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(String value1, String value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(String value1, String value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andOrderidIsNull() {
            addCriterion("orderid is null");
            return (Criteria) this;
        }

        public Criteria andOrderidIsNotNull() {
            addCriterion("orderid is not null");
            return (Criteria) this;
        }

        public Criteria andOrderidEqualTo(Long value) {
            addCriterion("orderid =", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotEqualTo(Long value) {
            addCriterion("orderid <>", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThan(Long value) {
            addCriterion("orderid >", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThanOrEqualTo(Long value) {
            addCriterion("orderid >=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThan(Long value) {
            addCriterion("orderid <", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThanOrEqualTo(Long value) {
            addCriterion("orderid <=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidIn(List<Long> values) {
            addCriterion("orderid in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotIn(List<Long> values) {
            addCriterion("orderid not in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidBetween(Long value1, Long value2) {
            addCriterion("orderid between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotBetween(Long value1, Long value2) {
            addCriterion("orderid not between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andCreditcardIsNull() {
            addCriterion("creditcard is null");
            return (Criteria) this;
        }

        public Criteria andCreditcardIsNotNull() {
            addCriterion("creditcard is not null");
            return (Criteria) this;
        }

        public Criteria andCreditcardEqualTo(String value) {
            addCriterion("creditcard =", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardNotEqualTo(String value) {
            addCriterion("creditcard <>", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardGreaterThan(String value) {
            addCriterion("creditcard >", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardGreaterThanOrEqualTo(String value) {
            addCriterion("creditcard >=", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardLessThan(String value) {
            addCriterion("creditcard <", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardLessThanOrEqualTo(String value) {
            addCriterion("creditcard <=", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardLike(String value) {
            addCriterion("creditcard like", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardNotLike(String value) {
            addCriterion("creditcard not like", value, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardIn(List<String> values) {
            addCriterion("creditcard in", values, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardNotIn(List<String> values) {
            addCriterion("creditcard not in", values, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardBetween(String value1, String value2) {
            addCriterion("creditcard between", value1, value2, "creditcard");
            return (Criteria) this;
        }

        public Criteria andCreditcardNotBetween(String value1, String value2) {
            addCriterion("creditcard not between", value1, value2, "creditcard");
            return (Criteria) this;
        }

        public Criteria andAgeIsNull() {
            addCriterion("age is null");
            return (Criteria) this;
        }

        public Criteria andAgeIsNotNull() {
            addCriterion("age is not null");
            return (Criteria) this;
        }

        public Criteria andAgeEqualTo(String value) {
            addCriterion("age =", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotEqualTo(String value) {
            addCriterion("age <>", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThan(String value) {
            addCriterion("age >", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThanOrEqualTo(String value) {
            addCriterion("age >=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThan(String value) {
            addCriterion("age <", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThanOrEqualTo(String value) {
            addCriterion("age <=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLike(String value) {
            addCriterion("age like", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotLike(String value) {
            addCriterion("age not like", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeIn(List<String> values) {
            addCriterion("age in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotIn(List<String> values) {
            addCriterion("age not in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeBetween(String value1, String value2) {
            addCriterion("age between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotBetween(String value1, String value2) {
            addCriterion("age not between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andHealthIsNull() {
            addCriterion("health is null");
            return (Criteria) this;
        }

        public Criteria andHealthIsNotNull() {
            addCriterion("health is not null");
            return (Criteria) this;
        }

        public Criteria andHealthEqualTo(String value) {
            addCriterion("health =", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthNotEqualTo(String value) {
            addCriterion("health <>", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthGreaterThan(String value) {
            addCriterion("health >", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthGreaterThanOrEqualTo(String value) {
            addCriterion("health >=", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthLessThan(String value) {
            addCriterion("health <", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthLessThanOrEqualTo(String value) {
            addCriterion("health <=", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthLike(String value) {
            addCriterion("health like", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthNotLike(String value) {
            addCriterion("health not like", value, "health");
            return (Criteria) this;
        }

        public Criteria andHealthIn(List<String> values) {
            addCriterion("health in", values, "health");
            return (Criteria) this;
        }

        public Criteria andHealthNotIn(List<String> values) {
            addCriterion("health not in", values, "health");
            return (Criteria) this;
        }

        public Criteria andHealthBetween(String value1, String value2) {
            addCriterion("health between", value1, value2, "health");
            return (Criteria) this;
        }

        public Criteria andHealthNotBetween(String value1, String value2) {
            addCriterion("health not between", value1, value2, "health");
            return (Criteria) this;
        }

        public Criteria andCardtypeIsNull() {
            addCriterion("cardtype is null");
            return (Criteria) this;
        }

        public Criteria andCardtypeIsNotNull() {
            addCriterion("cardtype is not null");
            return (Criteria) this;
        }

        public Criteria andCardtypeEqualTo(String value) {
            addCriterion("cardtype =", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeNotEqualTo(String value) {
            addCriterion("cardtype <>", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeGreaterThan(String value) {
            addCriterion("cardtype >", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeGreaterThanOrEqualTo(String value) {
            addCriterion("cardtype >=", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeLessThan(String value) {
            addCriterion("cardtype <", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeLessThanOrEqualTo(String value) {
            addCriterion("cardtype <=", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeLike(String value) {
            addCriterion("cardtype like", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeNotLike(String value) {
            addCriterion("cardtype not like", value, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeIn(List<String> values) {
            addCriterion("cardtype in", values, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeNotIn(List<String> values) {
            addCriterion("cardtype not in", values, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeBetween(String value1, String value2) {
            addCriterion("cardtype between", value1, value2, "cardtype");
            return (Criteria) this;
        }

        public Criteria andCardtypeNotBetween(String value1, String value2) {
            addCriterion("cardtype not between", value1, value2, "cardtype");
            return (Criteria) this;
        }

        public Criteria andUpdatetagIsNull() {
            addCriterion("updatetag is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetagIsNotNull() {
            addCriterion("updatetag is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetagEqualTo(Integer value) {
            addCriterion("updatetag =", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagNotEqualTo(Integer value) {
            addCriterion("updatetag <>", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagGreaterThan(Integer value) {
            addCriterion("updatetag >", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagGreaterThanOrEqualTo(Integer value) {
            addCriterion("updatetag >=", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagLessThan(Integer value) {
            addCriterion("updatetag <", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagLessThanOrEqualTo(Integer value) {
            addCriterion("updatetag <=", value, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagIn(List<Integer> values) {
            addCriterion("updatetag in", values, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagNotIn(List<Integer> values) {
            addCriterion("updatetag not in", values, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagBetween(Integer value1, Integer value2) {
            addCriterion("updatetag between", value1, value2, "updatetag");
            return (Criteria) this;
        }

        public Criteria andUpdatetagNotBetween(Integer value1, Integer value2) {
            addCriterion("updatetag not between", value1, value2, "updatetag");
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

        public Criteria andCompanynameIsNull() {
            addCriterion("companyname is null");
            return (Criteria) this;
        }

        public Criteria andCompanynameIsNotNull() {
            addCriterion("companyname is not null");
            return (Criteria) this;
        }

        public Criteria andCompanynameEqualTo(String value) {
            addCriterion("companyname =", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotEqualTo(String value) {
            addCriterion("companyname <>", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameGreaterThan(String value) {
            addCriterion("companyname >", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameGreaterThanOrEqualTo(String value) {
            addCriterion("companyname >=", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLessThan(String value) {
            addCriterion("companyname <", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLessThanOrEqualTo(String value) {
            addCriterion("companyname <=", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameLike(String value) {
            addCriterion("companyname like", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotLike(String value) {
            addCriterion("companyname not like", value, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameIn(List<String> values) {
            addCriterion("companyname in", values, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotIn(List<String> values) {
            addCriterion("companyname not in", values, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameBetween(String value1, String value2) {
            addCriterion("companyname between", value1, value2, "companyname");
            return (Criteria) this;
        }

        public Criteria andCompanynameNotBetween(String value1, String value2) {
            addCriterion("companyname not between", value1, value2, "companyname");
            return (Criteria) this;
        }

        public Criteria andAddrIsNull() {
            addCriterion("addr is null");
            return (Criteria) this;
        }

        public Criteria andAddrIsNotNull() {
            addCriterion("addr is not null");
            return (Criteria) this;
        }

        public Criteria andAddrEqualTo(String value) {
            addCriterion("addr =", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotEqualTo(String value) {
            addCriterion("addr <>", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrGreaterThan(String value) {
            addCriterion("addr >", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrGreaterThanOrEqualTo(String value) {
            addCriterion("addr >=", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLessThan(String value) {
            addCriterion("addr <", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLessThanOrEqualTo(String value) {
            addCriterion("addr <=", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLike(String value) {
            addCriterion("addr like", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotLike(String value) {
            addCriterion("addr not like", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrIn(List<String> values) {
            addCriterion("addr in", values, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotIn(List<String> values) {
            addCriterion("addr not in", values, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrBetween(String value1, String value2) {
            addCriterion("addr between", value1, value2, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotBetween(String value1, String value2) {
            addCriterion("addr not between", value1, value2, "addr");
            return (Criteria) this;
        }

        public Criteria andAddr2IsNull() {
            addCriterion("addr2 is null");
            return (Criteria) this;
        }

        public Criteria andAddr2IsNotNull() {
            addCriterion("addr2 is not null");
            return (Criteria) this;
        }

        public Criteria andAddr2EqualTo(String value) {
            addCriterion("addr2 =", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2NotEqualTo(String value) {
            addCriterion("addr2 <>", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2GreaterThan(String value) {
            addCriterion("addr2 >", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2GreaterThanOrEqualTo(String value) {
            addCriterion("addr2 >=", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2LessThan(String value) {
            addCriterion("addr2 <", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2LessThanOrEqualTo(String value) {
            addCriterion("addr2 <=", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2Like(String value) {
            addCriterion("addr2 like", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2NotLike(String value) {
            addCriterion("addr2 not like", value, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2In(List<String> values) {
            addCriterion("addr2 in", values, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2NotIn(List<String> values) {
            addCriterion("addr2 not in", values, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2Between(String value1, String value2) {
            addCriterion("addr2 between", value1, value2, "addr2");
            return (Criteria) this;
        }

        public Criteria andAddr2NotBetween(String value1, String value2) {
            addCriterion("addr2 not between", value1, value2, "addr2");
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

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
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

        public Criteria andBuycountIsNull() {
            addCriterion("buycount is null");
            return (Criteria) this;
        }

        public Criteria andBuycountIsNotNull() {
            addCriterion("buycount is not null");
            return (Criteria) this;
        }

        public Criteria andBuycountEqualTo(Integer value) {
            addCriterion("buycount =", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotEqualTo(Integer value) {
            addCriterion("buycount <>", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountGreaterThan(Integer value) {
            addCriterion("buycount >", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountGreaterThanOrEqualTo(Integer value) {
            addCriterion("buycount >=", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountLessThan(Integer value) {
            addCriterion("buycount <", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountLessThanOrEqualTo(Integer value) {
            addCriterion("buycount <=", value, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountIn(List<Integer> values) {
            addCriterion("buycount in", values, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotIn(List<Integer> values) {
            addCriterion("buycount not in", values, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountBetween(Integer value1, Integer value2) {
            addCriterion("buycount between", value1, value2, "buycount");
            return (Criteria) this;
        }

        public Criteria andBuycountNotBetween(Integer value1, Integer value2) {
            addCriterion("buycount not between", value1, value2, "buycount");
            return (Criteria) this;
        }

        public Criteria andRecordcountIsNull() {
            addCriterion("recordcount is null");
            return (Criteria) this;
        }

        public Criteria andRecordcountIsNotNull() {
            addCriterion("recordcount is not null");
            return (Criteria) this;
        }

        public Criteria andRecordcountEqualTo(Integer value) {
            addCriterion("recordcount =", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountNotEqualTo(Integer value) {
            addCriterion("recordcount <>", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountGreaterThan(Integer value) {
            addCriterion("recordcount >", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("recordcount >=", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountLessThan(Integer value) {
            addCriterion("recordcount <", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountLessThanOrEqualTo(Integer value) {
            addCriterion("recordcount <=", value, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountIn(List<Integer> values) {
            addCriterion("recordcount in", values, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountNotIn(List<Integer> values) {
            addCriterion("recordcount not in", values, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountBetween(Integer value1, Integer value2) {
            addCriterion("recordcount between", value1, value2, "recordcount");
            return (Criteria) this;
        }

        public Criteria andRecordcountNotBetween(Integer value1, Integer value2) {
            addCriterion("recordcount not between", value1, value2, "recordcount");
            return (Criteria) this;
        }

        public Criteria andCustomertypeIsNull() {
            addCriterion("customertype is null");
            return (Criteria) this;
        }

        public Criteria andCustomertypeIsNotNull() {
            addCriterion("customertype is not null");
            return (Criteria) this;
        }

        public Criteria andCustomertypeEqualTo(Integer value) {
            addCriterion("customertype =", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeNotEqualTo(Integer value) {
            addCriterion("customertype <>", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeGreaterThan(Integer value) {
            addCriterion("customertype >", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("customertype >=", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeLessThan(Integer value) {
            addCriterion("customertype <", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeLessThanOrEqualTo(Integer value) {
            addCriterion("customertype <=", value, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeIn(List<Integer> values) {
            addCriterion("customertype in", values, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeNotIn(List<Integer> values) {
            addCriterion("customertype not in", values, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeBetween(Integer value1, Integer value2) {
            addCriterion("customertype between", value1, value2, "customertype");
            return (Criteria) this;
        }

        public Criteria andCustomertypeNotBetween(Integer value1, Integer value2) {
            addCriterion("customertype not between", value1, value2, "customertype");
            return (Criteria) this;
        }

        public Criteria andSourceidIsNull() {
            addCriterion("sourceid is null");
            return (Criteria) this;
        }

        public Criteria andSourceidIsNotNull() {
            addCriterion("sourceid is not null");
            return (Criteria) this;
        }

        public Criteria andSourceidEqualTo(Long value) {
            addCriterion("sourceid =", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidNotEqualTo(Long value) {
            addCriterion("sourceid <>", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidGreaterThan(Long value) {
            addCriterion("sourceid >", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidGreaterThanOrEqualTo(Long value) {
            addCriterion("sourceid >=", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidLessThan(Long value) {
            addCriterion("sourceid <", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidLessThanOrEqualTo(Long value) {
            addCriterion("sourceid <=", value, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidIn(List<Long> values) {
            addCriterion("sourceid in", values, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidNotIn(List<Long> values) {
            addCriterion("sourceid not in", values, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidBetween(Long value1, Long value2) {
            addCriterion("sourceid between", value1, value2, "sourceid");
            return (Criteria) this;
        }

        public Criteria andSourceidNotBetween(Long value1, Long value2) {
            addCriterion("sourceid not between", value1, value2, "sourceid");
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

        public Criteria andAddrcodeIsNull() {
            addCriterion("addrcode is null");
            return (Criteria) this;
        }

        public Criteria andAddrcodeIsNotNull() {
            addCriterion("addrcode is not null");
            return (Criteria) this;
        }

        public Criteria andAddrcodeEqualTo(String value) {
            addCriterion("addrcode =", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeNotEqualTo(String value) {
            addCriterion("addrcode <>", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeGreaterThan(String value) {
            addCriterion("addrcode >", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeGreaterThanOrEqualTo(String value) {
            addCriterion("addrcode >=", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeLessThan(String value) {
            addCriterion("addrcode <", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeLessThanOrEqualTo(String value) {
            addCriterion("addrcode <=", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeLike(String value) {
            addCriterion("addrcode like", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeNotLike(String value) {
            addCriterion("addrcode not like", value, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeIn(List<String> values) {
            addCriterion("addrcode in", values, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeNotIn(List<String> values) {
            addCriterion("addrcode not in", values, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeBetween(String value1, String value2) {
            addCriterion("addrcode between", value1, value2, "addrcode");
            return (Criteria) this;
        }

        public Criteria andAddrcodeNotBetween(String value1, String value2) {
            addCriterion("addrcode not between", value1, value2, "addrcode");
            return (Criteria) this;
        }

        public Criteria andCompanyidIsNull() {
            addCriterion("companyid is null");
            return (Criteria) this;
        }

        public Criteria andCompanyidIsNotNull() {
            addCriterion("companyid is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyidEqualTo(Long value) {
            addCriterion("companyid =", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidNotEqualTo(Long value) {
            addCriterion("companyid <>", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidGreaterThan(Long value) {
            addCriterion("companyid >", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidGreaterThanOrEqualTo(Long value) {
            addCriterion("companyid >=", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidLessThan(Long value) {
            addCriterion("companyid <", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidLessThanOrEqualTo(Long value) {
            addCriterion("companyid <=", value, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidIn(List<Long> values) {
            addCriterion("companyid in", values, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidNotIn(List<Long> values) {
            addCriterion("companyid not in", values, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidBetween(Long value1, Long value2) {
            addCriterion("companyid between", value1, value2, "companyid");
            return (Criteria) this;
        }

        public Criteria andCompanyidNotBetween(Long value1, Long value2) {
            addCriterion("companyid not between", value1, value2, "companyid");
            return (Criteria) this;
        }

        public Criteria andHangyeIsNull() {
            addCriterion("hangye is null");
            return (Criteria) this;
        }

        public Criteria andHangyeIsNotNull() {
            addCriterion("hangye is not null");
            return (Criteria) this;
        }

        public Criteria andHangyeEqualTo(String value) {
            addCriterion("hangye =", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeNotEqualTo(String value) {
            addCriterion("hangye <>", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeGreaterThan(String value) {
            addCriterion("hangye >", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeGreaterThanOrEqualTo(String value) {
            addCriterion("hangye >=", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeLessThan(String value) {
            addCriterion("hangye <", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeLessThanOrEqualTo(String value) {
            addCriterion("hangye <=", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeLike(String value) {
            addCriterion("hangye like", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeNotLike(String value) {
            addCriterion("hangye not like", value, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeIn(List<String> values) {
            addCriterion("hangye in", values, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeNotIn(List<String> values) {
            addCriterion("hangye not in", values, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeBetween(String value1, String value2) {
            addCriterion("hangye between", value1, value2, "hangye");
            return (Criteria) this;
        }

        public Criteria andHangyeNotBetween(String value1, String value2) {
            addCriterion("hangye not between", value1, value2, "hangye");
            return (Criteria) this;
        }

        public Criteria andGuimoIsNull() {
            addCriterion("guimo is null");
            return (Criteria) this;
        }

        public Criteria andGuimoIsNotNull() {
            addCriterion("guimo is not null");
            return (Criteria) this;
        }

        public Criteria andGuimoEqualTo(String value) {
            addCriterion("guimo =", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoNotEqualTo(String value) {
            addCriterion("guimo <>", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoGreaterThan(String value) {
            addCriterion("guimo >", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoGreaterThanOrEqualTo(String value) {
            addCriterion("guimo >=", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoLessThan(String value) {
            addCriterion("guimo <", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoLessThanOrEqualTo(String value) {
            addCriterion("guimo <=", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoLike(String value) {
            addCriterion("guimo like", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoNotLike(String value) {
            addCriterion("guimo not like", value, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoIn(List<String> values) {
            addCriterion("guimo in", values, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoNotIn(List<String> values) {
            addCriterion("guimo not in", values, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoBetween(String value1, String value2) {
            addCriterion("guimo between", value1, value2, "guimo");
            return (Criteria) this;
        }

        public Criteria andGuimoNotBetween(String value1, String value2) {
            addCriterion("guimo not between", value1, value2, "guimo");
            return (Criteria) this;
        }

        public Criteria andZhizaoIsNull() {
            addCriterion("zhizao is null");
            return (Criteria) this;
        }

        public Criteria andZhizaoIsNotNull() {
            addCriterion("zhizao is not null");
            return (Criteria) this;
        }

        public Criteria andZhizaoEqualTo(String value) {
            addCriterion("zhizao =", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoNotEqualTo(String value) {
            addCriterion("zhizao <>", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoGreaterThan(String value) {
            addCriterion("zhizao >", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoGreaterThanOrEqualTo(String value) {
            addCriterion("zhizao >=", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoLessThan(String value) {
            addCriterion("zhizao <", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoLessThanOrEqualTo(String value) {
            addCriterion("zhizao <=", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoLike(String value) {
            addCriterion("zhizao like", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoNotLike(String value) {
            addCriterion("zhizao not like", value, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoIn(List<String> values) {
            addCriterion("zhizao in", values, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoNotIn(List<String> values) {
            addCriterion("zhizao not in", values, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoBetween(String value1, String value2) {
            addCriterion("zhizao between", value1, value2, "zhizao");
            return (Criteria) this;
        }

        public Criteria andZhizaoNotBetween(String value1, String value2) {
            addCriterion("zhizao not between", value1, value2, "zhizao");
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