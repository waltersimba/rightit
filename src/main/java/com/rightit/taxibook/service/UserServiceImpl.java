package com.rightit.taxibook.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.repository.Repository;

@Singleton
public class UserServiceImpl implements UserService {

	private Repository userRepository;
	
	@Inject
	public UserServiceImpl(Repository userRepository) {
		this.userRepository = userRepository;
	};
	
	@Override
	public boolean createNewUser(User newUser) {
	
		return false;
	}

}
