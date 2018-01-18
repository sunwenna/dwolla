package com.dwolla.controller;

import com.dwolla.api.util.AccountUtil;
import com.dwolla.api.util.ApiClientUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import io.swagger.client.api.FundingsourcesApi;
import io.swagger.client.model.CreateFundingSourceRequest;
import io.swagger.client.model.FundingSource;
import io.swagger.client.model.FundingSourceListResponse;

/**
 * 绑定／查看资金来源接口
 * @author lixiongfei
 */
public class FundingSourcesController extends Controller{
	
	/**
	 * 平台账户资金来源列表
	 */
	public void index() {
		String link = getPara("link");
		boolean removed = getParaToBoolean("removed", false);
		setAttr("description","资金来源，即绑定的银行卡");
		setAttr("link",link);
		try {
			FundingsourcesApi api = new FundingsourcesApi(ApiClientUtil.getApiClient());
			FundingSourceListResponse listResponse = api.getAccountFundingSources(AccountUtil.getAccountId(), removed);
			setAttr("response", listResponse.toString());
			setAttr("links", listResponse.getLinks());
			setAttr("data", listResponse.getEmbedded());
		} catch (Exception e) {
			e.printStackTrace();
		}

		render("index.html");
	}
	
	/**
	 * 资金来源信息页面
	 */
	public void info() {
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			try {
				FundingsourcesApi api = new FundingsourcesApi(ApiClientUtil.getApiClient());
				FundingSource fundingSource = api.id(id);
				setAttr("response", fundingSource);
				setAttr("fundingSource", fundingSource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		render("info.html");
	}
	
	/**
	 * 保存资金来源
	 */
	public void add() {
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
			FundingSource fundingSource = api.createFundingSource(body);
			setAttr("msg","添加成功");
			setAttr("response", fundingSource);
			setAttr("fundingSource", fundingSource);
			render("info.html");
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("msg","添加失败");
		}
		setAttr("url","/fundingsources/");
		render("/WEB-INF/view/msg.html");
	}
	
}
