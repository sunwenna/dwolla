package com.dwolla.controller;

import com.alibaba.fastjson.JSONObject;
import com.dwolla.api.util.AccountUtil;
import com.dwolla.api.util.ApiClientUtil;
import com.dwolla.api.util.AppConfigUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import io.swagger.client.ApiException;
import io.swagger.client.api.CustomersApi;
import io.swagger.client.api.FundingsourcesApi;
import io.swagger.client.api.TransfersApi;
import io.swagger.client.model.CreateCustomer;
import io.swagger.client.model.Customer;
import io.swagger.client.model.CustomerListResponse;
import io.swagger.client.model.FundingSourceListResponse;
import io.swagger.client.model.IavToken;
import io.swagger.client.model.TransferListResponse;
import io.swagger.client.model.Unit$;
import io.swagger.client.model.UpdateCustomer;

/**
 * 客户管理控制器类
 * @author lixiongfei
 */
public class CustomersController extends Controller{

	/**
	 * 客户信息首页
	 */
	public void index(){
		int size = 20;
		int offset = getParaToInt("offset", 0);
		String search = getPara("search", null);
		try {
			CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
			CustomerListResponse customerList = customersApi.list(size, offset, search);
			setAttr("customerList", customerList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("index.html");
	}
	
	/**
	 * 获取客户详细信息
	 */
	public void self() {
		String id = getPara();
		CustomersApi customersApi;
		try {
			customersApi = new CustomersApi(ApiClientUtil.getApiClient());
			Customer customer = customersApi.getCustomer(id);
			setAttr("customer", customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		render("info.html");
	}
	
	/**
	 * 修改数据
	 */
	public void update() {
		String id = getPara();
		String city = getPara("city");
		String state = getPara("state");
		String postalCode = getPara("postalCode");
		String businessName = getPara("businessName");
		String doingBusinessAs = getPara("doingBusinessAs");
		String address1 = getPara("address1");
		String address2 = getPara("address2");
		String phone = getPara("phone");
		String ipAddress = getPara("ipAddress");
		String ssn = getPara("ssn");
		String dateOfBirth = getPara("dateOfBirth");
		String type = getPara("type");
		Customer customer = null;
		UpdateCustomer updateCustomer = new UpdateCustomer();
		try {
			CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
			customer = customersApi.getCustomer(id);
			updateCustomer.setFirstName(customer.getFirstName());
			updateCustomer.setLastName(customer.getLastName());
			updateCustomer.setEmail(customer.getEmail());
			updateCustomer.setCity(customer.getCity());
			updateCustomer.setState(customer.getState());
			updateCustomer.setType(customer.getType());
			updateCustomer.setPostalCode(customer.getPostalCode());
			updateCustomer.setBusinessName(customer.getBusinessName());
			updateCustomer.setDoingBusinessAs(customer.getDoingBusinessAs());
			updateCustomer.setAddress1(customer.getAddress1());
			updateCustomer.setAddress2(customer.getAddress2());
			updateCustomer.setPhone(phone);
			updateCustomer.setIpAddress(ipAddress);
			updateCustomer.setSsn(ssn);
			updateCustomer.setDateOfBirth(dateOfBirth);
			updateCustomer.setType(type);
			if (StrKit.notBlank(city))  updateCustomer.setCity(city);
			if (StrKit.notBlank(state))  updateCustomer.setState(state);
			if (StrKit.notBlank(postalCode)) updateCustomer.setPostalCode(postalCode);
			if (StrKit.notBlank(businessName)) updateCustomer.setBusinessName(businessName);
			if (StrKit.notBlank(doingBusinessAs)) updateCustomer.setDoingBusinessAs(doingBusinessAs);
			if (StrKit.notBlank(address1)) updateCustomer.setAddress1(address1);
			if (StrKit.notBlank(address2)) updateCustomer.setAddress2(address2);
			customer = customersApi.updateCustomer(updateCustomer, id);
			setAttr("customer", customer);
		} catch (Exception e) {
			setAttr("err","修改失败，"+e.getMessage());
			e.printStackTrace();
			setAttr("customer", customer);
		}
		render("info.html");
	}
	
	/**
	 * 编辑客户信息
	 */
	public void edit() {
		redirect("/customers/self/"+getPara());
	}
	
	/**
	 * 编辑客户信息
	 */
	public void editform() {
		redirect("/customers/self/"+getPara());
	}
	
	
	/**
	 * 获取客户信息页面
	 */
	public void info(){
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			try {
				CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
				Customer customer = customersApi.getCustomer(id);
				setAttr("customer", customer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		render("info.html");
	}
	
	/**
	 * 添加客户
	 */
	public void add() {
		String firstName = getPara("firstName");
		String lastName = getPara("lastName");
		String email = getPara("email");
		String ipAddress = "127.0.0.1";
		System.out.println("firstName: "+firstName);
		System.out.println("lastName: "+lastName);
		System.out.println("email: "+email);
		System.out.println("ipAddress: "+ipAddress);
		CreateCustomer customer = new CreateCustomer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setEmail(email);
		customer.setIpAddress(ipAddress);
		try {
			CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
			Unit$ result = customersApi.create(customer);
			setAttr("content" ,result.getLocationHeader());
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("msg","添加失败");
			setAttr("customer", customer);
			render("info.html");
		}
		setAttr("msg","添加成功");
		setAttr("url","/customers/");
		render("/WEB-INF/view/msg.html");
	}
	
	/**
	 * 查看客户交易记录
	 */
	public void transfers() {
		String id = getPara(); 
		setAttr("id", id);
		int limit = getParaToInt("limit", 10);
		int offset = getParaToInt("offset", 0);
		try {
			TransfersApi transfersApi = new TransfersApi(ApiClientUtil.getApiClient());
			TransferListResponse response = transfersApi.getCustomerTransfers(id, limit, offset);
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
	
	/**
	 * 资金来源
	 */
	public void fundingsources() {
		String id = getPara(); 
		setAttr("id", id);
		boolean removed = getParaToBoolean("removed", false);
		setAttr("description","资金来源，即绑定的银行卡");
		try {
			FundingsourcesApi api = new FundingsourcesApi(ApiClientUtil.getApiClient());
			FundingSourceListResponse listResponse = api.getCustomerFundingSources(id, removed);
			setAttr("response", listResponse.toString());
			setAttr("links", listResponse.getLinks());
			setAttr("data", listResponse.getEmbedded());
		} catch (Exception e) {
			e.printStackTrace();
		}

		render("fundingsources.html");
	}
	
	/**
	 * Instant account verification (IAV)
	 */
	public void iav() {
		String id = getPara(); 
		setAttr("id", id);
		// 获取环境类型
		String configure = "prod";
		if (AppConfigUtil.isDev()) {
			configure = "sandbox";
		}
		setAttr("configure", configure);
		
		// 获取token
		try {
			CustomersApi customerApi = new CustomersApi(ApiClientUtil.getApiClient());
			IavToken iavToken =  customerApi.getCustomerIavToken(id);
			setAttr("iavToken", iavToken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		render("iav.html");
	}
}
