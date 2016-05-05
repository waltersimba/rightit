package com.rightit.taxibook.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.rightit.taxibook.domain.User;
import com.rightit.taxibook.domain.VerificationToken;
import com.rightit.taxibook.repository.Repository;
import com.rightit.taxibook.repository.UseRepository;
import com.rightit.taxibook.repository.VerificationTokenRepository;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Repository<User>>() {}).to(UseRepository.class);
		bind(new TypeLiteral<Repository<VerificationToken>>() {}).to(VerificationTokenRepository.class);		
	}

}
