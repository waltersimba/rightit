package co.za.rightit.messaging.email.template;

import java.util.Map;

public interface TemplateService {
	String generateContent(String template, Map<String, Object> variableMap);
}
