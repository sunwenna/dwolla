package com.dwolla.controller;

import com.dwolla.api.util.ApiClientUtil;
import com.dwolla.api.util.ApiConfigUtil;
import com.jfinal.core.Controller;

import io.swagger.client.api.RootApi;
import io.swagger.client.model.CatalogResponse;

public class IndexController extends Controller {

	/**
	 * 首页
	 */
	public void index(){
		try {
			setAttr("description","“根”作为API的入口点，提供应用程序，可以根据请求中提供的OAuth access_token获取和发现可用资源。如果请求中提供了应用程序访问令牌，则API将返回属于Dwolla应用程序的资源的链接(即“事件”和“网络连接订阅”)。");
			RootApi rootApi = new RootApi(ApiClientUtil.getApiClient());
			CatalogResponse response = rootApi.root();
			setAttr("response",response.toString());
			System.out.println(ApiConfigUtil.getBaseApiHost());
			setAttr("catalogResponse",response);
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("response","系统异常！请稍后重试！");
		}
		render("index.html");
	}
	
}
