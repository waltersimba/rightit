package co.za.rightit.commons.utils.guice;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

public class TypeMatchers {

	private static final Logger LOGGER = LoggerFactory.getLogger(TypeMatchers.class);
	
	private static class SubClassesOf extends AbstractMatcher<TypeLiteral<?>> {
		private final Class<?> baseClass;

		private SubClassesOf(Class<?> baseClass) {
			this.baseClass = baseClass;
		}

		@Override
		public boolean matches(TypeLiteral<?> t) {
			return baseClass.isAssignableFrom(t.getRawType());
		}
	}

	private static class AnnotatedWith extends AbstractMatcher<TypeLiteral<?>> {
		private final Class<? extends Annotation> baseClass;

		private AnnotatedWith(Class<? extends Annotation> baseClass) {
			this.baseClass = baseClass;
		}

		@Override
		public boolean matches(TypeLiteral<?> t) {
			try {
				return t.getRawType().isAnnotationPresent(baseClass);
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage(), ex);
				return false;
			}
		}
	}

	/**
	 * Matcher matches all classes that extends, implements or is the same as
	 * baseClass
	 * 
	 * @param baseClass
	 * @return Matcher
	 */
	public static Matcher<TypeLiteral<?>> subclassesOf(Class<?> baseClass) {
		return new SubClassesOf(baseClass);
	}

	public static Matcher<TypeLiteral<?>> annotatedWith(Class<? extends Annotation> aClass) {
		return new AnnotatedWith(aClass);
	}

}
