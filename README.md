# dwolla

美国流行的第三方支付dwolla使用示例项目，包含中文使用说明文档


# 接口说明文档

## 1、请求参数说明

### 1.1.所有的请求头必须设置

	Accept:application/vnd.dwolla.v1.hal+json

### 1.2.POST请求头必须设置

	Content-Type:application/vnd.dwolla.v1.hal+json

### 1.3.所有的请求和相应内容为JSON编码

### 1.4.所有的请求必须是https，任何非安全的请求会重定向到对应的地址

	-H "Content-Type: application/vnd.dwolla.v1.hal+json" -H "Accept: application/vnd.dwolla.v1.hal+json" 

### 1.5.授权请求头设置

- 所有的请求都需要OAuth access token 或者是 client_id 和 client_secret 授权

- OAuth access token 请求Header设置：
	Authorization: Bearer {这里填写access_token}
	
- 例如：
	-H "Authorization: Bearer asdfwXTdDQFimVQOMdn9bOGHJh8KrqnFi34sugYqgrULRCb"

- client_id 和 client_secret授权请求Header设置
	Content-Type: application/x-www-form-urlencoded或application/json

## 2、API地址
- 生产：https://api.dwolla.com

- 测试：https://api-sandbox.dwolla.com

## 3、防重复提交KEY
- 如果因为网络原因数据传输失败，可以使用相同的key重复去请求。
- 如果使用相同的KEY提交，则仅会返回第一次提交的结果。使用唯一的ID作为防重复的可以，比如uuid
- 该ID24小时内有效，即相同ID在24小时内的请求会认为是同一个请求，主要是防止短时间内的重复提交。

例如：

	-H "Idempotency-Key: d2adcbab-4e4e-430b-9181-ac9346be723a"

## 4、错误响应
- 错误响应使用HTTP状态码来指示错误的类型。JSON响应主体将包含一个顶级错误代码和一个包含错误的详细描述的消息。
- 错误返回内容参考：https://github.com/blongden/vnd.error
- 错误示例：

	{
	  "code": "InvalidAccessToken",
	  "message": "Invalid access token."
	}
 
## 5、使用SDK
### 5.1.编译sdk jar包

	git clone https://github.com/Dwolla/dwolla-swagger-java
	cd dwolla-swagger-java
	mvn install package

### 5.2.将jar包导入项目中

### 5.3代码使用示例

	import io.swagger.client.ApiClient;
	import io.swagger.client.api.*;
	import io.swagger.client.model.*;
	ApiClient a = new ApiClient();
	a.setBasePath("https://api-sandbox.dwolla.com");
	a.setAccessToken("a token");
	CustomersApi c = new CustomersApi(a);
	CustomerListResponse custies = c.list(10);

## 6、授权流程
### 6.1 创建应用
- 创建应用后获取你的client_id和client_secret
- 测试环境注册后会自动创建一个应用。
- 确保client_secret保密

### 6.2 token生命周期
- Access token 生成后只有1个小时的有效时间。
- 刷新Access token回获取到一个新的access token，并且使旧的access token失效

### 6.3 应用授权接口
- 生产地址: POST https://www.dwolla.com/oauth/v2/token
- 沙箱地址: POST https://sandbox.dwolla.com/oauth/v2/token

	Header:
	Content-Type: application/x-www-form-urlencoded 

- 请求参数

<table>
	<tr>
		<th>参数</th>
		<th>是否必须</th>
		<th>类型	</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>client_id</td>
		<td>必须</td>
		<td>string</td>
		<td>应用的client_id</td>
	</tr>
	<tr>
		<td>client_secret</td>
		<td>必须</td>
		<td>string</td>
		<td>应用的client_secret</td>
	</tr>
	<tr>
		<td>grant_type</td>
		<td>必须</td>
		<td>string</td>
		<td>固定参数：client_credentials</td>
	</tr>
<table>

- 响应参数

<table>
	<tr>
		<th>参数</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>access_token</td>
		<td>获取一个新的access_token 用于访问接口</td>
	</tr>
	<tr>
		<td>expires_in</td>
		<td>access_token有效时间，单位秒</td>
	</tr>
	<tr>
		<td>grant_type</td>
		<td>固定字符串bearer</td>
	</tr>
<table>

- 响应示例：

	{
	  "access_token": "SF8Vxx6H644lekdVKAAHFnqRCFy8WGqltzitpii6w2MVaZp1Nw",
	  "token_type": "bearer",
	  "expires_in": 3600
	}

