package com.dwolla.controller;

import com.dwolla.api.util.ApiClientUtil;
import com.jfinal.core.Controller;

import io.swagger.client.api.EventsApi;
import io.swagger.client.model.EventListResponse;

public class EventsController extends Controller{

	public void index() {
		try {
			int limit = 25;
			int offset = 0;
			EventsApi eventsApi = new EventsApi(ApiClientUtil.getApiClient());
			EventListResponse  response = eventsApi.events(limit, offset);
			setAttr("response", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("index.html");
	}
}
