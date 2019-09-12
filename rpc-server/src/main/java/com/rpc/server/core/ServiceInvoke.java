package com.rpc.server.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.rpc.common.entity.Request;
import com.rpc.common.entity.Response;
import com.rpc.common.serializer.HessianUtil;
import com.rpc.server.entity.Global;

public class ServiceInvoke {
	
	private final String LIST_PATTERN = "java.util.*List";
	private final String MAP_PATTERN = "java.util.*Map";
	
	public static Response invoke(Request request) throws ClassNotFoundException {

		Response response = new Response();
		Object service = Global.getInstance().getServiceImpl(request.getServiceName());
		Class clazz = Global.getInstance().getServiceClass(request.getServiceName());

			Method method;
			try {
				Object[] requestParmsValues = new Object[request.getParamsValues() == null ? 0 : request.getParamsValues().size()];
				Class[] requestParamTypes = new Class[request.getParamsValues() == null ? 0 : request.getParamsValues().size()];
				
				
				for(int i=0;i<request.getParamsTypesName().size();i++){
					
					String className = request.getParamsTypesName().get(i);
					//基本类型不能通过class.forname获取
					if("byte".equals(className)){
						requestParamTypes[i]=byte.class;
					}else if("short".equals(className)){
						requestParamTypes[i]=short.class;
					}else if("int".equals(className)){
						requestParamTypes[i]=int.class;
					}else if("long".equals(className)){
						requestParamTypes[i]=long.class;
					}else if("float".equals(className)){
						requestParamTypes[i]=float.class;
					}else if("double".equals(className)){
						requestParamTypes[i]=double.class;
					}else if("boolean".equals(className)){
						requestParamTypes[i]=boolean.class;
					}else if("char".equals(className)){
						requestParamTypes[i]=char.class;
					}else{
						requestParamTypes[i]=Class.forName(className, false, Global.getInstance().getClassLoader());
					}
					
					requestParmsValues[i]=request.getParamsValues().get(i);
					
				}
				method = Global.getInstance().getMethod(request.getServiceName(), request.getMethodName(), request.getParamsTypesName());
				if(method==null){
					method = clazz.getMethod(request.getMethodName(),requestParamTypes);
					method.setAccessible(true);
					Global.getInstance().putMethod(request.getServiceName(), request.getMethodName(), request.getParamsTypesName(), method);
					
				}
				//日志记录
				System.out.println(new Date().toLocaleString() + ":     " + request.getHost() + "请求了" + request.getMethodName());
				response.setStatus("0");
				response.setData(method.invoke(service, requestParmsValues));
				
			} catch (NoSuchMethodException e) {
				response.setStatus("-2");
				response.setData("服务未发现");
				return response;
			} catch (SecurityException e) {
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
			}
			
		

		return response;
	}
}
	