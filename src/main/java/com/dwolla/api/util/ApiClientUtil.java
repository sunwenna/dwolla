package com.dwolla.api.util;

import com.dwolla.api.bean.AccessToken;
import com.dwolla.api.common.Authorization;
import com.dwolla.api.exception.TokenException;

import io.swagger.client.ApiClient;

public class ApiClientUtil {
	
	private static volatile ApiClient apiClient = null;
	
	private ApiClientUtil(){}
	
	public static ApiClient getApiClient() throws Exception {
		if (apiClient == null) {
			synchronized (ApiClient.class) {
				if (apiClient == null) {
					AccessToken token = Authorization.getAccessToken();
					if (token == null) {
						throw new TokenException("获取access token失败");
					}
					apiClient = new ApiClient();
					apiClient.setBasePath(ApiConfigUtil.getBaseApiHost());
					apiClient.setAccessToken(token.getAccessToke());
				}
			}
		}
		return apiClient;
	}

}
