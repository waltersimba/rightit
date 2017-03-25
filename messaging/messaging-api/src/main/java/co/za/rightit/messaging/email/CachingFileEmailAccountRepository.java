package co.za.rightit.messaging.email;

import java.util.Optional;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CachingFileEmailAccountRepository implements EmailAccountRepository {

	private final LoadingCache<String, Optional<EmailAccount>> cache;
	
	public CachingFileEmailAccountRepository(EmailAccountRepository real, String cacheSpec) {
		cache = CacheBuilder.from(cacheSpec).build(new CacheLoader<String, Optional<EmailAccount>>(){

			@Override
			public Optional<EmailAccount> load(String key) throws Exception {
				return real.findEmailAccount(key);
			}
			
		});
	}
		
	@Override
	public Optional<EmailAccount> findEmailAccount(String domain) {
		return cache.getUnchecked(domain);
	}

}
