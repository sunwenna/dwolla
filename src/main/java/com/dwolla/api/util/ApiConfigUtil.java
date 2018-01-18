package com.dwolla.api.util;

import com.dwolla.api.constant.AppConstant;
import com.jfinal.kit.PropKit;

public class ApiConfigUtil {

	/**
	 * 获取接口地址
	 * @return
	 */
	public static String getBaseHost() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.host");
	}
	
	/**
	 * 获取接口主机地址
	 * @return
	 */
	public static String getBaseApiHost() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.api_host");
	}
	
	public static String getAccessToken() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.token");
	}
	
	public static String getClientId() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.client_id");
	}
	
	public static String getClientSecret() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.client_secret");
	}
	
	public static String getGrantType() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.grant_type");
	}
	
	/**
	 * access token 刷新间隔时间，单位（秒）
	 * @return
	 */
	public static int getTokenRefreshPeriod() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").getInt("dwolla.token_refresh_period");
	}
	
	public static String getSecret() {
		return PropKit.use(AppConstant.AppConfig.API_PROPERTIES.file, "utf-8").get("dwolla.secret");
	}
	
	public static void main(String [] args) {
		System.out.println(getBaseHost());
	}
}
