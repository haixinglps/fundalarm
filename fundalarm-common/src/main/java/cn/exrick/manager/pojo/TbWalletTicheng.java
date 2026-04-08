package cn.exrick.manager.pojo;

public class TbWalletTicheng  implements java.io.Serializable {
    private Long organId;

    private Double useableBalance;

    private Double forzenBalance;

    private Double totalBalance;

    private String currency;

    private Integer version;

    private Long bm;

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public Double getUseableBalance() {
        return useableBalance;
    }

    public void setUseableBalance(Double useableBalance) {
        this.useableBalance = useableBalance;
    }

    public Double getForzenBalance() {
        return forzenBalance;
    }

    public void setForzenBalance(Double forzenBalance) {
        this.forzenBalance = forzenBalance;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getBm() {
        return bm;
    }

    public void setBm(Long bm) {
        this.bm = bm;
    }
}