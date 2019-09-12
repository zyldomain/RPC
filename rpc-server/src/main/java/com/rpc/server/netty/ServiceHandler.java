package com.rpc.server.netty;

import com.rpc.common.util.CompressUtil;
import com.rpc.common.util.ContextUtil;
import com.rpc.server.core.RequestHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServiceHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] bytes = (byte[])msg;
		
		Integer sessionID = ContextUtil.getSessionID(bytes);
		byte[] bytesrc = CompressUtil.uncompress(ContextUtil.getBody(bytes));

		byte[] responseBytes = ContextUtil.mergeSessionID(sessionID, CompressUtil.compress(RequestHandler.handler(bytesrc)));		

		ctx.writeAndFlush(responseBytes);
	}
	
}
