package co.za.rightit.taxibook.locale;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LanguageUtil {

	private LanguageUtil() {};
	
	public static final LoadingCache<String, Locale> LOCALE_CACHE = CacheBuilder.newBuilder()
	        .build(new CacheLoader<String, Locale>() {
	            @Override
	            public Locale load(String key) throws Exception {
	                return LocaleUtils.toLocale(key);
	            }
	        });
}
