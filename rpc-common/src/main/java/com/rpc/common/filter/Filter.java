package com.rpc.common.filter;

import com.rpc.common.entity.Request;
import com.rpc.common.entity.Response;

public interface Filter {
	public void PostRequest(Request request);
	
	public void PostResponse(Response response);
}
