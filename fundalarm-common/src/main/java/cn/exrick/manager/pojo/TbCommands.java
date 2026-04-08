package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class TbCommands implements Serializable {
	private Integer id;

	@ApiModelProperty(value = "标题")
	private String title;

	private String nicktitle;

	@ApiModelProperty(value = "需求描述")
	private String password;

	@ApiModelProperty(value = "编码")
	private String scriptcode;

	private Date created;

	private Date updated;

	@ApiModelProperty(value = "非指令有值")
	private String url;

	@ApiModelProperty(value = "0指令 1网页 2小程序")
	private Integer type;

	@ApiModelProperty(value = "logo")
	private String logo;

	@ApiModelProperty(value = "短描述")
	private String description;

	@ApiModelProperty(value = "使用人数")
	private Integer usecount;

	@ApiModelProperty(value = "版本")
	private String version;

	@ApiModelProperty(value = "主图")
	private String img;

	@ApiModelProperty(value = "售价")
	private BigDecimal price;

	@ApiModelProperty(value = "原价")
	private BigDecimal originalPrice;

	@ApiModelProperty(value = "1有效  0无效")
	private Integer valid;

	@ApiModelProperty(value = "1是h5  0非h5")
	private Integer forh5;

	@ApiModelProperty(value = "1私人定制 0公用")
	private Integer privatetag;

	@ApiModelProperty(value = "0 开发中 1开发完毕 2招商中")
	private Integer lifeperiod;

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

	public String getNicktitle() {
		return nicktitle;
	}

	public void setNicktitle(String nicktitle) {
		this.nicktitle = nicktitle == null ? null : nicktitle.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getScriptcode() {
		return scriptcode;
	}

	public void setScriptcode(String scriptcode) {
		this.scriptcode = scriptcode == null ? null : scriptcode.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo == null ? null : logo.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Integer getUsecount() {
		return usecount;
	}

	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img == null ? null : img.trim();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getForh5() {
		return forh5;
	}

	public void setForh5(Integer forh5) {
		this.forh5 = forh5;
	}

	public Integer getPrivatetag() {
		return privatetag;
	}

	public void setPrivatetag(Integer privatetag) {
		this.privatetag = privatetag;
	}

	public Integer getLifeperiod() {
		return lifeperiod;
	}

	public void setLifeperiod(Integer lifeperiod) {
		this.lifeperiod = lifeperiod;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", nicktitle=").append(nicktitle);
		sb.append(", password=").append(password);
		sb.append(", scriptcode=").append(scriptcode);
		sb.append(", created=").append(created);
		sb.append(", updated=").append(updated);
		sb.append(", url=").append(url);
		sb.append(", type=").append(type);
		sb.append(", logo=").append(logo);
		sb.append(", description=").append(description);
		sb.append(", usecount=").append(usecount);
		sb.append(", version=").append(version);
		sb.append(", img=").append(img);
		sb.append(", price=").append(price);
		sb.append(", originalPrice=").append(originalPrice);
		sb.append(", valid=").append(valid);
		sb.append(", forh5=").append(forh5);
		sb.append(", privatetag=").append(privatetag);
		sb.append(", lifeperiod=").append(lifeperiod);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}