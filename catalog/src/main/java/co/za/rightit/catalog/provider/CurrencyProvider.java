package co.za.rightit.catalog.provider;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CurrencyProvider implements Provider<Optional<Currency>> {

	private final Locale locale;

	@Inject
	public CurrencyProvider(Provider<Locale> localeProvider) {
		this.locale = localeProvider.get();
	}

	@Override
	public Optional<Currency> get() {
		try {
			return Optional.ofNullable(Currency.getInstance(locale));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

}
