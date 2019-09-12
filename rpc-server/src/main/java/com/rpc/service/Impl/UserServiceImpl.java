package com.rpc.service.Impl;

import com.rpc.service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public String sayHello(String name) {
		System.out.println("Hello " + name );
		return name + "调用成功";
	}
	
}