## 7.查看账户
- 账户接口检索：
<table>
	<tr>
		<th>链接</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>self</td>
		<td>账户资源链接地址</td>
	</tr>
	<tr>
		<td>receive</td>
		<td>按照这个链接创建一个转账到这个帐户。</td>
	</tr>
	<tr>
		<td>funding-sources</td>
		<td>GET 账户资金来源列表</td>
	</tr>
	<tr>
		<td>transfers</td>
		<td>GET 账户资金转账列表</td>
	</tr>
	<tr>
		<td>customers</td>
		<td>(可选) 如果存在此链接，则授权该帐户创建和管理访问API客户</td>
	</tr>
	<tr>
		<td>send</td>
		<td>按照这个链接创建一个转账到这个帐户。</td>
	</tr>
<table>


### 7.1 创建资金账户
> 给 Dwolla 添加银行账号，在创建时，银行帐户处于未验证的状态。在从Dwolla账号转账前需要via验证。

- 请求地址
	
	POST https://api.dwolla.com/funding-sources
	
- 请求参数

<table>
	<tr>
		<th>参数</th>
		<th>是否必须</th>
		<th>类型	</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>accountNumber</td>
		<td>必须</td>
		<td>String</td>
		<td>银行账号</td>
	</tr>
	<tr>
		<td>routingNumber</td>
		<td>必须</td>
		<td>String</td>
		<td>银行路由账号</td>
	</tr>
	<tr>
		<td>bankAccountType</td>
		<td>必须</td>
		<td>String</td>
		<td>账号类型:checking/savings</td>
	</tr>
	<tr>
		<td>name</td>
		<td>必须</td>
		<td>String</td>
		<td>资金账号昵称</td>
	</tr>
	<tr>
		<td>channels</td>
		<td>非必须</td>
		<td>array</td>
		<td>处理通道数组。ACH是银行转账的默认处理通道</td>
	</tr>
<table>

- HTTP状态

<table>
	<tr>
		<th>链接</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>400</td>
		<td>重复的资金来源或验证错误。</td>
	</tr>
	<tr>
		<td>403</td>
		<td>未授权。</td>
	</tr>
</table>

- 接口调用

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

## 8.客户
### 8.1 客户管理接口重要说明

- 在转账时，至少一方必须完成身份验证过程，即发送方或接收方
<p>根据您的业务模型，您决定由哪个方完成这个过程，您可能希望双方完成身份验证过程。当客户从您的帐户中发送资金或接收资金时，客户仍然无法验证，因为您的帐户已经被验证。但是，如果您需要在您的客户之间转移资金，那么至少有一个需要验证。</p>

### 8.2 客户功能连接
<table>
	<tr>
		<th>链接</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>self</td>
		<td>当前客户资源地址</td>
	</tr>
	<tr>
		<td>receive</td>
		<td>为当前客户创建一个交易</td>
	</tr>
	<tr>
		<td>funding-sources</td>
		<td>获取当前客户的资金来源</td>
	</tr>
	<tr>
		<td>transfers</td>
		<td>获取当前客户的交易</td>
	</tr>
	<tr>
		<td>send</td>
		<td>可选，客户支付金额接口</td>
	</tr>
	<tr>
		<td>retry-verification</td>
		<td>状态为retry，使用该链接更正客户验证信息</td>
	</tr>
	<tr>
		<td>verify-with-document</td>
		<td>状态为document，上传图片文档验证</td>
	</tr>
	<tr>
		<td>verify-business-with-document</td>
		<td>客户类型为：business，状态为document，上传图片验证公司</td>
	</tr>
	<tr>
		<td>verify-authorized-representative-and-business-with-document</td>
		<td>客户类型为：business，状态为document，上传图片验证身份和公司</td>
	</tr>
</table>

### 8.3 客户状态
<table>
	<tr>
		<th>状态</th>
		<th>描述</th>
	</tr>
	<tr>
		<td>unverified</td>
		<td>未经验证(unverified)或仅可以收款(receive-only )</td>
	</tr>
	<tr>
		<td>retry</td>
		<td>初始的验证尝试失败，因为提供的信息没有满足验证检查</td>
	</tr>
	<tr>
		<td>document</td>
		<td>需要上传图片认证</td>
	</tr>
	<tr>
		<td>verified</td>
		<td>认证的个人或企业客户可以拥有这种状态。客户当前已验证</td>
	</tr>
	<tr>
		<td>suspended</td>
		<td>所有客户类型都可以有暂停的状态。客户被暂停，可能既不发送也不接收资金</td>
	</tr>
	<tr>
		<td>deactivated</td>
		<td>停用状态，被停用的客户既不能发送也不能接收资金</td>
	</tr>
