package com.dwolla.controller;

import java.util.HashMap;
import java.util.Map;

import com.dwolla.api.util.ApiClientUtil;
import com.jfinal.core.Controller;

import io.swagger.client.api.TransfersApi;
import io.swagger.client.model.Amount;
import io.swagger.client.model.HalLink;
import io.swagger.client.model.TransferRequestBody;
import io.swagger.client.model.Unit$;

public class TransfersController extends Controller{
	
	public void index() {
		render("index.html");
	}

	/**
	 * 创建一个转账交易
	 */
	public void create() {
		String source = getPara("source");
		String destination = getPara("destination");
		String value = getPara("amount");
		try {
			TransfersApi transferApi = new TransfersApi(ApiClientUtil.getApiClient());
			TransferRequestBody transfer = new TransferRequestBody();
			Map<String, HalLink> links = new HashMap<String, HalLink>();
			HalLink sourceLink = new HalLink();
			sourceLink.setHref(source);
			HalLink destinationLink = new HalLink();
			destinationLink.setHref(destination);
			links.put("source", sourceLink);
			links.put("destination", destinationLink);
			transfer.setLinks(links);
			
			// 设置转账金额
			Amount amount = new Amount();
			amount.setValue(value);
			amount.setCurrency("USD");
			
			transfer.setAmount(amount);
			Unit$ result = transferApi.create(transfer);
			setAttr("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("index.html");
	}
	
	
}
