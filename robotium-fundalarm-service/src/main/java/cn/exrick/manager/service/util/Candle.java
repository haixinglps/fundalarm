package cn.exrick.manager.service.util;

public class Candle {
	double high;
	double low;
	double close;
	long tm;
	double sar;
	double cjl;

	public double getHigh() {
		return high;
	}

	public double getCjl() {
		return cjl;
	}

	public void setCjl(double cjl) {
		this.cjl = cjl;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getTm() {
		return tm;
	}

	public void setTm(long tm) {
		this.tm = tm;
	}

	public double getSar() {
		return sar;
	}

	public void setSar(double sar) {
		this.sar = sar;
	}

	public Candle(double high, double low, double close, long tm) {
		this.high = high;
		this.low = low;
		this.close = close;
		this.tm = tm;
	}

}