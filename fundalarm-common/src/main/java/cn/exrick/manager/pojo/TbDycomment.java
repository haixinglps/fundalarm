package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class TbDycomment implements Serializable {
	private Integer id;

	@ApiModelProperty(value = "作者")
	private String title;

	@ApiModelProperty(value = "内容")
	private String descr;

	@ApiModelProperty(value = "chatgpt反馈结果")
	private String result;

	@ApiModelProperty(value = "短视频对应评论")
	private String attach;

	@ApiModelProperty(value = "时间")
	private Date created;

	@ApiModelProperty(value = "点赞数")
	private Integer zancount;

	@ApiModelProperty(value = "分享数")
	private Integer sharecount;

	@ApiModelProperty(value = "评论数")
	private Integer commentcount;

	@ApiModelProperty(value = "用户编号")
	private String uid;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr == null ? null : descr.trim();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result == null ? null : result.trim();
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach == null ? null : attach.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getZancount() {
		return zancount;
	}

	public void setZancount(Integer zancount) {
		this.zancount = zancount;
	}

	public Integer getSharecount() {
		return sharecount;
	}

	public void setSharecount(Integer sharecount) {
		this.sharecount = sharecount;
	}

	public Integer getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(Integer commentcount) {
		this.commentcount = commentcount;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid == null ? null : uid.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", descr=").append(descr);
		sb.append(", result=").append(result);
		sb.append(", attach=").append(attach);
		sb.append(", created=").append(created);
		sb.append(", zancount=").append(zancount);
		sb.append(", sharecount=").append(sharecount);
		sb.append(", commentcount=").append(commentcount);
		sb.append(", uid=").append(uid);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}