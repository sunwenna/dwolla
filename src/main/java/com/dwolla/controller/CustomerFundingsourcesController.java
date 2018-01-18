package com.dwolla.controller;

import com.dwolla.api.util.ApiClientUtil;
import com.jfinal.core.Controller;

import io.swagger.client.api.FundingsourcesApi;
import io.swagger.client.model.CreateFundingSourceRequest;
import io.swagger.client.model.FundingSource;

public class CustomerFundingsourcesController extends Controller{

	/**
	 * 添加页面
	 */
	public void add(){
		String id = getPara();
		setAttr("id", id);
		render("add.html");
	}
	
	/**
	 * 调用接口添加客户资金来源
	 */
	public void save() {
		String id = getPara();
		setAttr("id", id);
		String accountNumber = getPara("accountNumber");
		String routingNumber = getPara("routingNumber");
		String bankAccountType = getPara("bankAccountType");
		String name = getPara("name");
		CreateFundingSourceRequest body = new CreateFundingSourceRequest();
		body.setAccountNumber(accountNumber);
		body.setRoutingNumber(routingNumber);
		body.setType(bankAccountType);
		body.setName(name);
		try {
			FundingsourcesApi api = new FundingsourcesApi(ApiClientUtil.getApiClient());
			FundingSource fundingSource = api.createCustomerFundingSource(body, id);
			setAttr("msg","添加成功");
			setAttr("fundingSource", fundingSource);
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("msg","添加失败,"+e.getMessage());
		}
		render("add.html");
	}
	
}
