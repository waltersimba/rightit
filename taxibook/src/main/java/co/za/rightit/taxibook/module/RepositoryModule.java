package co.za.rightit.taxibook.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import co.za.rightit.commons.repository.Repository;
import co.za.rightit.taxibook.domain.User;
import co.za.rightit.taxibook.domain.VerificationToken;
import co.za.rightit.taxibook.repository.UseRepository;
import co.za.rightit.taxibook.repository.VerificationTokenRepository;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Repository<User>>() {}).to(UseRepository.class);
		bind(new TypeLiteral<Repository<VerificationToken>>() {}).to(VerificationTokenRepository.class);		
	}

}
