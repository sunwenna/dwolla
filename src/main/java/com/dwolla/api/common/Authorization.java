package com.dwolla.api.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.dwolla.api.bean.AccessToken;
import com.dwolla.api.exception.TokenException;
import com.dwolla.api.util.ApiClientUtil;
import com.dwolla.api.util.ApiConfigUtil;
import com.dwolla.api.util.AppConfigUtil;
import com.jfinal.kit.StrKit;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;

public class Authorization {
	
	private static AccessToken accessToken = null;
	
	private Authorization(){ }
	
	public static AccessToken getAccessToken() throws Exception {
		if (accessToken == null) {
			synchronized (AccessToken.class) {
				if (accessToken == null) {
					if(AppConfigUtil.isDev() && StrKit.notBlank(ApiConfigUtil.getAccessToken()) ) {
						accessToken = new AccessToken();
						accessToken.setExpiresIn(3600);
						accessToken.setAccessToke(ApiConfigUtil.getAccessToken());
					} else {
						// 刷新TOKEN
						Authorization.refreshToken();
					}
					
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							try {
								// 定期刷新TOKEN
								Authorization.refreshToken();
								// 刷新ApiClient中的access token;
								ApiClientUtil.getApiClient().setAccessToken(accessToken.getAccessToke());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, ApiConfigUtil.getTokenRefreshPeriod() * 1000, ApiConfigUtil.getTokenRefreshPeriod() * 1000);
				}
			}
		} else if(accessToken.isExpired()) {
			synchronized (accessToken) {
				if(accessToken.isExpired()) {
					// 刷新TOKEN
					Authorization.refreshToken();
				}
			}
		}
		return accessToken;
	}
	
	private static void refreshToken() throws Exception {
		ApiClient apiClient  = new ApiClient();
		apiClient.setBasePath(ApiConfigUtil.getBaseHost());
		Map<String, String> queryParams = new HashMap<String, String>(); 
		queryParams.put("client_id", ApiConfigUtil.getClientId());
		queryParams.put("client_secret", ApiConfigUtil.getClientSecret());
		queryParams.put("grant_type", ApiConfigUtil.getGrantType());
		Map<String, String> headerParams = new HashMap<String, String>(); 
		Map<String, String> formParams = new HashMap<String, String>(); 
		String accept = "";
		String contentType = "application/x-www-form-urlencoded";
		String [] authNames = {};
		try {
			String result = apiClient.invokeAPI("/oauth/v2/token", "POST", queryParams, null, headerParams, formParams, accept, contentType, authNames);
			// {"access_token":"lEWN3bKeYiPKqcQOAEoYc6pC1EmG12Aiyf55I9yeBSF91xuyHD","token_type":"bearer","expires_in":3605}
			System.out.println("result> "+result);
			JSONObject resultObj = JSONObject.parseObject(result);
			if (resultObj.containsKey("access_token")) {
				accessToken = new AccessToken();
				accessToken.setAccessToke(resultObj.getString("access_token"));
				accessToken.setTokenType(resultObj.getString("token_type"));
				accessToken.setExpiresIn(resultObj.getIntValue("expires_in"));
			} else {
				throw new TokenException("获取access token失败！");
			}
		} catch (ApiException e) {
			throw new TokenException("获取access token失败！");
		}
	}
	

	public static void main(String [] args) {
		String result = "{\"access_token\":\"lEWN3bKeYiPKqcQOAEoYc6pC1EmG12Aiyf55I9yeBSF91xuyHD\",\"token_type\":\"bearer\",\"expires_in\":3605}";
		JSONObject resultObj = JSONObject.parseObject(result);
		if (resultObj.containsKey("access_token")) {
			AccessToken token = new AccessToken();
			token.setAccessToke(resultObj.getString("access_token"));
			token.setTokenType(resultObj.getString("token_type"));
			token.setExpiresIn(resultObj.getIntValue("expires_in"));
			System.out.println("token>"+token.toString());
		}
	}
	
}
