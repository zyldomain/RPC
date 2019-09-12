package com.rpc.server.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.zookeeper.ZooKeeper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.rpc.server.entity.Global;

public class LoadConfigure {
	/**
	 *   	加载服务端的配置文件
	 * @param serviceRootPath
	 * @throws Exception
	 */
	public static void load(String serviceRootPath) throws Exception {

		String serviceLib = serviceRootPath + File.separator + "lib";
		String serviceConf = serviceRootPath + File.separator + "conf";

		// 读取该服务的配置文件
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(serviceConf + File.separator + "service.xml"));
		document.setXMLEncoding("UTF-8");
		Element node = document.getRootElement();
		Element zookeeperNode = node.element("zookeeper");
		
		Global.getInstance().setZookeeperIp(zookeeperNode.attributeValue("ip"));
		Global.getInstance().setZooKeeper(new ZooKeeper(Global.getInstance().getZookeeperIp(), 15000, null));
		Element proNode = node.element("property");
		
		Element connectionNode = proNode.element("connection");
		Element nettyNode = proNode.element("netty");
		
		Global.getInstance().setMaxBuf(Integer.parseInt(nettyNode.attributeValue("maxBuf")));
		
		Global.getInstance().setIp(connectionNode.attributeValue("ip"));
		
		if(Global.getInstance().getPort()==null) {
			Global.getInstance().setPort(Integer.parseInt(connectionNode.attributeValue("port")));
		}else {
			connectionNode.setAttributeValue("port", String.valueOf(Global.getInstance().getPort()));
			FileOutputStream fos = new FileOutputStream(serviceConf + File.separator + "service.xml");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			OutputFormat of = new OutputFormat();
			of.setEncoding("UTF-8");
			XMLWriter write =new XMLWriter(osw,of);
			write.write(document);
			write.close();
		}
		
		
		Global.getInstance().setTimeout(Integer.parseInt(connectionNode.attributeValue("timeout")));

		Map<String, String> serviceMap = new HashMap<String, String>();
		Element servicesNode = node.element("services");

		List<Element> serviceList = servicesNode.elements("service");
		for (Element e : serviceList) {
			serviceMap.put(e.attributeValue("name"), e.attributeValue("impl"));
		}
		Global.getInstance().setServiceList(new ArrayList<>(serviceMap.values()));
		initService(serviceMap, serviceLib);

	}
	/**
	 * 	初始化指定的服务
	 * @param services
	 * @param serviceLibPath
	 * @throws Exception
	 */
	private static void initService(Map<String, String> services, String serviceLibPath) throws Exception {
		//jar包的目录
		File serviceLibDir = new File(serviceLibPath);
		//过滤出来所有的jar包文件
		File[] jarFiles = serviceLibDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		//获得所有jar包的位置
		URL[] jarURLS = new URL[jarFiles.length];
		for (int i = 0; i < jarFiles.length; i++) {
			jarURLS[i] = jarFiles[i].toURI().toURL();
		}

		//实例化类加载器
		URLClassLoader classLoader = new URLClassLoader(jarURLS, ClassLoader.getSystemClassLoader());
		
		//保存实例
		Map<String,Object> instances = new HashMap<String,Object>();
		//保存class对象
		Map<String,Class> types = new HashMap<String,Class>();
		//遍历所有的服务
		Iterator<Entry<String, String>> it = services.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> e = it.next();
			Class clazz = classLoader.loadClass(e.getValue());
			instances.put(e.getKey(), clazz.newInstance());
			types.put(e.getKey(), clazz);
		}
		
		Global.getInstance().setClassLoader(classLoader);
		Global.getInstance().setServiceImpl(instances);
		Global.getInstance().setServiceClass(types);
	}

}
