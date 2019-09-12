package com.rpc.client.loadBalance;

import java.util.List;

import com.rpc.client.entity.Address;

public interface LoadBalance {
	Address loadbalance(List<Address> addresses);
}
