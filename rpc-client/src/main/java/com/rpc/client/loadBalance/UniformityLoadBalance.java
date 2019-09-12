package com.rpc.client.loadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.rpc.client.entity.Address;

public class UniformityLoadBalance implements LoadBalance{
private static AtomicInteger count = new AtomicInteger(0);
	
	
	public Address loadbalance(List<Address> addresses) {
		int total = addresses.size();
		return addresses.get(count.getAndIncrement()%total);

		
	}
}
