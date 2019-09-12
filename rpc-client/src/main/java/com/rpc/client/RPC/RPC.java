package com.rpc.client.RPC;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.ZooKeeper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.rpc.client.entity.Address;
import com.rpc.client.entity.ServiceEntity;
import com.rpc.client.loadBalance.LoadBalance;

public class RPC {
	//用来缓存服务的ip和端口
	public static Map<String,List<Address>> serviceCache = new HashMap<String,List<Address>>();
	public static ZooKeeper zooKeeper;
	public static Map<String, ServiceEntity> serviceList = new HashMap<>();
	public static boolean cache;
	public static Long timeout;
	public static Long FailureTime = 0l;
	public static LoadBalance loadBalance;
	/**
	 * 	初始化客户端配置文件
	 * 
	 * @param clientPath
	 * @throws Exception 
	 */
	public static void init(String clientPath) throws Exception {
		// 读取该服务的配置文件
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(clientPath));
		Element root = document.getRootElement();
		//zookeeper的地址
		Element zookeeperNode = root.element("zookeeper");
		zooKeeper = new ZooKeeper(zookeeperNode.attributeValue("ip"), 150000, null);
		//选择负载均衡策略
		Element LoadBalancingStrategyNode = root.element("LoadBalancingStrategy");
		String LoadBalancingStrategy = LoadBalancingStrategyNode.attributeValue("className");
		loadBalance = (LoadBalance) Class.forName(LoadBalancingStrategy).newInstance();	
		//配置的服务
		List<Element> serviceNodes = root.elements("service");
		
		for(Element serviceNode:serviceNodes){
			ServiceEntity entity = new ServiceEntity(serviceNode.attributeValue("name"), serviceNode.attributeValue("className"),Integer.parseInt(serviceNode.attributeValue("timeout")));
			serviceList.put(serviceNode.attributeValue("name"), entity);
		}
		//缓存解析
		Element cacheNode = root.element("cache");
		if(cacheNode.attributeValue("enable").equals("true")) {
			cache = true;
			timeout = Long.parseLong(cacheNode.attributeValue("timeout"));
		}else {
			cache = false;
		}
	}
	/**
	 * 	从注册中心中查找可以提供该服务的所有主机
	 * @param className
	 * @return
	 */
	public static List<Address> queryServiceHost(String className){
		List<Address> addresses = new ArrayList<>();
		try {
			//从注册中心中获得当前服务的所有主机
			List<String> childs = zooKeeper.getChildren("/RPC/Services/" + className, null);
			for(String child : childs) {
				String[] param = child.split(":");
				addresses.add(new Address(param[0],Integer.parseInt(param[1])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return addresses;
	}
}
