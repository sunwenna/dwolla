package com.dwolla.api.util;

import com.jfinal.kit.StrKit;

import io.swagger.client.api.RootApi;
import io.swagger.client.model.CatalogResponse;
import io.swagger.client.model.HalLink;

/**
 * 平台账户工具类
 * @author lixiongfei
 */
public class AccountUtil {
	
	private static String accountId = "";
	
	/**
	 * 获取平台账户ID
	 * @return
	 */
	public static String getAccountId() {
		if (StrKit.isBlank(accountId)) {
			synchronized (accountId) {
				if (StrKit.isBlank(accountId)) {
					setAccountId();
				}
			}
		}
		return accountId;
	}

	/**
	 * 设置账户ID
	 */
	private static void setAccountId() {
		try {
			RootApi rootApi = new RootApi(ApiClientUtil.getApiClient());
			CatalogResponse response = rootApi.root();
			HalLink link = response.getLinks().get("account");
			accountId=  getIdByUrl(link.getHref());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据资源链接获取账户ID
	 * @param url
	 * @return
	 */
	public static String getIdByUrl(String url) {
		String id = "";
		try {
			id = url.substring(url.lastIndexOf(47) + 1);
		} catch (Exception e) {
			id = "";
			e.printStackTrace();
		}
		return id;
	}

}
