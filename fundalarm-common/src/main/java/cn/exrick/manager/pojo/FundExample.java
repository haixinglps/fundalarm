package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FundExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public FundExample() {
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

		public Criteria andIschangwaiIsNull() {
			addCriterion("ischangwai is null");
			return (Criteria) this;
		}

		public Criteria andIschangwaiIsNotNull() {
			addCriterion("ischangwai is not null");
			return (Criteria) this;
		}

		public Criteria andIschangwaiEqualTo(Integer value) {
			addCriterion("ischangwai =", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiNotEqualTo(Integer value) {
			addCriterion("ischangwai <>", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiGreaterThan(Integer value) {
			addCriterion("ischangwai >", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiGreaterThanOrEqualTo(Integer value) {
			addCriterion("ischangwai >=", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiLessThan(Integer value) {
			addCriterion("ischangwai <", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiLessThanOrEqualTo(Integer value) {
			addCriterion("ischangwai <=", value, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiIn(List<Integer> values) {
			addCriterion("ischangwai in", values, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiNotIn(List<Integer> values) {
			addCriterion("ischangwai not in", values, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiBetween(Integer value1, Integer value2) {
			addCriterion("ischangwai between", value1, value2, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andIschangwaiNotBetween(Integer value1, Integer value2) {
			addCriterion("ischangwai not between", value1, value2, "ischangwai");
			return (Criteria) this;
		}

		public Criteria andBodongshuIsNull() {
			addCriterion("bodongshu is null");
			return (Criteria) this;
		}

		public Criteria andBodongshuIsNotNull() {
			addCriterion("bodongshu is not null");
			return (Criteria) this;
		}

		public Criteria andBodongshuEqualTo(Integer value) {
			addCriterion("bodongshu =", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andPrepareEqualTo(Integer value) {
			addCriterion("preparetag =", value, "preparetag");
			return (Criteria) this;
		}

		public Criteria andBodongshuNotEqualTo(Integer value) {
			addCriterion("bodongshu <>", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuGreaterThan(Integer value) {
			addCriterion("bodongshu >", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuGreaterThanOrEqualTo(Integer value) {
			addCriterion("bodongshu >=", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuLessThan(Integer value) {
			addCriterion("bodongshu <", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuLessThanOrEqualTo(Integer value) {
			addCriterion("bodongshu <=", value, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuIn(List<Integer> values) {
			addCriterion("bodongshu in", values, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuNotIn(List<Integer> values) {
			addCriterion("bodongshu not in", values, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuBetween(Integer value1, Integer value2) {
			addCriterion("bodongshu between", value1, value2, "bodongshu");
			return (Criteria) this;
		}

		public Criteria andBodongshuNotBetween(Integer value1, Integer value2) {
			addCriterion("bodongshu not between", value1, value2, "bodongshu");
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

		public Criteria andWenduIsNull() {
			addCriterion("wendu is null");
			return (Criteria) this;
		}

		public Criteria andWenduIsNotNull() {
			addCriterion("wendu is not null");
			return (Criteria) this;
		}

		public Criteria andWenduEqualTo(BigDecimal value) {
			addCriterion("wendu =", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduNotEqualTo(BigDecimal value) {
			addCriterion("wendu <>", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduGreaterThan(BigDecimal value) {
			addCriterion("wendu >", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("wendu >=", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduLessThan(BigDecimal value) {
			addCriterion("wendu <", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduLessThanOrEqualTo(BigDecimal value) {
			addCriterion("wendu <=", value, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduIn(List<BigDecimal> values) {
			addCriterion("wendu in", values, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduNotIn(List<BigDecimal> values) {
			addCriterion("wendu not in", values, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wendu between", value1, value2, "wendu");
			return (Criteria) this;
		}

		public Criteria andWenduNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("wendu not between", value1, value2, "wendu");
			return (Criteria) this;
		}

		public Criteria andPlantableIsNull() {
			addCriterion("plantable is null");
			return (Criteria) this;
		}

		public Criteria andPlantableIsNotNull() {
			addCriterion("plantable is not null");
			return (Criteria) this;
		}

		public Criteria andPlantableEqualTo(String value) {
			addCriterion("plantable =", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableNotEqualTo(String value) {
			addCriterion("plantable <>", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableGreaterThan(String value) {
			addCriterion("plantable >", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableGreaterThanOrEqualTo(String value) {
			addCriterion("plantable >=", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableLessThan(String value) {
			addCriterion("plantable <", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableLessThanOrEqualTo(String value) {
			addCriterion("plantable <=", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableLike(String value) {
			addCriterion("plantable like", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableNotLike(String value) {
			addCriterion("plantable not like", value, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableIn(List<String> values) {
			addCriterion("plantable in", values, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableNotIn(List<String> values) {
			addCriterion("plantable not in", values, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableBetween(String value1, String value2) {
			addCriterion("plantable between", value1, value2, "plantable");
			return (Criteria) this;
		}

		public Criteria andPlantableNotBetween(String value1, String value2) {
			addCriterion("plantable not between", value1, value2, "plantable");
			return (Criteria) this;
		}

		public Criteria andCansellIsNull() {
			addCriterion("cansell is null");
			return (Criteria) this;
		}

		public Criteria andCansellIsNotNull() {
			addCriterion("cansell is not null");
			return (Criteria) this;
		}

		public Criteria andCansellEqualTo(Integer value) {
			addCriterion("cansell =", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellNotEqualTo(Integer value) {
			addCriterion("cansell <>", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellGreaterThan(Integer value) {
			addCriterion("cansell >", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellGreaterThanOrEqualTo(Integer value) {
			addCriterion("cansell >=", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellLessThan(Integer value) {
			addCriterion("cansell <", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellLessThanOrEqualTo(Integer value) {
			addCriterion("cansell <=", value, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellIn(List<Integer> values) {
			addCriterion("cansell in", values, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellNotIn(List<Integer> values) {
			addCriterion("cansell not in", values, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellBetween(Integer value1, Integer value2) {
			addCriterion("cansell between", value1, value2, "cansell");
			return (Criteria) this;
		}

		public Criteria andCansellNotBetween(Integer value1, Integer value2) {
			addCriterion("cansell not between", value1, value2, "cansell");
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