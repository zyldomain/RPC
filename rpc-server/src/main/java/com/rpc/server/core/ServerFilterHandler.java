package com.rpc.server.core;

import java.util.ArrayList;
import java.util.List;

import com.rpc.common.filter.Filter;
import com.rpc.common.filter.FilterInvoker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerFilterHandler extends ChannelInboundHandlerAdapter {

	public static List<Filter> filters = new ArrayList<>();
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FilterInvoker.invoke(ctx, msg, 0, filters);
	}
}
