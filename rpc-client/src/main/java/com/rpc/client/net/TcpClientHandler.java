package com.rpc.client.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {

	private TCPClient tcpClient;
	
	
	public TcpClientHandler(TCPClient tcpClient ) {
		this.tcpClient=tcpClient;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//TODO 容错机制
		tcpClient.receiver((byte[]) msg);

	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
