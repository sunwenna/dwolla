package com.dwolla.controller;

import com.dwolla.api.util.ApiClientUtil;
import com.dwolla.api.util.ApiConfigUtil;
import com.jfinal.core.Controller;

import io.swagger.client.api.WebhooksubscriptionsApi;
import io.swagger.client.model.CreateWebhook;
import io.swagger.client.model.WebhookListResponse;

public class WebhookSubscriptionsController extends Controller{

	public void index() {
		try {
			WebhooksubscriptionsApi webhookApi = new WebhooksubscriptionsApi(ApiClientUtil.getApiClient());
			WebhookListResponse response = webhookApi.list();
			setAttr("response", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("index.html");
	}
	
	public void info() {
		render("info.html");
	}
	
	public void add() {
		String url = getPara("url");
		String secret = ApiConfigUtil.getSecret();
		try {
			WebhooksubscriptionsApi webhookApi = new WebhooksubscriptionsApi(ApiClientUtil.getApiClient());
			CreateWebhook body = new CreateWebhook();
			body.setUrl(url);
			body.setSecret(secret);
			webhookApi.create(body);
		} catch (Exception e) {
			setAttr("msg","添加失败");
			render("info.html");
		}
		setAttr("msg","添加成功");
		setAttr("url","/webhooksubscriptions/");
		render("/WEB-INF/view/msg.html");
	}
	
}
