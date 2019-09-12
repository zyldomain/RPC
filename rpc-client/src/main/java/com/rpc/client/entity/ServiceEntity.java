package com.rpc.client.entity;

public class ServiceEntity {
	private String name;
	private String className;
	private Integer timeout;
	
	public ServiceEntity(String name, String className, Integer timeout) {
		super();
		this.name = name;
		this.className = className;
		this.timeout = timeout;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
}
