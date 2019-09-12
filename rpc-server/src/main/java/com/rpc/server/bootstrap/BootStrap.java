package com.rpc.server.bootstrap;

import java.io.File;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;

import com.rpc.common.util.ProtocalConst;
import com.rpc.server.core.LoadConfigure;
import com.rpc.server.core.SendToZookeeper;
import com.rpc.server.entity.Global;
import com.rpc.server.netty.ServiceHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class BootStrap {
	public static void main(String[] args) throws Exception {
		// 初始化项目路径
        String serviceName = args[0];
        
		Global.getInstance().setServiceName(serviceName);
        String rootPath = "/";
        String rootLibPath = "/lib";

        String serviceRootPath = "C:\\Users\\zyldo\\Desktop\\RPC\\rpc-server\\src\\main\\resources\\";


        // 加载配置文件，并初始化相关内容
        LoadConfigure.load(serviceRootPath);
        SendToZookeeper.zooKeeper = Global.getInstance().getZooKeeper();
        SendToZookeeper.instance = Global.getInstance();
        SendToZookeeper.init();
        SendToZookeeper.send();
        List<String> nodes = Global.getInstance().getZooKeeper().getChildren("/RPC/Services", null);
        for(String node : nodes) {
        	System.out.println(node);
        	List<String> childs =  Global.getInstance().getZooKeeper().getChildren("/RPC/Services/" + node , false, null);
        	for(String child : childs) {
        		System.out.println(child);
        	}
        }
        
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup,workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                        pipeline.addLast("decoder", new ByteArrayDecoder());
                        pipeline.addLast("encoder", new ByteArrayEncoder());
                        pipeline.addLast(new ServiceHandler());
						
					}
				})
				 .childOption(ChannelOption.SO_KEEPALIVE, true)
                 .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, Global.getInstance().getMaxBuf(), Global.getInstance().getMaxBuf()));
		ChannelFuture f = bootstrap.bind(Global.getInstance().getIp() , Global.getInstance().getPort()).sync();
		f.channel().closeFuture().sync();
		
	}
}
