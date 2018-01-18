package com.dwolla.api.constant;

public interface AppConstant {
	
	
	public enum AppConfig{
		
		APP_PROPERTIES("app.properties","application基础配置"),
		API_PROPERTIES("api.properties","api基础配置")
		;
		
		public String file = "";
		public String descript = "";
		AppConfig(String file, String descript) {
			this.file = file;
			this.descript = descript;
		}
		
	} 
	
	
}
