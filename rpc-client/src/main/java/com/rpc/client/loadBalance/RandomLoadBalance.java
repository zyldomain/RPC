package com.rpc.client.loadBalance;

import java.util.List;

import com.rpc.client.entity.Address;

public class RandomLoadBalance implements LoadBalance{
	public Address loadbalance(List<Address> addresses){
		int total = addresses.size();
		int index = (int) (System.currentTimeMillis()%total);
		
		return addresses.get(index);
	}
}
