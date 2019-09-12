package com.rpc.service.Impl;

import com.rpc.client.pojo.Person;
import com.rpc.service.PersonService;

public class PersonServiceImpl implements PersonService {

	@Override
	public void say(Person person) {
		System.out.println(person.getName());
		
	}

}
