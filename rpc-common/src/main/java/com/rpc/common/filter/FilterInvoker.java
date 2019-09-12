package com.rpc.common.filter;

import java.util.List;

import com.rpc.common.entity.Request;
import com.rpc.common.entity.Response;

import io.netty.channel.ChannelHandlerContext;

public class FilterInvoker {
	public static void invoke(ChannelHandlerContext cx, Object msg, int step, List<Filter> filters) {
		if(step == filters.size()) {
			cx.fireChannelRead(msg);
		}
		
		if(msg instanceof Request) {
			filters.get(step).PostRequest((Request)msg);
		}else if(msg instanceof Response) {
			filters.get(step).PostResponse((Response)msg);
		}
		
		invoke(cx, msg, step + 1, filters);
	}
}
