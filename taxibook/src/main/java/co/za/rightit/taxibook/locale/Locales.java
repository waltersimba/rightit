package co.za.rightit.taxibook.locale;

import java.util.Locale;

public class Locales {

	private Locales() {};
	
	public static Locale of(String locale) {
		return LanguageUtil.LOCALE_CACHE.getUnchecked(locale);
	}
}
