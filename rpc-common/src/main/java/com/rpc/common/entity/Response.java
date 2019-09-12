package com.rpc.common.entity;

import java.io.Serializable;

/**
 * RPC调用返回的结果
 * 
 * @author zhao
 *
 */
public class Response implements Serializable{

	private static final long serialVersionUID = 4235979493889293157L;
	
	private Object data;
	private String status;
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
