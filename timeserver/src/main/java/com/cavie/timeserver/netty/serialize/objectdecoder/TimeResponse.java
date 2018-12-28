package com.cavie.timeserver.netty.serialize.objectdecoder;

import java.io.Serializable;

/**
 * 时间响应
 *
 * @author created by Cavielee
 * @date 2018年12月28日 上午11:27:55
 */
public class TimeResponse implements Serializable {

	/**
	 * 默认的 ID
	 */
	private static final long serialVersionUID = 1L;

	// 状态码，0代表成功
	private int respCode;

	// 时间
	private String time;

	private String errMsg;

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public String toString() {
		return "TimeResponse[ respCode: " + respCode + " ; time :" + time + " ; errMsg :" + errMsg + " ]";
	}
}
