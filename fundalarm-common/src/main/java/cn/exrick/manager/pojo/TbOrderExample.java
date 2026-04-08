package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbOrderExample() {
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

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("order_id like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("order_id not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
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
        public Criteria andStatusmodequalto(Integer value) {
            addCriterion("status%2=", value, "status");
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
        public Criteria andhavesendIsNull() {
            addCriterion("havesendmessage is null");
            return (Criteria) this;
        }
        public Criteria andhavensendmessageEqualTo(Integer value) {
            addCriterion("havesendmessage =", value, "havesendmessage");
            return (Criteria) this;
        }
        
        
        public Criteria andpolicyidEqualTo(Integer value) {
            addCriterion("policyid =", value, "policyid");
            return (Criteria) this;
        }
        
        
        public Criteria andneddtosyncEqualTo(Integer value) {
            addCriterion("needsynctag =", value, "needsynctag");
            return (Criteria) this;
        }
        public Criteria andhavensendtoyoubaoEqualTo(Integer value) {
            addCriterion("havesendtoyoubao =", value, "havesendmessage");
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