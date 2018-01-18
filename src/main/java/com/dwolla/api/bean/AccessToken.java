package com.dwolla.api.bean;

import java.util.Calendar;
import java.util.Date;

/**
 * access token Bean
 * @author lixiongfei
 */
public class AccessToken {
	
	/**
	 * access token
	 */
	private String accessToke;
	/**
	 * token 类型
	 */
	private String tokenType;
	/**
	 * 有效时间
	 */
	private int expiresIn;
	/**
	 * 到期时间
	 */
	private Date expireTime;
	
	public String getAccessToke() {
		return accessToke;
	}
	public void setAccessToke(String accessToke) {
		this.accessToke = accessToke;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		// 设置到期时间
		expireTime = new Date(System.currentTimeMillis()+(expiresIn * 1000));
		this.expiresIn = expiresIn;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	/**
	 * 是否到期了
	 * @return
	 */
	public boolean isExpired() {
		if (System.currentTimeMillis() > expireTime.getTime()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "AccessToken [accessToke=" + accessToke + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn
				+ ", expireTime=" + expireTime + "]";
	}
	

}
