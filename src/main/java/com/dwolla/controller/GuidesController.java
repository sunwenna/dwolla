package com.dwolla.controller;

import com.jfinal.core.Controller;

public class GuidesController extends Controller{

	public void index(){
		render("index.html");
	}
	
	public void sendToUser (){
		render("sendToUser.html");
	}
	
}
