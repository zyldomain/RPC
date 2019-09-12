package com.rpc.server.entity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.ZooKeeper;

/**
 * 服务端全局参数
 * @author zhao
 *
 */
public class Global {
	
	private Global(){
		methodCache = new ConcurrentHashMap<String,Method>();
	}
	
	private static class SingleHolder{
		private static final Global INSTANCE = new Global();
	}
	
	/**
	 * 单例
	 * @return
	 */
	public static Global getInstance(){
		return SingleHolder.INSTANCE;
	}
	//zookeeper的ip和port
	private String zookeeperIp;
	private Integer zookeeperPort;
	private ZooKeeper zooKeeper;
	
	//netty接受缓冲区大小
	private Integer MaxBuf = 1024;
	
	//服务应用名字
	private  String serviceName;
	
	//网络连接的一些配置
	private  String ip;
	private  Integer port;
	private  Integer timeout;
	
	//服务缓存
	private  Map<String,Object> serviceImpl;
	private List<String> serviceList;
	//服务实现类缓存
	private Map<String,Class> serviceClass;
	
	private Map<String,Method> methodCache;
	
	private ClassLoader classLoader;
	
	public String getServiceName() {
		return serviceName;
	}

	public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	public List<String> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	public String getZookeeperIp() {
		return zookeeperIp;
	}

	public void setZookeeperIp(String zookeeperIp) {
		this.zookeeperIp = zookeeperIp;
	}

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZooKeeper(ZooKeeper zooKeeper) {
		this.zooKeeper = zooKeeper;
	}

	public Integer getZookeeperPort() {
		return zookeeperPort;
	}

	public void setZookeeperPort(Integer zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public Map<String, Object> getServiceImpl() {
		return serviceImpl;
	}
	
	public Object getServiceImpl(String key){
		return serviceImpl.get(key);
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public void setServiceImpl(Map<String, Object> serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	public Map<String, Class> getServiceClass() {
		return serviceClass;
	}
	
	public Class getServiceClass(String key){
		return serviceClass.get(key);
	}

	public void setServiceClass(Map<String, Class> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public Integer getMaxBuf() {
		return MaxBuf;
	}

	public void setMaxBuf(Integer maxBuf) {
		MaxBuf = maxBuf;
	}

	public Method getMethod(String serviceName,String methodName,List<String> paramsTypesName){
		
		return this.methodCache.get(buildKey(serviceName, methodName, paramsTypesName));
	}
	
	public void putMethod(String serviceName,String methodName,List<String> paramsTypesName,Method method){
		this.methodCache.put(buildKey(serviceName, methodName, paramsTypesName), method);
	}
	
	private String buildKey(String serviceName,String methodName,List<String> paramsTypesName){
		StringBuilder methodKey = new StringBuilder(serviceName);
		methodKey.append("-").append(methodName);
		for(String s:paramsTypesName){
			methodKey.append("-").append(s);
		}
		
		return methodKey.toString();		
	}
}
