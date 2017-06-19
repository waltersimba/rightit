package co.za.rightit.messaging.email.template;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import co.za.rightit.messaging.email.EmailAccount;
import co.za.rightit.messaging.email.EmailAccountRepository;

public class TemplateServiceRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceRepository.class);
	private final LoadingCache<String, TemplateService> cache;
	private final EmailAccountRepository emailAccountRepository;
	
	public TemplateServiceRepository(String cacheSpec, EmailAccountRepository emailAccountRepository) {
		this(CacheBuilder.from(cacheSpec), emailAccountRepository);
	}
	
	@SuppressWarnings("unchecked")
	public TemplateServiceRepository(CacheBuilder builder, EmailAccountRepository emailAccountRepository) {
		this.emailAccountRepository = emailAccountRepository;
		cache = builder.build(new CacheLoader<String, TemplateService>(){

			@Override
			public TemplateService load(String domain) throws Exception {
				Optional<EmailAccount> optional = emailAccountRepository.findEmailAccount(domain);
				if(optional.isPresent()) {
					String path = optional.get().getTemplatePath();
					LOGGER.debug("creating template service, domain={}, template path={}", domain, path);
					Path templateBasePath = Paths.get(path);
					return new VelocityTemplateService(templateBasePath);
				} else {
					throw new IllegalStateException(String.format("Email account with domain=%s not found", domain));
				}
			}
		});
	}
	
	public TemplateService getTemplateService(String domain) {
		return cache.getUnchecked(domain);
	}
	
}
