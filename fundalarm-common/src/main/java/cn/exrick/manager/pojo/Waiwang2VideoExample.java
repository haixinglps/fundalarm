package cn.exrick.manager.pojo;

import java.util.ArrayList;
import java.util.List;

public class Waiwang2VideoExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public Waiwang2VideoExample() {
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

		public Criteria andVidIsNull() {
			addCriterion("vid is null");
			return (Criteria) this;
		}

		public Criteria andVidIsNotNull() {
			addCriterion("vid is not null");
			return (Criteria) this;
		}

		public Criteria andVidEqualTo(Integer value) {
			addCriterion("vid =", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidNotEqualTo(Integer value) {
			addCriterion("vid <>", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidGreaterThan(Integer value) {
			addCriterion("vid >", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidGreaterThanOrEqualTo(Integer value) {
			addCriterion("vid >=", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidLessThan(Integer value) {
			addCriterion("vid <", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidLessThanOrEqualTo(Integer value) {
			addCriterion("vid <=", value, "vid");
			return (Criteria) this;
		}

		public Criteria andVidIn(List<Integer> values) {
			addCriterion("vid in", values, "vid");
			return (Criteria) this;
		}

		public Criteria andVidNotIn(List<Integer> values) {
			addCriterion("vid not in", values, "vid");
			return (Criteria) this;
		}

		public Criteria andVidBetween(Integer value1, Integer value2) {
			addCriterion("vid between", value1, value2, "vid");
			return (Criteria) this;
		}

		public Criteria andVidNotBetween(Integer value1, Integer value2) {
			addCriterion("vid not between", value1, value2, "vid");
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

		public Criteria andDtEqualTo(String value) {
			addCriterion("dt =", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtNotEqualTo(String value) {
			addCriterion("dt <>", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtGreaterThan(String value) {
			addCriterion("dt >", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtGreaterThanOrEqualTo(String value) {
			addCriterion("dt >=", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtLessThan(String value) {
			addCriterion("dt <", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtLessThanOrEqualTo(String value) {
			addCriterion("dt <=", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtLike(String value) {
			addCriterion("dt like", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtNotLike(String value) {
			addCriterion("dt not like", value, "dt");
			return (Criteria) this;
		}

		public Criteria andDtIn(List<String> values) {
			addCriterion("dt in", values, "dt");
			return (Criteria) this;
		}

		public Criteria andDtNotIn(List<String> values) {
			addCriterion("dt not in", values, "dt");
			return (Criteria) this;
		}

		public Criteria andDtBetween(String value1, String value2) {
			addCriterion("dt between", value1, value2, "dt");
			return (Criteria) this;
		}

		public Criteria andDtNotBetween(String value1, String value2) {
			addCriterion("dt not between", value1, value2, "dt");
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

		public Criteria andChannelIsNull() {
			addCriterion("channel is null");
			return (Criteria) this;
		}

		public Criteria andChannelIsNotNull() {
			addCriterion("channel is not null");
			return (Criteria) this;
		}

		public Criteria andChannelEqualTo(String value) {
			addCriterion("channel =", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelNotEqualTo(String value) {
			addCriterion("channel <>", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelGreaterThan(String value) {
			addCriterion("channel >", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelGreaterThanOrEqualTo(String value) {
			addCriterion("channel >=", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelLessThan(String value) {
			addCriterion("channel <", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelLessThanOrEqualTo(String value) {
			addCriterion("channel <=", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelLike(String value) {
			addCriterion("channel like", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelNotLike(String value) {
			addCriterion("channel not like", value, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelIn(List<String> values) {
			addCriterion("channel in", values, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelNotIn(List<String> values) {
			addCriterion("channel not in", values, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelBetween(String value1, String value2) {
			addCriterion("channel between", value1, value2, "channel");
			return (Criteria) this;
		}

		public Criteria andChannelNotBetween(String value1, String value2) {
			addCriterion("channel not between", value1, value2, "channel");
			return (Criteria) this;
		}

		public Criteria andDurationIsNull() {
			addCriterion("duration is null");
			return (Criteria) this;
		}

		public Criteria andDurationIsNotNull() {
			addCriterion("duration is not null");
			return (Criteria) this;
		}

		public Criteria andDurationEqualTo(String value) {
			addCriterion("duration =", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationNotEqualTo(String value) {
			addCriterion("duration <>", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationGreaterThan(String value) {
			addCriterion("duration >", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationGreaterThanOrEqualTo(String value) {
			addCriterion("duration >=", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationLessThan(String value) {
			addCriterion("duration <", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationLessThanOrEqualTo(String value) {
			addCriterion("duration <=", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationLike(String value) {
			addCriterion("duration like", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationNotLike(String value) {
			addCriterion("duration not like", value, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationIn(List<String> values) {
			addCriterion("duration in", values, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationNotIn(List<String> values) {
			addCriterion("duration not in", values, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationBetween(String value1, String value2) {
			addCriterion("duration between", value1, value2, "duration");
			return (Criteria) this;
		}

		public Criteria andDurationNotBetween(String value1, String value2) {
			addCriterion("duration not between", value1, value2, "duration");
			return (Criteria) this;
		}

		public Criteria andAuthorIsNull() {
			addCriterion("author is null");
			return (Criteria) this;
		}

		public Criteria andAuthorIsNotNull() {
			addCriterion("author is not null");
			return (Criteria) this;
		}

		public Criteria andAuthorEqualTo(String value) {
			addCriterion("author =", value, "author");
			return (Criteria) this;
		}

		public Criteria andUidEqualTo(String value) {
			addCriterion("uid =", value, "uid");
			return (Criteria) this;
		}

		public Criteria andAuthorNotEqualTo(String value) {
			addCriterion("author <>", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorGreaterThan(String value) {
			addCriterion("author >", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorGreaterThanOrEqualTo(String value) {
			addCriterion("author >=", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorLessThan(String value) {
			addCriterion("author <", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorLessThanOrEqualTo(String value) {
			addCriterion("author <=", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorLike(String value) {
			addCriterion("author like", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorNotLike(String value) {
			addCriterion("author not like", value, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorIn(List<String> values) {
			addCriterion("author in", values, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorNotIn(List<String> values) {
			addCriterion("author not in", values, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorBetween(String value1, String value2) {
			addCriterion("author between", value1, value2, "author");
			return (Criteria) this;
		}

		public Criteria andAuthorNotBetween(String value1, String value2) {
			addCriterion("author not between", value1, value2, "author");
			return (Criteria) this;
		}

		public Criteria andUrlIsNull() {
			addCriterion("url is null");
			return (Criteria) this;
		}

		public Criteria andUrlIsNotNull() {
			addCriterion("url is not null");
			return (Criteria) this;
		}

		public Criteria andUrlEqualTo(String value) {
			addCriterion("url =", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotEqualTo(String value) {
			addCriterion("url <>", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlGreaterThan(String value) {
			addCriterion("url >", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlGreaterThanOrEqualTo(String value) {
			addCriterion("url >=", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLessThan(String value) {
			addCriterion("url <", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLessThanOrEqualTo(String value) {
			addCriterion("url <=", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLike(String value) {
			addCriterion("url like", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotLike(String value) {
			addCriterion("url not like", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlIn(List<String> values) {
			addCriterion("url in", values, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotIn(List<String> values) {
			addCriterion("url not in", values, "url");
			return (Criteria) this;
		}

		public Criteria andUrlBetween(String value1, String value2) {
			addCriterion("url between", value1, value2, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotBetween(String value1, String value2) {
			addCriterion("url not between", value1, value2, "url");
			return (Criteria) this;
		}

		public Criteria andGoodtagIsNull() {
			addCriterion("goodtag is null");
			return (Criteria) this;
		}

		public Criteria andGoodtagIsNotNull() {
			addCriterion("goodtag is not null");
			return (Criteria) this;
		}

		public Criteria andGoodtagEqualTo(Integer value) {
			addCriterion("goodtag =", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagNotEqualTo(Integer value) {
			addCriterion("goodtag <>", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagGreaterThan(Integer value) {
			addCriterion("goodtag >", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagGreaterThanOrEqualTo(Integer value) {
			addCriterion("goodtag >=", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagLessThan(Integer value) {
			addCriterion("goodtag <", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagLessThanOrEqualTo(Integer value) {
			addCriterion("goodtag <=", value, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagIn(List<Integer> values) {
			addCriterion("goodtag in", values, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagNotIn(List<Integer> values) {
			addCriterion("goodtag not in", values, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagBetween(Integer value1, Integer value2) {
			addCriterion("goodtag between", value1, value2, "goodtag");
			return (Criteria) this;
		}

		public Criteria andGoodtagNotBetween(Integer value1, Integer value2) {
			addCriterion("goodtag not between", value1, value2, "goodtag");
			return (Criteria) this;
		}

		public Criteria andNicknameIsNull() {
			addCriterion("nickname is null");
			return (Criteria) this;
		}

		public Criteria andNicknameIsNotNull() {
			addCriterion("nickname is not null");
			return (Criteria) this;
		}

		public Criteria andNicknameEqualTo(String value) {
			addCriterion("nickname =", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameNotEqualTo(String value) {
			addCriterion("nickname <>", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameGreaterThan(String value) {
			addCriterion("nickname >", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameGreaterThanOrEqualTo(String value) {
			addCriterion("nickname >=", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameLessThan(String value) {
			addCriterion("nickname <", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameLessThanOrEqualTo(String value) {
			addCriterion("nickname <=", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameLike(String value) {
			addCriterion("nickname like", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameNotLike(String value) {
			addCriterion("nickname not like", value, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameIn(List<String> values) {
			addCriterion("nickname in", values, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameNotIn(List<String> values) {
			addCriterion("nickname not in", values, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameBetween(String value1, String value2) {
			addCriterion("nickname between", value1, value2, "nickname");
			return (Criteria) this;
		}

		public Criteria andNicknameNotBetween(String value1, String value2) {
			addCriterion("nickname not between", value1, value2, "nickname");
			return (Criteria) this;
		}

		public Criteria andPhotoIsNull() {
			addCriterion("photo is null");
			return (Criteria) this;
		}

		public Criteria andPantagIsNull() {
			addCriterion("pantag is null");
			return (Criteria) this;
		}

		public Criteria andPhotoIsNotNull() {
			addCriterion("photo is not null");
			return (Criteria) this;
		}

		public Criteria andPhotoEqualTo(String value) {
			addCriterion("photo =", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoNotEqualTo(String value) {
			addCriterion("photo <>", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoGreaterThan(String value) {
			addCriterion("photo >", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoGreaterThanOrEqualTo(String value) {
			addCriterion("photo >=", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoLessThan(String value) {
			addCriterion("photo <", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoLessThanOrEqualTo(String value) {
			addCriterion("photo <=", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoLike(String value) {
			addCriterion("photo like", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoNotLike(String value) {
			addCriterion("photo not like", value, "photo");
			return (Criteria) this;
		}

		public Criteria andPantagNotLike(String value) {
			addCriterion("pantag not like", value, "pantag");
			return (Criteria) this;
		}

		public Criteria andPhotoIn(List<String> values) {
			addCriterion("photo in", values, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoNotIn(List<String> values) {
			addCriterion("photo not in", values, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoBetween(String value1, String value2) {
			addCriterion("photo between", value1, value2, "photo");
			return (Criteria) this;
		}

		public Criteria andPhotoNotBetween(String value1, String value2) {
			addCriterion("photo not between", value1, value2, "photo");
			return (Criteria) this;
		}

		public Criteria andCoverIsNull() {
			addCriterion("cover is null");
			return (Criteria) this;
		}

		public Criteria andCoverIsNotNull() {
			addCriterion("cover is not null");
			return (Criteria) this;
		}

		public Criteria andCoverEqualTo(String value) {
			addCriterion("cover =", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverNotEqualTo(String value) {
			addCriterion("cover <>", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverGreaterThan(String value) {
			addCriterion("cover >", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverGreaterThanOrEqualTo(String value) {
			addCriterion("cover >=", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverLessThan(String value) {
			addCriterion("cover <", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverLessThanOrEqualTo(String value) {
			addCriterion("cover <=", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverLike(String value) {
			addCriterion("cover like", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverNotLike(String value) {
			addCriterion("cover not like", value, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverIn(List<String> values) {
			addCriterion("cover in", values, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverNotIn(List<String> values) {
			addCriterion("cover not in", values, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverBetween(String value1, String value2) {
			addCriterion("cover between", value1, value2, "cover");
			return (Criteria) this;
		}

		public Criteria andCoverNotBetween(String value1, String value2) {
			addCriterion("cover not between", value1, value2, "cover");
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