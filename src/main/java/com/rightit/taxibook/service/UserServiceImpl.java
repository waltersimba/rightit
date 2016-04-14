package com.rightit.taxibook.service;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.validation.Validator;

import com.rightit.taxibook.repository.UserRepository;

@Singleton
public class UserServiceImpl extends AbstractService implements UserService {

	private UserRepository userRepository;
	
	@Inject
	public UserServiceImpl(UserRepository userRepository, Provider<Validator> validatorProvider) {
		super(validatorProvider.get());
		this.userRepository = userRepository;
	};
	
	@Override
	public boolean createNewUser(CreateUserRequest request) {
		validate(request);
		return false;
	}

}
