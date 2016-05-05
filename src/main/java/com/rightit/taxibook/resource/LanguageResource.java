package com.rightit.taxibook.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.rightit.taxibook.locale.Locales;

@Path("lang")
public class LanguageResource {

	private static final String SUPPORTED_LANGUAGES = "en_US,de,zh";
	
	@GET
	@Path("supported")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSupported() {
		return Response.ok(getSupportedLanguages()).build();
	}
	
	private List<SupportedLanguage> getSupportedLanguages() {
		List<SupportedLanguage> supportedLanguages = new ArrayList<>();
		for(String localeString : StringUtils.split(SUPPORTED_LANGUAGES, ",")) {
			final Locale locale = Locales.of(localeString);
			final String country = StringUtils.isEmpty(locale.getCountry()) ? null : locale.getCountry();
			supportedLanguages.add(new SupportedLanguage(localeString, locale.getLanguage(), 
					country, locale.getDisplayLanguage(locale)));
		}
		return supportedLanguages;
	}
	
	public static class SupportedLanguage {
		
		private String locale;
				
		private String language;
		
		private String country;
		
		private String displayLanguage;
		
		public SupportedLanguage(String locale, String language, String country, String displayLanguage) {
			this.locale = locale;
			this.language = language;
			this.country = country;
			this.displayLanguage = displayLanguage;
		}

		public String getLocale() {
			return locale;
		}

		public String getLanguage() {
			return language;
		}
		
		public String getCountry() {
			return country;
		}

		public String getDisplayLanguage() {
			return displayLanguage;
		}				
	}
	
}
