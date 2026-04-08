package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fund1Gaoduanzhuangbei2OkExample {
	protected String orderByClause;

	protected String tableName;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public Fund1Gaoduanzhuangbei2OkExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
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

		public Criteria andCodeIsNull() {
			addCriterion("code is null");
			return (Criteria) this;
		}

		public Criteria andCodeIsNotNull() {
			addCriterion("code is not null");
			return (Criteria) this;
		}

		public Criteria andCodeEqualTo(String value) {
			addCriterion("code =", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotEqualTo(String value) {
			addCriterion("code <>", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeGreaterThan(String value) {
			addCriterion("code >", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeGreaterThanOrEqualTo(String value) {
			addCriterion("code >=", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLessThan(String value) {
			addCriterion("code <", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLessThanOrEqualTo(String value) {
			addCriterion("code <=", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeLike(String value) {
			addCriterion("code like", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotLike(String value) {
			addCriterion("code not like", value, "code");
			return (Criteria) this;
		}

		public Criteria andCodeIn(List<String> values) {
			addCriterion("code in", values, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotIn(List<String> values) {
			addCriterion("code not in", values, "code");
			return (Criteria) this;
		}

		public Criteria andCodeBetween(String value1, String value2) {
			addCriterion("code between", value1, value2, "code");
			return (Criteria) this;
		}

		public Criteria andCodeNotBetween(String value1, String value2) {
			addCriterion("code not between", value1, value2, "code");
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

		public Criteria andCurrentpriceIsNull() {
			addCriterion("currentprice is null");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceIsNotNull() {
			addCriterion("currentprice is not null");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceEqualTo(BigDecimal value) {
			addCriterion("currentprice =", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceNotEqualTo(BigDecimal value) {
			addCriterion("currentprice <>", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceGreaterThan(BigDecimal value) {
			addCriterion("currentprice >", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("currentprice >=", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceLessThan(BigDecimal value) {
			addCriterion("currentprice <", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("currentprice <=", value, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceIn(List<BigDecimal> values) {
			addCriterion("currentprice in", values, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceNotIn(List<BigDecimal> values) {
			addCriterion("currentprice not in", values, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("currentprice between", value1, value2, "currentprice");
			return (Criteria) this;
		}

		public Criteria andCurrentpriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("currentprice not between", value1, value2, "currentprice");
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

		public Criteria andLevelEqualTo(Integer value) {
			addCriterion("level =", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelNotEqualTo(Integer value) {
			addCriterion("level <>", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelGreaterThan(Integer value) {
			addCriterion("level >", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
			addCriterion("level >=", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelLessThan(Integer value) {
			addCriterion("level <", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelLessThanOrEqualTo(Integer value) {
			addCriterion("level <=", value, "level");
			return (Criteria) this;
		}

		public Criteria andLevelIn(List<Integer> values) {
			addCriterion("level in", values, "level");
			return (Criteria) this;
		}

		public Criteria andLevelNotIn(List<Integer> values) {
			addCriterion("level not in", values, "level");
			return (Criteria) this;
		}

		public Criteria andLevelBetween(Integer value1, Integer value2) {
			addCriterion("level between", value1, value2, "level");
			return (Criteria) this;
		}

		public Criteria andLevelNotBetween(Integer value1, Integer value2) {
			addCriterion("level not between", value1, value2, "level");
			return (Criteria) this;
		}

		public Criteria andIscurrentIsNull() {
			addCriterion("iscurrent is null");
			return (Criteria) this;
		}

		public Criteria andIscurrentIsNotNull() {
			addCriterion("iscurrent is not null");
			return (Criteria) this;
		}

		public Criteria andIscurrentEqualTo(Integer value) {
			addCriterion("iscurrent =", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andTableNameEqualTo(String value) {
			addCriterion("tablename =", value, "tablename");
			return (Criteria) this;
		}

		public Criteria andIscurrentNotEqualTo(Integer value) {
			addCriterion("iscurrent <>", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentGreaterThan(Integer value) {
			addCriterion("iscurrent >", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentGreaterThanOrEqualTo(Integer value) {
			addCriterion("iscurrent >=", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentLessThan(Integer value) {
			addCriterion("iscurrent <", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentLessThanOrEqualTo(Integer value) {
			addCriterion("iscurrent <=", value, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentIn(List<Integer> values) {
			addCriterion("iscurrent in", values, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentNotIn(List<Integer> values) {
			addCriterion("iscurrent not in", values, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentBetween(Integer value1, Integer value2) {
			addCriterion("iscurrent between", value1, value2, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andIscurrentNotBetween(Integer value1, Integer value2) {
			addCriterion("iscurrent not between", value1, value2, "iscurrent");
			return (Criteria) this;
		}

		public Criteria andBuypriceIsNull() {
			addCriterion("buyprice is null");
			return (Criteria) this;
		}

		public Criteria andBuypriceIsNotNull() {
			addCriterion("buyprice is not null");
			return (Criteria) this;
		}

		public Criteria andBuypriceEqualTo(BigDecimal value) {
			addCriterion("buyprice =", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceNotEqualTo(BigDecimal value) {
			addCriterion("buyprice <>", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceGreaterThan(BigDecimal value) {
			addCriterion("buyprice >", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("buyprice >=", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceLessThan(BigDecimal value) {
			addCriterion("buyprice <", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("buyprice <=", value, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceIn(List<BigDecimal> values) {
			addCriterion("buyprice in", values, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceNotIn(List<BigDecimal> values) {
			addCriterion("buyprice not in", values, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buyprice between", value1, value2, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buyprice not between", value1, value2, "buyprice");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealIsNull() {
			addCriterion("buyprice_real is null");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealIsNotNull() {
			addCriterion("buyprice_real is not null");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealEqualTo(BigDecimal value) {
			addCriterion("buyprice_real =", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealNotEqualTo(BigDecimal value) {
			addCriterion("buyprice_real <>", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealGreaterThan(BigDecimal value) {
			addCriterion("buyprice_real >", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("buyprice_real >=", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealLessThan(BigDecimal value) {
			addCriterion("buyprice_real <", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealLessThanOrEqualTo(BigDecimal value) {
			addCriterion("buyprice_real <=", value, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealIn(List<BigDecimal> values) {
			addCriterion("buyprice_real in", values, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealNotIn(List<BigDecimal> values) {
			addCriterion("buyprice_real not in", values, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buyprice_real between", value1, value2, "buypriceReal");
			return (Criteria) this;
		}

		public Criteria andBuypriceRealNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buyprice_real not between", value1, value2, "buypriceReal");
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

		public Criteria andSellpriceIsNull() {
			addCriterion("sellprice is null");
			return (Criteria) this;
		}

		public Criteria andSellpriceIsNotNull() {
			addCriterion("sellprice is not null");
			return (Criteria) this;
		}

		public Criteria andSellpriceEqualTo(BigDecimal value) {
			addCriterion("sellprice =", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceNotEqualTo(BigDecimal value) {
			addCriterion("sellprice <>", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceGreaterThan(BigDecimal value) {
			addCriterion("sellprice >", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("sellprice >=", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceLessThan(BigDecimal value) {
			addCriterion("sellprice <", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("sellprice <=", value, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceIn(List<BigDecimal> values) {
			addCriterion("sellprice in", values, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceNotIn(List<BigDecimal> values) {
			addCriterion("sellprice not in", values, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("sellprice between", value1, value2, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("sellprice not between", value1, value2, "sellprice");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealIsNull() {
			addCriterion("sellprice_real is null");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealIsNotNull() {
			addCriterion("sellprice_real is not null");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealEqualTo(BigDecimal value) {
			addCriterion("sellprice_real =", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealNotEqualTo(BigDecimal value) {
			addCriterion("sellprice_real <>", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealGreaterThan(BigDecimal value) {
			addCriterion("sellprice_real >", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("sellprice_real >=", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealLessThan(BigDecimal value) {
			addCriterion("sellprice_real <", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealLessThanOrEqualTo(BigDecimal value) {
			addCriterion("sellprice_real <=", value, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealIn(List<BigDecimal> values) {
			addCriterion("sellprice_real in", values, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealNotIn(List<BigDecimal> values) {
			addCriterion("sellprice_real not in", values, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("sellprice_real between", value1, value2, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andSellpriceRealNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("sellprice_real not between", value1, value2, "sellpriceReal");
			return (Criteria) this;
		}

		public Criteria andDtSellIsNull() {
			addCriterion("dt_sell is null");
			return (Criteria) this;
		}

		public Criteria andDtSellIsNotNull() {
			addCriterion("dt_sell is not null");
			return (Criteria) this;
		}

		public Criteria andDtSellEqualTo(Date value) {
			addCriterion("dt_sell =", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellNotEqualTo(Date value) {
			addCriterion("dt_sell <>", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellGreaterThan(Date value) {
			addCriterion("dt_sell >", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellGreaterThanOrEqualTo(Date value) {
			addCriterion("dt_sell >=", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellLessThan(Date value) {
			addCriterion("dt_sell <", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellLessThanOrEqualTo(Date value) {
			addCriterion("dt_sell <=", value, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellIn(List<Date> values) {
			addCriterion("dt_sell in", values, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellNotIn(List<Date> values) {
			addCriterion("dt_sell not in", values, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellBetween(Date value1, Date value2) {
			addCriterion("dt_sell between", value1, value2, "dtSell");
			return (Criteria) this;
		}

		public Criteria andDtSellNotBetween(Date value1, Date value2) {
			addCriterion("dt_sell not between", value1, value2, "dtSell");
			return (Criteria) this;
		}

		public Criteria andWanggeIsNull() {
			addCriterion("wangge is null");
			return (Criteria) this;
		}

		public Criteria andWanggeIsNotNull() {
			addCriterion("wangge is not null");
			return (Criteria) this;
		}

		public Criteria andWanggeEqualTo(BigDecimal value) {
			addCriterion("wangge =", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeNotEqualTo(BigDecimal value) {
			addCriterion("wangge <>", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeGreaterThan(BigDecimal value) {
			addCriterion("wangge >", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("wangge >=", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeLessThan(BigDecimal value) {
			addCriterion("wangge <", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeLessThanOrEqualTo(BigDecimal value) {
			addCriterion("wangge <=", value, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeIn(List<BigDecimal> values) {
			addCriterion("wangge in", values, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeNotIn(List<BigDecimal> values) {
			addCriterion("wangge not in", values, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wangge between", value1, value2, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggeNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wangge not between", value1, value2, "wangge");
			return (Criteria) this;
		}

		public Criteria andWanggepriceIsNull() {
			addCriterion("wanggeprice is null");
			return (Criteria) this;
		}

		public Criteria andWanggepriceIsNotNull() {
			addCriterion("wanggeprice is not null");
			return (Criteria) this;
		}

		public Criteria andWanggepriceEqualTo(BigDecimal value) {
			addCriterion("wanggeprice =", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceNotEqualTo(BigDecimal value) {
			addCriterion("wanggeprice <>", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceGreaterThan(BigDecimal value) {
			addCriterion("wanggeprice >", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("wanggeprice >=", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceLessThan(BigDecimal value) {
			addCriterion("wanggeprice <", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("wanggeprice <=", value, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceIn(List<BigDecimal> values) {
			addCriterion("wanggeprice in", values, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceNotIn(List<BigDecimal> values) {
			addCriterion("wanggeprice not in", values, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wanggeprice between", value1, value2, "wanggeprice");
			return (Criteria) this;
		}

		public Criteria andWanggepriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wanggeprice not between", value1, value2, "wanggeprice");
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

		public Criteria andMinpriceIsNull() {
			addCriterion("minprice is null");
			return (Criteria) this;
		}

		public Criteria andMinpriceIsNotNull() {
			addCriterion("minprice is not null");
			return (Criteria) this;
		}

		public Criteria andMinpriceEqualTo(BigDecimal value) {
			addCriterion("minprice =", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceNotEqualTo(BigDecimal value) {
			addCriterion("minprice <>", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceGreaterThan(BigDecimal value) {
			addCriterion("minprice >", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("minprice >=", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceLessThan(BigDecimal value) {
			addCriterion("minprice <", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("minprice <=", value, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceIn(List<BigDecimal> values) {
			addCriterion("minprice in", values, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceNotIn(List<BigDecimal> values) {
			addCriterion("minprice not in", values, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("minprice between", value1, value2, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("minprice not between", value1, value2, "minprice");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentIsNull() {
			addCriterion("minpricepercent is null");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentIsNotNull() {
			addCriterion("minpricepercent is not null");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentEqualTo(BigDecimal value) {
			addCriterion("minpricepercent =", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentNotEqualTo(BigDecimal value) {
			addCriterion("minpricepercent <>", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentGreaterThan(BigDecimal value) {
			addCriterion("minpricepercent >", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("minpricepercent >=", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentLessThan(BigDecimal value) {
			addCriterion("minpricepercent <", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentLessThanOrEqualTo(BigDecimal value) {
			addCriterion("minpricepercent <=", value, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentIn(List<BigDecimal> values) {
			addCriterion("minpricepercent in", values, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentNotIn(List<BigDecimal> values) {
			addCriterion("minpricepercent not in", values, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("minpricepercent between", value1, value2, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andMinpricepercentNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("minpricepercent not between", value1, value2, "minpricepercent");
			return (Criteria) this;
		}

		public Criteria andFeneIsNull() {
			addCriterion("fene is null");
			return (Criteria) this;
		}

		public Criteria andFeneIsNotNull() {
			addCriterion("fene is not null");
			return (Criteria) this;
		}

		public Criteria andFeneEqualTo(BigDecimal value) {
			addCriterion("fene =", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneNotEqualTo(BigDecimal value) {
			addCriterion("fene <>", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneGreaterThan(BigDecimal value) {
			addCriterion("fene >", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("fene >=", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneLessThan(BigDecimal value) {
			addCriterion("fene <", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneLessThanOrEqualTo(BigDecimal value) {
			addCriterion("fene <=", value, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneIn(List<BigDecimal> values) {
			addCriterion("fene in", values, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneNotIn(List<BigDecimal> values) {
			addCriterion("fene not in", values, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("fene between", value1, value2, "fene");
			return (Criteria) this;
		}

		public Criteria andFeneNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("fene not between", value1, value2, "fene");
			return (Criteria) this;
		}

		public Criteria andMoneyIsNull() {
			addCriterion("money is null");
			return (Criteria) this;
		}

		public Criteria andMoneyIsNotNull() {
			addCriterion("money is not null");
			return (Criteria) this;
		}

		public Criteria andMoneyEqualTo(BigDecimal value) {
			addCriterion("money =", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyNotEqualTo(BigDecimal value) {
			addCriterion("money <>", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyGreaterThan(BigDecimal value) {
			addCriterion("money >", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("money >=", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyLessThan(BigDecimal value) {
			addCriterion("money <", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyLessThanOrEqualTo(BigDecimal value) {
			addCriterion("money <=", value, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyIn(List<BigDecimal> values) {
			addCriterion("money in", values, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyNotIn(List<BigDecimal> values) {
			addCriterion("money not in", values, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("money between", value1, value2, "money");
			return (Criteria) this;
		}

		public Criteria andMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("money not between", value1, value2, "money");
			return (Criteria) this;
		}

		public Criteria andSuccesscountIsNull() {
			addCriterion("successcount is null");
			return (Criteria) this;
		}

		public Criteria andSuccesscountIsNotNull() {
			addCriterion("successcount is not null");
			return (Criteria) this;
		}

		public Criteria andSuccesscountEqualTo(Integer value) {
			addCriterion("successcount =", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountNotEqualTo(Integer value) {
			addCriterion("successcount <>", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountGreaterThan(Integer value) {
			addCriterion("successcount >", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountGreaterThanOrEqualTo(Integer value) {
			addCriterion("successcount >=", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountLessThan(Integer value) {
			addCriterion("successcount <", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountLessThanOrEqualTo(Integer value) {
			addCriterion("successcount <=", value, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountIn(List<Integer> values) {
			addCriterion("successcount in", values, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountNotIn(List<Integer> values) {
			addCriterion("successcount not in", values, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountBetween(Integer value1, Integer value2) {
			addCriterion("successcount between", value1, value2, "successcount");
			return (Criteria) this;
		}

		public Criteria andSuccesscountNotBetween(Integer value1, Integer value2) {
			addCriterion("successcount not between", value1, value2, "successcount");
			return (Criteria) this;
		}

		public Criteria andCateIsNull() {
			addCriterion("cate is null");
			return (Criteria) this;
		}

		public Criteria andCateIsNotNull() {
			addCriterion("cate is not null");
			return (Criteria) this;
		}

		public Criteria andCateEqualTo(String value) {
			addCriterion("cate =", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateNotEqualTo(String value) {
			addCriterion("cate <>", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateGreaterThan(String value) {
			addCriterion("cate >", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateGreaterThanOrEqualTo(String value) {
			addCriterion("cate >=", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateLessThan(String value) {
			addCriterion("cate <", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateLessThanOrEqualTo(String value) {
			addCriterion("cate <=", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateLike(String value) {
			addCriterion("cate like", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateNotLike(String value) {
			addCriterion("cate not like", value, "cate");
			return (Criteria) this;
		}

		public Criteria andCateIn(List<String> values) {
			addCriterion("cate in", values, "cate");
			return (Criteria) this;
		}

		public Criteria andCateNotIn(List<String> values) {
			addCriterion("cate not in", values, "cate");
			return (Criteria) this;
		}

		public Criteria andCateBetween(String value1, String value2) {
			addCriterion("cate between", value1, value2, "cate");
			return (Criteria) this;
		}

		public Criteria andCateNotBetween(String value1, String value2) {
			addCriterion("cate not between", value1, value2, "cate");
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