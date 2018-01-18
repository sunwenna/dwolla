package com.dwolla.controller;

import com.alibaba.fastjson.JSONObject;
import com.dwolla.api.util.AccountUtil;
import com.dwolla.api.util.ApiClientUtil;
import com.jfinal.core.Controller;

import io.swagger.client.ApiException;
import io.swagger.client.api.AccountsApi;
import io.swagger.client.api.TransfersApi;
import io.swagger.client.model.AccountInfo;
import io.swagger.client.model.TransferListResponse;

public class AccountController extends Controller{

	/**
	 * 账户首页
	 */
	public void index() {
		try {
			AccountsApi accountApi = new AccountsApi(ApiClientUtil.getApiClient());
			setAttr("description","账户资源列表 id:"+AccountUtil.getAccountId());
			AccountInfo accountInfo = accountApi.id(AccountUtil.getAccountId());
			setAttr("response",accountInfo.toString());
			setAttr("accountInfo",accountInfo);
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("response","系统异常！请稍后重试！");
		}
		render("index.html");
	}
	
	/**
	 * 跳转账户列表
	 */
	public void fundingsources() {
		redirect("/fundingsources");
	}
	
	public void transfers() {
		int limit = getParaToInt("limit", 10);
		int offset = getParaToInt("offset", 0);
		try {
			TransfersApi transfersApi = new TransfersApi(ApiClientUtil.getApiClient());
			TransferListResponse response = transfersApi.getAccountTransfers(AccountUtil.getAccountId(), limit, offset);
			setAttr("response", response.toString());
			setAttr("transferList", response);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ApiException) {
				JSONObject error = JSONObject.parseObject(e.getMessage());
				if ("NotFound".equals(error.get("code"))) {
					setAttr("response", "无交易记录！");
				}
			}
			setAttr("transferList", null);
		}
		render("transfers.html");
	}
	
}
