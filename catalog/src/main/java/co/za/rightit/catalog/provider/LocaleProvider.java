package co.za.rightit.catalog.provider;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class LocaleProvider implements Provider<Locale> {

	private static final String LOCALE_PARAMETER = "locale";
	private static final String EMPTY_STRING = "";
	private final HttpServletRequest request;

	@Inject
	public LocaleProvider(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Locale get() {
		Locale locale = null;
		Optional<String> optionalLocale = Optional.ofNullable(request.getParameter(LOCALE_PARAMETER));
		if (optionalLocale.isPresent()) {
			Locale specifiedLocale = Locale.forLanguageTag(optionalLocale.get());
			locale = EMPTY_STRING.equals(specifiedLocale.toString()) ? request.getLocale() : specifiedLocale;
		} else {
			locale = request.getLocale();
		}
		return locale;
	}

}
