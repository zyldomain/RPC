package com.rpc.client.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.rpc.client.RPC.RPC;
import com.rpc.client.entity.Address;
import com.rpc.client.exception.NotFoundMethodException;
import com.rpc.client.exception.NotFoundServiceException;
import com.rpc.client.net.TCPClient;
import com.rpc.common.entity.Request;
import com.rpc.common.entity.Response;
import com.rpc.common.serializer.HessianUtil;
import com.rpc.common.util.CompressUtil;

public class RequestHandler {
	private static Map<Address, TCPClient> tcpClientCache = new ConcurrentHashMap<Address, TCPClient>();

	private static Object lockHelper = new Object();

	public static Object request(String className, String serviceName, Request request, Class<?> returnType) throws NotFoundServiceException{

		Address addr = null;
		byte[] responseBytessrc = null;
		//先看一下是否开启缓存
		if(RPC.cache) {
			if(RPC.FailureTime <= System.currentTimeMillis()) {
				RPC.serviceCache.clear();
			}
			//缓存是否失效
			if(RPC.FailureTime.longValue() == 0l || RPC.serviceCache.get(serviceName) == null) {
				//缓存失效
				//过期需要向zookeeper查询指定的服务器
				List<Address> addresses = RPC.queryServiceHost(className);
				addr = RPC.loadBalance.loadbalance(addresses);
				//清空所有缓存
				
				RPC.serviceCache.put(serviceName, addresses);
				//重置过期时间
				RPC.FailureTime = RPC.timeout + System.currentTimeMillis();
			}else {
				addr = RPC.loadBalance.loadbalance(RPC.serviceCache.get(serviceName));
			}
		}else {
			//没有开启缓存，需要向zookeeper查询指定的服务器
			addr = RPC.loadBalance.loadbalance(RPC.queryServiceHost(className));
		}
		
		if(addr != null) {
			try {
				responseBytessrc = doRequest(request, addr, RPC.serviceList.get(serviceName).getTimeout());
				Response response = (Response) HessianUtil.deserialize( CompressUtil.uncompress(responseBytessrc), null);
				if(response.getStatus().equals("-1")) {
					//没有找到指定的服务，从zookeeper查找,将当前置空
					 RPC.serviceCache.put(serviceName,null);
					 return request(className, serviceName, request, returnType);
				}else if(response.getStatus().equals("-2")) {
					//没有指定的方法
					throw new NotFoundMethodException("方法不存在");
				}else {
					return returnType.cast(response.getData());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}else {
			//服务找不到
			throw new NotFoundServiceException("服务不存在");
		}
		return null;
		
	}
	public static byte[] doRequest(Request request, Address addr, Integer timeout) throws Exception {
		//向指定的远程方法服务器请求
		byte[] requestBytes = CompressUtil.compress(HessianUtil.serialize(request));
		TCPClient tcpClient = getTCPClient(addr,timeout);
		Integer sessionID = tcpClient.sendMsg(requestBytes);
			
		byte[] responseBytessrc = tcpClient.getData(sessionID);
		return responseBytessrc;
	
	}
	private static TCPClient getTCPClient(Address address,Integer timeout) throws IOException {
		TCPClient tcpClient= tcpClientCache.get(address);
		if (Objects.isNull(tcpClient)) {

			synchronized (lockHelper) {
				tcpClient = tcpClientCache.get(address);
				if (Objects.isNull(tcpClient)) {
					tcpClient = new TCPClient(address.getHost(), address.getPort(),timeout);
					tcpClientCache.put(address, tcpClient);
				}
			}

		}

		return tcpClient;
	}
}
