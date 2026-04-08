package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbOrderchildExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbOrderchildExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andLzxFeeIsNull() {
            addCriterion("lzx_fee is null");
            return (Criteria) this;
        }

        public Criteria andLzxFeeIsNotNull() {
            addCriterion("lzx_fee is not null");
            return (Criteria) this;
        }

        public Criteria andLzxFeeEqualTo(BigDecimal value) {
            addCriterion("lzx_fee =", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeNotEqualTo(BigDecimal value) {
            addCriterion("lzx_fee <>", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeGreaterThan(BigDecimal value) {
            addCriterion("lzx_fee >", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lzx_fee >=", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeLessThan(BigDecimal value) {
            addCriterion("lzx_fee <", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lzx_fee <=", value, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeIn(List<BigDecimal> values) {
            addCriterion("lzx_fee in", values, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeNotIn(List<BigDecimal> values) {
            addCriterion("lzx_fee not in", values, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzx_fee between", value1, value2, "lzxFee");
            return (Criteria) this;
        }

        public Criteria andLzxFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzx_fee not between", value1, value2, "lzxFee");
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
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

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeIsNull() {
            addCriterion("payment_time is null");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeIsNotNull() {
            addCriterion("payment_time is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeEqualTo(Date value) {
            addCriterion("payment_time =", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeNotEqualTo(Date value) {
            addCriterion("payment_time <>", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeGreaterThan(Date value) {
            addCriterion("payment_time >", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("payment_time >=", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeLessThan(Date value) {
            addCriterion("payment_time <", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeLessThanOrEqualTo(Date value) {
            addCriterion("payment_time <=", value, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeIn(List<Date> values) {
            addCriterion("payment_time in", values, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeNotIn(List<Date> values) {
            addCriterion("payment_time not in", values, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeBetween(Date value1, Date value2) {
            addCriterion("payment_time between", value1, value2, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andPaymentTimeNotBetween(Date value1, Date value2) {
            addCriterion("payment_time not between", value1, value2, "paymentTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeIsNull() {
            addCriterion("consign_time is null");
            return (Criteria) this;
        }

        public Criteria andConsignTimeIsNotNull() {
            addCriterion("consign_time is not null");
            return (Criteria) this;
        }

        public Criteria andConsignTimeEqualTo(Date value) {
            addCriterion("consign_time =", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeNotEqualTo(Date value) {
            addCriterion("consign_time <>", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeGreaterThan(Date value) {
            addCriterion("consign_time >", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("consign_time >=", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeLessThan(Date value) {
            addCriterion("consign_time <", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeLessThanOrEqualTo(Date value) {
            addCriterion("consign_time <=", value, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeIn(List<Date> values) {
            addCriterion("consign_time in", values, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeNotIn(List<Date> values) {
            addCriterion("consign_time not in", values, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeBetween(Date value1, Date value2) {
            addCriterion("consign_time between", value1, value2, "consignTime");
            return (Criteria) this;
        }

        public Criteria andConsignTimeNotBetween(Date value1, Date value2) {
            addCriterion("consign_time not between", value1, value2, "consignTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andShippingCodeIsNull() {
            addCriterion("shipping_code is null");
            return (Criteria) this;
        }

        public Criteria andShippingCodeIsNotNull() {
            addCriterion("shipping_code is not null");
            return (Criteria) this;
        }

        public Criteria andShippingCodeEqualTo(String value) {
            addCriterion("shipping_code =", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeNotEqualTo(String value) {
            addCriterion("shipping_code <>", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeGreaterThan(String value) {
            addCriterion("shipping_code >", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeGreaterThanOrEqualTo(String value) {
            addCriterion("shipping_code >=", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeLessThan(String value) {
            addCriterion("shipping_code <", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeLessThanOrEqualTo(String value) {
            addCriterion("shipping_code <=", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeLike(String value) {
            addCriterion("shipping_code like", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeNotLike(String value) {
            addCriterion("shipping_code not like", value, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeIn(List<String> values) {
            addCriterion("shipping_code in", values, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeNotIn(List<String> values) {
            addCriterion("shipping_code not in", values, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeBetween(String value1, String value2) {
            addCriterion("shipping_code between", value1, value2, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andShippingCodeNotBetween(String value1, String value2) {
            addCriterion("shipping_code not between", value1, value2, "shippingCode");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andOripaymentIsNull() {
            addCriterion("oripayment is null");
            return (Criteria) this;
        }

        public Criteria andOripaymentIsNotNull() {
            addCriterion("oripayment is not null");
            return (Criteria) this;
        }

        public Criteria andOripaymentEqualTo(BigDecimal value) {
            addCriterion("oripayment =", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentNotEqualTo(BigDecimal value) {
            addCriterion("oripayment <>", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentGreaterThan(BigDecimal value) {
            addCriterion("oripayment >", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("oripayment >=", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentLessThan(BigDecimal value) {
            addCriterion("oripayment <", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentLessThanOrEqualTo(BigDecimal value) {
            addCriterion("oripayment <=", value, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentIn(List<BigDecimal> values) {
            addCriterion("oripayment in", values, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentNotIn(List<BigDecimal> values) {
            addCriterion("oripayment not in", values, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("oripayment between", value1, value2, "oripayment");
            return (Criteria) this;
        }

        public Criteria andOripaymentNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("oripayment not between", value1, value2, "oripayment");
            return (Criteria) this;
        }

        public Criteria andYwxFeeIsNull() {
            addCriterion("ywx_fee is null");
            return (Criteria) this;
        }

        public Criteria andYwxFeeIsNotNull() {
            addCriterion("ywx_fee is not null");
            return (Criteria) this;
        }

        public Criteria andYwxFeeEqualTo(BigDecimal value) {
            addCriterion("ywx_fee =", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeNotEqualTo(BigDecimal value) {
            addCriterion("ywx_fee <>", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeGreaterThan(BigDecimal value) {
            addCriterion("ywx_fee >", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ywx_fee >=", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeLessThan(BigDecimal value) {
            addCriterion("ywx_fee <", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ywx_fee <=", value, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeIn(List<BigDecimal> values) {
            addCriterion("ywx_fee in", values, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeNotIn(List<BigDecimal> values) {
            addCriterion("ywx_fee not in", values, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywx_fee between", value1, value2, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywx_fee not between", value1, value2, "ywxFee");
            return (Criteria) this;
        }

        public Criteria andYwxtypeIsNull() {
            addCriterion("ywxtype is null");
            return (Criteria) this;
        }

        public Criteria andYwxtypeIsNotNull() {
            addCriterion("ywxtype is not null");
            return (Criteria) this;
        }

        public Criteria andYwxtypeEqualTo(Integer value) {
            addCriterion("ywxtype =", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeNotEqualTo(Integer value) {
            addCriterion("ywxtype <>", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeGreaterThan(Integer value) {
            addCriterion("ywxtype >", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ywxtype >=", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeLessThan(Integer value) {
            addCriterion("ywxtype <", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeLessThanOrEqualTo(Integer value) {
            addCriterion("ywxtype <=", value, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeIn(List<Integer> values) {
            addCriterion("ywxtype in", values, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeNotIn(List<Integer> values) {
            addCriterion("ywxtype not in", values, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeBetween(Integer value1, Integer value2) {
            addCriterion("ywxtype between", value1, value2, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andYwxtypeNotBetween(Integer value1, Integer value2) {
            addCriterion("ywxtype not between", value1, value2, "ywxtype");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagIsNull() {
            addCriterion("selftraveltag is null");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagIsNotNull() {
            addCriterion("selftraveltag is not null");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagEqualTo(Integer value) {
            addCriterion("selftraveltag =", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagNotEqualTo(Integer value) {
            addCriterion("selftraveltag <>", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagGreaterThan(Integer value) {
            addCriterion("selftraveltag >", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagGreaterThanOrEqualTo(Integer value) {
            addCriterion("selftraveltag >=", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagLessThan(Integer value) {
            addCriterion("selftraveltag <", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagLessThanOrEqualTo(Integer value) {
            addCriterion("selftraveltag <=", value, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagIn(List<Integer> values) {
            addCriterion("selftraveltag in", values, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagNotIn(List<Integer> values) {
            addCriterion("selftraveltag not in", values, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagBetween(Integer value1, Integer value2) {
            addCriterion("selftraveltag between", value1, value2, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSelftraveltagNotBetween(Integer value1, Integer value2) {
            addCriterion("selftraveltag not between", value1, value2, "selftraveltag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagIsNull() {
            addCriterion("specialservertag is null");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagIsNotNull() {
            addCriterion("specialservertag is not null");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagEqualTo(Integer value) {
            addCriterion("specialservertag =", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagNotEqualTo(Integer value) {
            addCriterion("specialservertag <>", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagGreaterThan(Integer value) {
            addCriterion("specialservertag >", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagGreaterThanOrEqualTo(Integer value) {
            addCriterion("specialservertag >=", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagLessThan(Integer value) {
            addCriterion("specialservertag <", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagLessThanOrEqualTo(Integer value) {
            addCriterion("specialservertag <=", value, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagIn(List<Integer> values) {
            addCriterion("specialservertag in", values, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagNotIn(List<Integer> values) {
            addCriterion("specialservertag not in", values, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagBetween(Integer value1, Integer value2) {
            addCriterion("specialservertag between", value1, value2, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andSpecialservertagNotBetween(Integer value1, Integer value2) {
            addCriterion("specialservertag not between", value1, value2, "specialservertag");
            return (Criteria) this;
        }

        public Criteria andLzxtypeIsNull() {
            addCriterion("lzxtype is null");
            return (Criteria) this;
        }

        public Criteria andLzxtypeIsNotNull() {
            addCriterion("lzxtype is not null");
            return (Criteria) this;
        }

        public Criteria andLzxtypeEqualTo(Integer value) {
            addCriterion("lzxtype =", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeNotEqualTo(Integer value) {
            addCriterion("lzxtype <>", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeGreaterThan(Integer value) {
            addCriterion("lzxtype >", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("lzxtype >=", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeLessThan(Integer value) {
            addCriterion("lzxtype <", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeLessThanOrEqualTo(Integer value) {
            addCriterion("lzxtype <=", value, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeIn(List<Integer> values) {
            addCriterion("lzxtype in", values, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeNotIn(List<Integer> values) {
            addCriterion("lzxtype not in", values, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeBetween(Integer value1, Integer value2) {
            addCriterion("lzxtype between", value1, value2, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLzxtypeNotBetween(Integer value1, Integer value2) {
            addCriterion("lzxtype not between", value1, value2, "lzxtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeIsNull() {
            addCriterion("locationtype is null");
            return (Criteria) this;
        }

        public Criteria andLocationtypeIsNotNull() {
            addCriterion("locationtype is not null");
            return (Criteria) this;
        }

        public Criteria andLocationtypeEqualTo(String value) {
            addCriterion("locationtype =", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeNotEqualTo(String value) {
            addCriterion("locationtype <>", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeGreaterThan(String value) {
            addCriterion("locationtype >", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeGreaterThanOrEqualTo(String value) {
            addCriterion("locationtype >=", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeLessThan(String value) {
            addCriterion("locationtype <", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeLessThanOrEqualTo(String value) {
            addCriterion("locationtype <=", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeLike(String value) {
            addCriterion("locationtype like", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeNotLike(String value) {
            addCriterion("locationtype not like", value, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeIn(List<String> values) {
            addCriterion("locationtype in", values, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeNotIn(List<String> values) {
            addCriterion("locationtype not in", values, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeBetween(String value1, String value2) {
            addCriterion("locationtype between", value1, value2, "locationtype");
            return (Criteria) this;
        }

        public Criteria andLocationtypeNotBetween(String value1, String value2) {
            addCriterion("locationtype not between", value1, value2, "locationtype");
            return (Criteria) this;
        }

        public Criteria andSafetypeIsNull() {
            addCriterion("safetype is null");
            return (Criteria) this;
        }

        public Criteria andSafetypeIsNotNull() {
            addCriterion("safetype is not null");
            return (Criteria) this;
        }

        public Criteria andSafetypeEqualTo(String value) {
            addCriterion("safetype =", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotEqualTo(String value) {
            addCriterion("safetype <>", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeGreaterThan(String value) {
            addCriterion("safetype >", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeGreaterThanOrEqualTo(String value) {
            addCriterion("safetype >=", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLessThan(String value) {
            addCriterion("safetype <", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLessThanOrEqualTo(String value) {
            addCriterion("safetype <=", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeLike(String value) {
            addCriterion("safetype like", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotLike(String value) {
            addCriterion("safetype not like", value, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeIn(List<String> values) {
            addCriterion("safetype in", values, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotIn(List<String> values) {
            addCriterion("safetype not in", values, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeBetween(String value1, String value2) {
            addCriterion("safetype between", value1, value2, "safetype");
            return (Criteria) this;
        }

        public Criteria andSafetypeNotBetween(String value1, String value2) {
            addCriterion("safetype not between", value1, value2, "safetype");
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

        public Criteria andLzxcountIsNull() {
            addCriterion("lzxcount is null");
            return (Criteria) this;
        }

        public Criteria andLzxcountIsNotNull() {
            addCriterion("lzxcount is not null");
            return (Criteria) this;
        }

        public Criteria andLzxcountEqualTo(Integer value) {
            addCriterion("lzxcount =", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountNotEqualTo(Integer value) {
            addCriterion("lzxcount <>", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountGreaterThan(Integer value) {
            addCriterion("lzxcount >", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("lzxcount >=", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountLessThan(Integer value) {
            addCriterion("lzxcount <", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountLessThanOrEqualTo(Integer value) {
            addCriterion("lzxcount <=", value, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountIn(List<Integer> values) {
            addCriterion("lzxcount in", values, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountNotIn(List<Integer> values) {
            addCriterion("lzxcount not in", values, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountBetween(Integer value1, Integer value2) {
            addCriterion("lzxcount between", value1, value2, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andLzxcountNotBetween(Integer value1, Integer value2) {
            addCriterion("lzxcount not between", value1, value2, "lzxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountIsNull() {
            addCriterion("ywxcount is null");
            return (Criteria) this;
        }

        public Criteria andYwxcountIsNotNull() {
            addCriterion("ywxcount is not null");
            return (Criteria) this;
        }

        public Criteria andYwxcountEqualTo(Integer value) {
            addCriterion("ywxcount =", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountNotEqualTo(Integer value) {
            addCriterion("ywxcount <>", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountGreaterThan(Integer value) {
            addCriterion("ywxcount >", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ywxcount >=", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountLessThan(Integer value) {
            addCriterion("ywxcount <", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountLessThanOrEqualTo(Integer value) {
            addCriterion("ywxcount <=", value, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountIn(List<Integer> values) {
            addCriterion("ywxcount in", values, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountNotIn(List<Integer> values) {
            addCriterion("ywxcount not in", values, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountBetween(Integer value1, Integer value2) {
            addCriterion("ywxcount between", value1, value2, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxcountNotBetween(Integer value1, Integer value2) {
            addCriterion("ywxcount not between", value1, value2, "ywxcount");
            return (Criteria) this;
        }

        public Criteria andYwxperIsNull() {
            addCriterion("ywxper is null");
            return (Criteria) this;
        }

        public Criteria andYwxperIsNotNull() {
            addCriterion("ywxper is not null");
            return (Criteria) this;
        }

        public Criteria andYwxperEqualTo(BigDecimal value) {
            addCriterion("ywxper =", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperNotEqualTo(BigDecimal value) {
            addCriterion("ywxper <>", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperGreaterThan(BigDecimal value) {
            addCriterion("ywxper >", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ywxper >=", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperLessThan(BigDecimal value) {
            addCriterion("ywxper <", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ywxper <=", value, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperIn(List<BigDecimal> values) {
            addCriterion("ywxper in", values, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperNotIn(List<BigDecimal> values) {
            addCriterion("ywxper not in", values, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywxper between", value1, value2, "ywxper");
            return (Criteria) this;
        }

        public Criteria andYwxperNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ywxper not between", value1, value2, "ywxper");
            return (Criteria) this;
        }

        public Criteria andLzxperIsNull() {
            addCriterion("lzxper is null");
            return (Criteria) this;
        }

        public Criteria andLzxperIsNotNull() {
            addCriterion("lzxper is not null");
            return (Criteria) this;
        }

        public Criteria andLzxperEqualTo(BigDecimal value) {
            addCriterion("lzxper =", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperNotEqualTo(BigDecimal value) {
            addCriterion("lzxper <>", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperGreaterThan(BigDecimal value) {
            addCriterion("lzxper >", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lzxper >=", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperLessThan(BigDecimal value) {
            addCriterion("lzxper <", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lzxper <=", value, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperIn(List<BigDecimal> values) {
            addCriterion("lzxper in", values, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperNotIn(List<BigDecimal> values) {
            addCriterion("lzxper not in", values, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzxper between", value1, value2, "lzxper");
            return (Criteria) this;
        }

        public Criteria andLzxperNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lzxper not between", value1, value2, "lzxper");
            return (Criteria) this;
        }

        public Criteria andRouteIsNull() {
            addCriterion("route is null");
            return (Criteria) this;
        }

        public Criteria andRouteIsNotNull() {
            addCriterion("route is not null");
            return (Criteria) this;
        }

        public Criteria andRouteEqualTo(String value) {
            addCriterion("route =", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteNotEqualTo(String value) {
            addCriterion("route <>", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteGreaterThan(String value) {
            addCriterion("route >", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteGreaterThanOrEqualTo(String value) {
            addCriterion("route >=", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteLessThan(String value) {
            addCriterion("route <", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteLessThanOrEqualTo(String value) {
            addCriterion("route <=", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteLike(String value) {
            addCriterion("route like", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteNotLike(String value) {
            addCriterion("route not like", value, "route");
            return (Criteria) this;
        }

        public Criteria andRouteIn(List<String> values) {
            addCriterion("route in", values, "route");
            return (Criteria) this;
        }

        public Criteria andRouteNotIn(List<String> values) {
            addCriterion("route not in", values, "route");
            return (Criteria) this;
        }

        public Criteria andRouteBetween(String value1, String value2) {
            addCriterion("route between", value1, value2, "route");
            return (Criteria) this;
        }

        public Criteria andRouteNotBetween(String value1, String value2) {
            addCriterion("route not between", value1, value2, "route");
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

        public Criteria andOrderidEqualTo(String value) {
            addCriterion("orderid =", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotEqualTo(String value) {
            addCriterion("orderid <>", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThan(String value) {
            addCriterion("orderid >", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThanOrEqualTo(String value) {
            addCriterion("orderid >=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThan(String value) {
            addCriterion("orderid <", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThanOrEqualTo(String value) {
            addCriterion("orderid <=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLike(String value) {
            addCriterion("orderid like", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotLike(String value) {
            addCriterion("orderid not like", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidIn(List<String> values) {
            addCriterion("orderid in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotIn(List<String> values) {
            addCriterion("orderid not in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidBetween(String value1, String value2) {
            addCriterion("orderid between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotBetween(String value1, String value2) {
            addCriterion("orderid not between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andIsownIsNull() {
            addCriterion("isown is null");
            return (Criteria) this;
        }

        public Criteria andIsownIsNotNull() {
            addCriterion("isown is not null");
            return (Criteria) this;
        }

        public Criteria andIsownEqualTo(Integer value) {
            addCriterion("isown =", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownNotEqualTo(Integer value) {
            addCriterion("isown <>", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownGreaterThan(Integer value) {
            addCriterion("isown >", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownGreaterThanOrEqualTo(Integer value) {
            addCriterion("isown >=", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownLessThan(Integer value) {
            addCriterion("isown <", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownLessThanOrEqualTo(Integer value) {
            addCriterion("isown <=", value, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownIn(List<Integer> values) {
            addCriterion("isown in", values, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownNotIn(List<Integer> values) {
            addCriterion("isown not in", values, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownBetween(Integer value1, Integer value2) {
            addCriterion("isown between", value1, value2, "isown");
            return (Criteria) this;
        }

        public Criteria andIsownNotBetween(Integer value1, Integer value2) {
            addCriterion("isown not between", value1, value2, "isown");
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

        public Criteria andCardIsNull() {
            addCriterion("card is null");
            return (Criteria) this;
        }

        public Criteria andCardIsNotNull() {
            addCriterion("card is not null");
            return (Criteria) this;
        }

        public Criteria andCardEqualTo(String value) {
            addCriterion("card =", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotEqualTo(String value) {
            addCriterion("card <>", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThan(String value) {
            addCriterion("card >", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThanOrEqualTo(String value) {
            addCriterion("card >=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThan(String value) {
            addCriterion("card <", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThanOrEqualTo(String value) {
            addCriterion("card <=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLike(String value) {
            addCriterion("card like", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotLike(String value) {
            addCriterion("card not like", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardIn(List<String> values) {
            addCriterion("card in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotIn(List<String> values) {
            addCriterion("card not in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardBetween(String value1, String value2) {
            addCriterion("card between", value1, value2, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotBetween(String value1, String value2) {
            addCriterion("card not between", value1, value2, "card");
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

        public Criteria andDailiusernameIsNull() {
            addCriterion("dailiusername is null");
            return (Criteria) this;
        }

        public Criteria andDailiusernameIsNotNull() {
            addCriterion("dailiusername is not null");
            return (Criteria) this;
        }

        public Criteria andDailiusernameEqualTo(String value) {
            addCriterion("dailiusername =", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameNotEqualTo(String value) {
            addCriterion("dailiusername <>", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameGreaterThan(String value) {
            addCriterion("dailiusername >", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameGreaterThanOrEqualTo(String value) {
            addCriterion("dailiusername >=", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameLessThan(String value) {
            addCriterion("dailiusername <", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameLessThanOrEqualTo(String value) {
            addCriterion("dailiusername <=", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameLike(String value) {
            addCriterion("dailiusername like", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameNotLike(String value) {
            addCriterion("dailiusername not like", value, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameIn(List<String> values) {
            addCriterion("dailiusername in", values, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameNotIn(List<String> values) {
            addCriterion("dailiusername not in", values, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameBetween(String value1, String value2) {
            addCriterion("dailiusername between", value1, value2, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailiusernameNotBetween(String value1, String value2) {
            addCriterion("dailiusername not between", value1, value2, "dailiusername");
            return (Criteria) this;
        }

        public Criteria andDailicardIsNull() {
            addCriterion("dailicard is null");
            return (Criteria) this;
        }

        public Criteria andDailicardIsNotNull() {
            addCriterion("dailicard is not null");
            return (Criteria) this;
        }

        public Criteria andDailicardEqualTo(String value) {
            addCriterion("dailicard =", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardNotEqualTo(String value) {
            addCriterion("dailicard <>", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardGreaterThan(String value) {
            addCriterion("dailicard >", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardGreaterThanOrEqualTo(String value) {
            addCriterion("dailicard >=", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardLessThan(String value) {
            addCriterion("dailicard <", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardLessThanOrEqualTo(String value) {
            addCriterion("dailicard <=", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardLike(String value) {
            addCriterion("dailicard like", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardNotLike(String value) {
            addCriterion("dailicard not like", value, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardIn(List<String> values) {
            addCriterion("dailicard in", values, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardNotIn(List<String> values) {
            addCriterion("dailicard not in", values, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardBetween(String value1, String value2) {
            addCriterion("dailicard between", value1, value2, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailicardNotBetween(String value1, String value2) {
            addCriterion("dailicard not between", value1, value2, "dailicard");
            return (Criteria) this;
        }

        public Criteria andDailiphoneIsNull() {
            addCriterion("dailiphone is null");
            return (Criteria) this;
        }

        public Criteria andDailiphoneIsNotNull() {
            addCriterion("dailiphone is not null");
            return (Criteria) this;
        }

        public Criteria andDailiphoneEqualTo(String value) {
            addCriterion("dailiphone =", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneNotEqualTo(String value) {
            addCriterion("dailiphone <>", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneGreaterThan(String value) {
            addCriterion("dailiphone >", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneGreaterThanOrEqualTo(String value) {
            addCriterion("dailiphone >=", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneLessThan(String value) {
            addCriterion("dailiphone <", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneLessThanOrEqualTo(String value) {
            addCriterion("dailiphone <=", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneLike(String value) {
            addCriterion("dailiphone like", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneNotLike(String value) {
            addCriterion("dailiphone not like", value, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneIn(List<String> values) {
            addCriterion("dailiphone in", values, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneNotIn(List<String> values) {
            addCriterion("dailiphone not in", values, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneBetween(String value1, String value2) {
            addCriterion("dailiphone between", value1, value2, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andDailiphoneNotBetween(String value1, String value2) {
            addCriterion("dailiphone not between", value1, value2, "dailiphone");
            return (Criteria) this;
        }

        public Criteria andRequireidIsNull() {
            addCriterion("requireid is null");
            return (Criteria) this;
        }

        public Criteria andRequireidIsNotNull() {
            addCriterion("requireid is not null");
            return (Criteria) this;
        }

        public Criteria andRequireidEqualTo(String value) {
            addCriterion("requireid =", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidNotEqualTo(String value) {
            addCriterion("requireid <>", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidGreaterThan(String value) {
            addCriterion("requireid >", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidGreaterThanOrEqualTo(String value) {
            addCriterion("requireid >=", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidLessThan(String value) {
            addCriterion("requireid <", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidLessThanOrEqualTo(String value) {
            addCriterion("requireid <=", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidLike(String value) {
            addCriterion("requireid like", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidNotLike(String value) {
            addCriterion("requireid not like", value, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidIn(List<String> values) {
            addCriterion("requireid in", values, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidNotIn(List<String> values) {
            addCriterion("requireid not in", values, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidBetween(String value1, String value2) {
            addCriterion("requireid between", value1, value2, "requireid");
            return (Criteria) this;
        }

        public Criteria andRequireidNotBetween(String value1, String value2) {
            addCriterion("requireid not between", value1, value2, "requireid");
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

        public Criteria andSourceplatformIsNull() {
            addCriterion("sourceplatform is null");
            return (Criteria) this;
        }

        public Criteria andSourceplatformIsNotNull() {
            addCriterion("sourceplatform is not null");
            return (Criteria) this;
        }

        public Criteria andSourceplatformEqualTo(String value) {
            addCriterion("sourceplatform =", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformNotEqualTo(String value) {
            addCriterion("sourceplatform <>", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformGreaterThan(String value) {
            addCriterion("sourceplatform >", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformGreaterThanOrEqualTo(String value) {
            addCriterion("sourceplatform >=", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformLessThan(String value) {
            addCriterion("sourceplatform <", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformLessThanOrEqualTo(String value) {
            addCriterion("sourceplatform <=", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformLike(String value) {
            addCriterion("sourceplatform like", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformNotLike(String value) {
            addCriterion("sourceplatform not like", value, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformIn(List<String> values) {
            addCriterion("sourceplatform in", values, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformNotIn(List<String> values) {
            addCriterion("sourceplatform not in", values, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformBetween(String value1, String value2) {
            addCriterion("sourceplatform between", value1, value2, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andSourceplatformNotBetween(String value1, String value2) {
            addCriterion("sourceplatform not between", value1, value2, "sourceplatform");
            return (Criteria) this;
        }

        public Criteria andOrdertypeIsNull() {
            addCriterion("ordertype is null");
            return (Criteria) this;
        }

        public Criteria andOrdertypeIsNotNull() {
            addCriterion("ordertype is not null");
            return (Criteria) this;
        }

        public Criteria andOrdertypeEqualTo(String value) {
            addCriterion("ordertype =", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeNotEqualTo(String value) {
            addCriterion("ordertype <>", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeGreaterThan(String value) {
            addCriterion("ordertype >", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeGreaterThanOrEqualTo(String value) {
            addCriterion("ordertype >=", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeLessThan(String value) {
            addCriterion("ordertype <", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeLessThanOrEqualTo(String value) {
            addCriterion("ordertype <=", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeLike(String value) {
            addCriterion("ordertype like", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeNotLike(String value) {
            addCriterion("ordertype not like", value, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeIn(List<String> values) {
            addCriterion("ordertype in", values, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeNotIn(List<String> values) {
            addCriterion("ordertype not in", values, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeBetween(String value1, String value2) {
            addCriterion("ordertype between", value1, value2, "ordertype");
            return (Criteria) this;
        }

        public Criteria andOrdertypeNotBetween(String value1, String value2) {
            addCriterion("ordertype not between", value1, value2, "ordertype");
            return (Criteria) this;
        }

        public Criteria andSourcenumberIsNull() {
            addCriterion("sourcenumber is null");
            return (Criteria) this;
        }

        public Criteria andSourcenumberIsNotNull() {
            addCriterion("sourcenumber is not null");
            return (Criteria) this;
        }

        public Criteria andSourcenumberEqualTo(String value) {
            addCriterion("sourcenumber =", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberNotEqualTo(String value) {
            addCriterion("sourcenumber <>", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberGreaterThan(String value) {
            addCriterion("sourcenumber >", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberGreaterThanOrEqualTo(String value) {
            addCriterion("sourcenumber >=", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberLessThan(String value) {
            addCriterion("sourcenumber <", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberLessThanOrEqualTo(String value) {
            addCriterion("sourcenumber <=", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberLike(String value) {
            addCriterion("sourcenumber like", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberNotLike(String value) {
            addCriterion("sourcenumber not like", value, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberIn(List<String> values) {
            addCriterion("sourcenumber in", values, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberNotIn(List<String> values) {
            addCriterion("sourcenumber not in", values, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberBetween(String value1, String value2) {
            addCriterion("sourcenumber between", value1, value2, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andSourcenumberNotBetween(String value1, String value2) {
            addCriterion("sourcenumber not between", value1, value2, "sourcenumber");
            return (Criteria) this;
        }

        public Criteria andCompanytypeIsNull() {
            addCriterion("companytype is null");
            return (Criteria) this;
        }

        public Criteria andCompanytypeIsNotNull() {
            addCriterion("companytype is not null");
            return (Criteria) this;
        }

        public Criteria andCompanytypeEqualTo(String value) {
            addCriterion("companytype =", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeNotEqualTo(String value) {
            addCriterion("companytype <>", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeGreaterThan(String value) {
            addCriterion("companytype >", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeGreaterThanOrEqualTo(String value) {
            addCriterion("companytype >=", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeLessThan(String value) {
            addCriterion("companytype <", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeLessThanOrEqualTo(String value) {
            addCriterion("companytype <=", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeLike(String value) {
            addCriterion("companytype like", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeNotLike(String value) {
            addCriterion("companytype not like", value, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeIn(List<String> values) {
            addCriterion("companytype in", values, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeNotIn(List<String> values) {
            addCriterion("companytype not in", values, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeBetween(String value1, String value2) {
            addCriterion("companytype between", value1, value2, "companytype");
            return (Criteria) this;
        }

        public Criteria andCompanytypeNotBetween(String value1, String value2) {
            addCriterion("companytype not between", value1, value2, "companytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeIsNull() {
            addCriterion("policytype is null");
            return (Criteria) this;
        }

        public Criteria andPolicytypeIsNotNull() {
            addCriterion("policytype is not null");
            return (Criteria) this;
        }

        public Criteria andPolicytypeEqualTo(String value) {
            addCriterion("policytype =", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeNotEqualTo(String value) {
            addCriterion("policytype <>", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeGreaterThan(String value) {
            addCriterion("policytype >", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeGreaterThanOrEqualTo(String value) {
            addCriterion("policytype >=", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeLessThan(String value) {
            addCriterion("policytype <", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeLessThanOrEqualTo(String value) {
            addCriterion("policytype <=", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeLike(String value) {
            addCriterion("policytype like", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeNotLike(String value) {
            addCriterion("policytype not like", value, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeIn(List<String> values) {
            addCriterion("policytype in", values, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeNotIn(List<String> values) {
            addCriterion("policytype not in", values, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeBetween(String value1, String value2) {
            addCriterion("policytype between", value1, value2, "policytype");
            return (Criteria) this;
        }

        public Criteria andPolicytypeNotBetween(String value1, String value2) {
            addCriterion("policytype not between", value1, value2, "policytype");
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