package com.rpc.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * RPC调用请求
 * 
 * @author zhao
 *
 */
public class Request implements Serializable {
	private static final long serialVersionUID = -6060365745498911171L;
	// 服务名实现类名字
	private String serviceName;
	// 方法名
	private String methodName;
	// 调用方法参数的 Class路径
	private List<String> paramsTypesName;

	// 调用方法参数的 实例,顺序与上面的Class保持一致
	private List<Object> paramsValues;
	private String host;
	//是否需要丢弃当前请求
	private boolean discard;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<Object> getParamsValues() {
		return paramsValues;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParamsValues(List<Object> paramsValues) {
		this.paramsValues = paramsValues;
	}

	public List<String> getParamsTypesName() {
		return paramsTypesName;
	}

	public void setParamsTypesName(List<String> paramsTypesName) {
		this.paramsTypesName = paramsTypesName;
	}

	public boolean isDiscard() {
		return discard;
	}

	public void setDiscard(boolean discard) {
		this.discard = discard;
	}

}