</table>

### 8.4 接口调用
	
#### 8.4.1 添加客户
	
		CreateCustomer customer = new CreateCustomer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setEmail(email);
		customer.setIpAddress(ipAddress);
		CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
		Unit$ result = customersApi.create(customer);
		
#### 8.4.2 修改客户

		CustomersApi customersApi = new CustomersApi(ApiClientUtil.getApiClient());
		UpdateCustomer updateCustomer = new UpdateCustomer();
		if (StrKit.notBlank(city))  updateCustomer.setCity(city);
		if (StrKit.notBlank(state))  updateCustomer.setState(state);
		if (StrKit.notBlank(postalCode)) updateCustomer.setPostalCode(postalCode);
		if (StrKit.notBlank(businessName)) updateCustomer.setBusinessName(businessName);
		if (StrKit.notBlank(doingBusinessAs)) updateCustomer.setDoingBusinessAs(doingBusinessAs);
		if (StrKit.notBlank(address1)) updateCustomer.setAddress1(address1);
		if (StrKit.notBlank(address2)) updateCustomer.setAddress2(address2);
		customer = customersApi.updateCustomer(updateCustomer, id);	

## 9.Instant account verification (IAV) 速成户头验证

<p>
	IAV	是一个需要服务端和客户端交互简单并且安全的流程。服务端需要请求一个single-use token去代表客户添加或验证他们的银行卡。客户端在页面需要引入dwolla.js包进行IAV流程。
</p>

### 9.1 前端代码

	<script type="text/javascript">
	  $('#start').click(function() {
		  var iavToken = '#(iavToken.token)';
		  dwolla.configure('#(configure)');
		  dwolla.iav.start(iavToken, {
		    container: 'iavContainer',
		    stylesheets: [
		      'http://fonts.googleapis.com/css?family=Lato&subset=latin,latin-ext'
		    ],
		    microDeposits: false,
		    fallbackToMicroDeposits: true
		  }, function(err, res) {
		     console.log('Error: ' + JSON.stringify(err) + ' -- Response: ' + JSON.stringify(res));
		  });
		});
	</script>

> iav前端代码中包含google的js引用，正常使用需要能访问到google，配置好代理后再使用该功能

### 9.2 iavToken获取

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


##  10 交易

### 10.1 转账参数说明
<table>
	<thead>
		<tr>
			<th>参数</th>
			<th>描述</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>id</td>
			<td>转账唯一ID</td>
		</tr>
		<tr>
			<td>status</td>
			<td>状态：processed 已转账, pending 交易中, cancelled 已取消, failed 转账失败,reclaimed 重新发起</td>
			</tr>
		<tr>
			<td>amount</td>
			<td>转账金额 Amount JSON object</td>
		</tr>
		<tr>
			<td>created</td>
			<td>创建时间，ISO-8601 timestamp</td>
		</tr>
		<tr>
			<td>metadata</td>
			<td>元数据，A metadata JSON object</td>
		</tr>
		<tr>
			<td>clearing</td>
			<td>结算数据，A clearing JSON object.</td>
		</tr>
		<tr>
			<td>correlationId</td>
			<td>用于关联本次交易的唯一的ID</td>
		</tr>
	</tbody>
</table>


	{
	  "_links": {},
	  "_embedded": {},
	  "id": "string",
	  "status": "string",
	  "amount": {
	    "value": "string",
	    "currency": "string"
	  },
	  "created": "string",
	  "metadata": {
	    "key": "value"
	    },
	  "clearing": {
	    "source": "standard",
	    "destination": "next-available"
	  },
	  "correlationId": "string"
	}

### 10.2  转账金额格式 Amount JSON object
<table><thead>
<tr>
	<th>参数</th>
	<th>描述</th>
</tr>
</thead><tbody>
<tr>
	<td>value</td>
	<td>转账金额</td>
</tr>
<tr>
	<td>currency</td>
	<td>
		资金类型，String, USD</code>
	</td>
</tr>
</tbody></table>

### 10.3 结算格式 A clearing JSON object

<table><thead>
<tr>
	<th>参数</th>
	<th>描述</th>
</tr>
</thead><tbody>
<tr>
	<td>source</td>
	<td>资金来源</td>
</tr>
<tr>
	<td>destination</td>
	<td>
		转账目标</code>
	</td>
</tr>
</tbody></table>


### 10.4 交易API调用

- HTTP 请求地址
	
	POST https://api.dwolla.com/transfers
	
