package com.rpc.server.core;

import java.io.IOException;

import com.rpc.common.entity.Request;
import com.rpc.common.entity.Response;
import com.rpc.common.serializer.HessianUtil;
import com.rpc.server.entity.Global;

public class RequestHandler {
	public static byte[] handler(byte[] requestBytes) throws ClassNotFoundException, IOException{
		Request request = (Request) HessianUtil.deserialize(requestBytes,Global.getInstance().getClassLoader());
		
		Response response = ServiceInvoke.invoke(request);
		return HessianUtil.serialize(response);
		
	}
}
