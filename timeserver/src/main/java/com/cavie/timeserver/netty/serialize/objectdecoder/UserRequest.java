package com.cavie.timeserver.netty.serialize.objectdecoder;

import java.io.Serializable;

/**
* 用户请求
*
* @author created by Cavielee
* @date 2018年12月28日 上午11:18:11
*/
public class UserRequest implements Serializable {

	/**
	 * 默认 ID 序列
	 */
	private static final long serialVersionUID = 1L;
	
	// 用户id
	private int uid;
	private String username;
	private String order;
	
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "UserRequest[ uid: " + uid + " ; username :" + username + "; order :" + order + " ]";
	}
	
	
}