- HTTP 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>Required</th>
<th>类型</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>_links</td>
<td>yes</td>
<td>JSON object</td>
<td>一个JSON Object,保存资金的转出方和转入方。</td>
</tr>
<tr>
<td>amount</td>
<td>yes</td>
<td>object</td>
<td>金额 Amount JSON object.参考前面的转账金额格式</td>
</tr>
<tr>
<td>metadata</td>
<td>no</td>
<td>object</td>
<td>元数据JSON Object ,最多10个键值对。每个键值对必须小于255个字符.</td>
</tr>
<tr>
<td>fees</td>
<td>no</td>
<td>array</td>
<td>费用数组，Fee JSON Object 仅包含一个转账费用</td>
</tr>
<tr>
<td>clearing</td>
<td>no</td>
<td>object</td>
<td>结算JSON Object,包含一个源和一个目标。</td>
</tr>
<tr>
<td>correlationId</td>
<td>no</td>
<td>string</td>
<td>本次交易关联的唯一ID，必须少于255个字符，并且不包含空格[a-z | 0-9 | - | . | _ ]</td>
</tr>
</tbody></table>

- 转出源和转入目标的参数类型
<table><thead>
<tr>
<th>转出源类型</th>
<th>URI</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>Funding source</td>
<td><code>https://api.dwolla.com/funding-sources/{id}</code></td>
<td>银行或者有可用余额的资金源.</td>
</tr>
</tbody></table>
<table><thead>
<tr>
<th>转入目标类型</th>
<th>URI</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>Funding source</td>
<td><code>https://api.dwolla.com/funding-sources/{id}</code></td>
<td>目标可以是平台账户，或者是已验证的客户银行卡或余额账号.</td>
</tr>
<tr>
<td>Customer</td>
<td><code class="prettyprint">https://api.dwolla.com/customers/{id}</code></td>
<td>目标是客户账户.</td>
</tr>
<tr>
<td>Account</td>
<td><code class="prettyprint">https://api.dwolla.com/accounts/{id}</code></td>
<td>目标是平台账户.</td>
</tr>
<tr>
<td>Email</td>
<td><code>mailto:johndoe@email.com</code></td>
<td>一个存在的Dwolla 账户或接收人的邮箱</td>
</tr>
</tbody></table>

- A fee JSON object
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>_links</td>
<td>包含一个与相关源或目标客户或帐户资源相链接的JSON对象</td>
</tr>
<tr>
<td>amount</td>
<td>费用金额</td>
</tr>
</tbody></table>

	"fees": [
	  {
	    "_links": {
	      "charge-to": {
	        "href": "https://api-sandbox.dwolla.com/customers/d795f696-2cac-4662-8f16-95f1db9bddd8"
	      }
	    },
	    "amount": {
	      "value": "4.00",
	      "currency": "USD"
	    }
	  }
	]

- 接口返回状态及说明
<table><thead>
<tr>
<th>HTTP 状态</th>
<th>消息</th>
</tr>
</thead><tbody>
<tr>
<td>400</td>
<td>交易失败.</td>
</tr>
<tr>
<td>403</td>
<td>认证失败.</td>
</tr>
</tbody></table>

- 创建交易

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


## 11 Event 事件

> 当资源状态发生变化时，Dwolla创建一个新的事件资源来记录更改。当创建一个事件时，将创建一个Webhook来将事件发送到您订阅url地址。

### 11.1 事件资源
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>_links</td>
<td>包含事件、关联资源和与事件关联的帐户的链接。</td>
</tr>
<tr>
<td>id</td>
<td>事件ID</td>
</tr>
<tr>
<td>created</td>
<td>创建时间。</td>
</tr>
<tr>
<td>topic</td>
<td>事件类型。</td>
</tr>
<tr>
<td>resourceId</td>
<td>与事件关联的资源id。</td>
</tr>
</tbody></table>
	
	{
	  "_links": {
	    "self": {
	      "href": "https://api.dwolla.com/events/f8e70f48-b7ff-47d0-9d3d-62a099363a76"
	    },
	    "resource": {
	      "href": "https://api.dwolla.com/transfers/48CFDDB4-1E74-E511-80DB-0AA34A9B2388"
	    },
	    "account": {
	      "href": "https://api.dwolla.com/accounts/ca32853c-48fa-40be-ae75-77b37504581b"
	    }
	  },
	  "id": "f8e70f48-b7ff-47d0-9d3d-62a099363a76",
	  "created": "2015-10-16T15:58:15.000Z",
	  "topic": "transfer_created",
	  "resourceId": "48CFDDB4-1E74-E511-80DB-0AA34A9B2388"
	}

