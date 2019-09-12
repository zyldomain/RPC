package com.rpc.server.core;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import com.rpc.server.entity.Global;
/**
 * 将自己能够加载的服务放到zookeeper上面
 * @author zyldo
 *
 */
public class SendToZookeeper {
	public static ZooKeeper zooKeeper;
	public static Global instance;
	public static void init() {
		existServiceNode("/RPC");
		existServiceNode("/RPC/Services");
	}
	public static void send() throws Exception{
		for(String service : instance.getServiceList()) {
			String path = "/RPC/Services/" + service;
			existServiceNode(path);
			path += "/" + instance.getIp() + ":" + instance.getPort();
			existServiceIpAndPortNode(path);
		}
	}
	private static void existServiceNode(String path) {
		zooKeeper.exists(path, false, exeistServiceNodeCallback, null);
	}
	private static void createServiceNode(String path) {
		zooKeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,createServiceNodeCallback,null);
	}
	private static void existServiceIpAndPortNode(String path) {
		zooKeeper.exists(path, false, exeistServiceNodeCallback, null);
	}
	private static void createServiceIpAndPortNode(String path) {
		zooKeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,createServiceIpAndPortNodeCallback,null);
	}
	//服务节点是否存在
	static StatCallback exeistServiceNodeCallback = new StatCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				existServiceNode(path);
				break;
			case OK:
				if(stat == null) {
					createServiceNode(path);
				}
			case NONODE:
				if(stat == null) {					
					createServiceNode(path);
				}
				break;
			default:
				break;
			
			}
		}
	};
	static StringCallback createServiceNodeCallback = new StringCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				createServiceNode(path);
				break;
			default:
				break;
			}
			
		}
	};
	
	//服务节点是否存在
	static StatCallback existServiceIpAndPortNodeCallback = new StatCallback() {
			
			@Override
			public void processResult(int rc, String path, Object ctx, Stat stat) {
				switch (Code.get(rc)) {
				case CONNECTIONLOSS:
					existServiceIpAndPortNode(path);
					break;
				case OK:
					System.out.println("456");
					if(stat == null) {
						createServiceIpAndPortNode(path);
					}
				case NONODE:
					System.out.println("456");
					if(stat == null) {
						createServiceIpAndPortNode(path);
					}
					break;
				default:
					break;
				
				}
			}
		};
	static StringCallback createServiceIpAndPortNodeCallback = new StringCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				createServiceIpAndPortNode(path);
				break;
			default:
				break;
			}
			
		}
	};
	
}
