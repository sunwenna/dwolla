package com.dwolla;

import com.dwolla.api.constant.AppConstant;
import com.dwolla.controller.AccountController;
import com.dwolla.controller.CustomerFundingsourcesController;
import com.dwolla.controller.CustomersController;
import com.dwolla.controller.EventsController;
import com.dwolla.controller.FundingSourcesController;
import com.dwolla.controller.GuidesController;
import com.dwolla.controller.IndexController;
import com.dwolla.controller.TransfersController;
import com.dwolla.controller.WebhookSubscriptionsController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;

public class AppConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
        PropKit.use(AppConstant.AppConfig.APP_PROPERTIES.file);
        me.setDevMode(PropKit.getBoolean("devModel"));
	}

	@Override
	public void configRoute(Routes me) {
		me.setBaseViewPath("WEB-INF/view");
		me.add("/", IndexController.class);
		me.add("/account", AccountController.class);
		me.add("/events", EventsController.class);
		me.add("/webhooksubscriptions", WebhookSubscriptionsController.class);
		me.add("/transfers", TransfersController.class);
		
		me.add("/customers", CustomersController.class);
		me.add("/customers/fundingsources", CustomerFundingsourcesController.class);

		me.add("/fundingsources", FundingSourcesController.class);
		
		
		me.add("/guides", GuidesController.class);
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

    public static void main(String[] args) {
    	System.out.println("-------");
        JFinal.start("src/main/webapp", 4444, "/");
    }
	
}