### 11.2 事件列表

	GET https://api.dwolla.com/events
	
#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>limit</td>
<td>返回数据条数</td>
</tr>
<tr>
<td>offset</td>
<td>略过数据条数</td>
</tr>
</tbody></table>

### 11.3 检索事件
	
	GET https://api.dwolla.com/events/{id}
	
#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>事件ID</td>
</tr>
</tbody></table>


## 12 Webhook订阅

> 当与应用程序相关的事件发生时，创建一个webhook订阅来接收Dwolla(称为webhook)的POST请求。webhook被发送到你在创建webhook订阅时提供的URL。当我们看到大多数合作伙伴维护一个webhook的子类时，你可以一次有10个活跃的webhook子。请参考触发webhook的事件列表中的events部分。
<hr/>
> 当订阅的地址连续400次访问失败时，Dwolla会自动暂停订阅。这将帮助我们确保不可用的端点不会在为其他API合作伙伴传递通知时造成延迟或问题。
<hr/>
> 当您的应用程序收到一个webhook时，它应该响应一个HTTP 2xx状态码来表示成功的收据。如果Dwolla收到大于或等于400的状态码，或者您的应用程序在尝试的20秒内没有响应，则将再次尝试。
<hr/>

#### Dwolla将在72小时内重新尝试交付8次。如果成功地收到了webhook，但是您又想要获得信息，您可以通过它的Id调用检索一个webhook。
<table><thead>
<tr>
<th>重试次数</th>
<th>间隔（相对于上次重试）</th>
<th>间隔（相对于原始请求）</th>
</tr>
</thead><tbody>
<tr>
<td>1</td>
<td>15分钟</td>
<td>15分钟</td>
</tr>
<tr>
<td>2</td>
<td>45分钟</td>
<td>1小时</td>
</tr>
<tr>
<td>3</td>
<td>2小时</td>
<td>3小时</td>
</tr>
<tr>
<td>4</td>
<td>3小时</td>
<td>6小时</td>
</tr>
<tr>
<td>5</td>
<td>6小时</td>
<td>12小时</td>
</tr>
<tr>
<td>6</td>
<td>12小时</td>
<td>24小时</td>
</tr>
<tr>
<td>7</td>
<td>24小时</td>
<td>48小时</td>
</tr>
<tr>
<td>8</td>
<td>24小时</td>
<td>72小时</td>
</tr>
</tbody></table>

### 12.1 Webhook订阅资源
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>订阅webhook唯一ID</td>
</tr>
<tr>
<td>url</td>
<td>已订阅的url</td>
</tr>
<tr>
<td>paused</td>
<td>是否已暂停</td>
</tr>
<tr>
<td>created</td>
<td>创建事件</td>
</tr>
</tbody></table>

### 12.2 创建一个订阅
#### HTTP请求

	POST https://api.dwolla.com/webhook-subscriptions

#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>url</td>
<td>订阅url地址</td>
</tr>
<tr>
<td>secret</td>
<td>一个随机的密钥，用于验证webhook订阅通知消息</td>
</tr>
</tbody></table>

### 12.3 检索一个订阅
#### HTTP请求

	GET https://api.dwolla.com/webhook-subscriptions/{id}
	
#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>webhook订阅唯一ID</td>
</tr>
</tbody></table>

### 12.4 更新一个订阅
#### HTTP请求

	POST https://api.dwolla.com/webhook-subscriptions/{id}

#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>webhook订阅唯一ID</td>
</tr>
<tr>
<td>paused</td>
<td>true 暂停订阅；false，取消暂停订阅</td>
</tr>
</tbody></table>

### 12.5 webhook订阅列表
#### HTTP请求

	GET https://api.dwolla.com/webhook-subscriptions

### 12.6 删除一个webhook订阅
#### HTTP请求

	DELETE https://api.dwolla.com/webhook-subscriptions/{id}
	
#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>webhook订阅唯一ID</td>
</tr>
</tbody></table>



### 12.7 查看一个webhook订阅的所有webhooks
#### HTTP请求

	GET https://api.dwolla.com/webhook-subscriptions/{id}/webhooks
	
#### 请求参数
<table><thead>
<tr>
<th>参数</th>
<th>描述</th>
</tr>
</thead><tbody>
<tr>
<td>id</td>
<td>webhook订阅唯一ID</td>
</tr>
<tr>
<td>limit</td>
<td>返回数据条数，默认25条</td>
</tr>
<tr>
<td>offset</td>
<td>略过的数据条数</td>
</tr>
</tbody></table>



