package com.rpc.client.proxy;

public class ProxyFactory {
	
	/**
	 * @param type
	 * @param serviceName
	 * @param serviceImpleName
	 * @return
	 */
	public static <T> T  create(Class<?> type, String serviceName, String className) {//<T> T返回任意类型的数据？  返回代理的实例  泛型
    	
		
		ProxyHandler handler = new ProxyHandler(serviceName, className);
		
		
		return (T) handler.bind(new Class<?>[]{type});
	}

}