package com.dwolla.api.util;

import com.dwolla.api.constant.AppConstant;
import com.jfinal.kit.PropKit;

/**
 * 应用参数设置工具类
 * @author lixiongfei
 */
public class AppConfigUtil {

	public static boolean isDev(){
		return PropKit.use(AppConstant.AppConfig.APP_PROPERTIES.file, "utf-8").getBoolean("devModel");
	}
	
}
