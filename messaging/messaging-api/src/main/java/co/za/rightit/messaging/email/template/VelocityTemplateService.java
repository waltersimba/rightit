package co.za.rightit.messaging.email.template;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityTemplateService implements TemplateService {

	private static final String TEMPLATE_EXTENSION = ".vm";
	private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
	private final String encoding;
	private final Path templateBasePath;
	
	public VelocityTemplateService(Path templateBasePath) {
		this(templateBasePath, null);
	}
	
	public VelocityTemplateService(Path templateBasePath, String encoding) {
		this.templateBasePath = templateBasePath;
		this.encoding = Optional.ofNullable(encoding).orElse(DEFAULT_ENCODING);
		initialiseTemplateEngine();
	}
	
	private void initialiseTemplateEngine() {
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateBasePath.toFile().getAbsolutePath());
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, Boolean.TRUE.toString());
		Velocity.init(properties);
	}
	
	public String generateContent(String templateName, Map<String, Object> variableMap) {
		VelocityContext ctx = new VelocityContext();
		for (Map.Entry<String, Object> data : variableMap.entrySet()) {
			ctx.put(data.getKey(), data.getValue());
		}
		Template template = Velocity.getTemplate(templateName + TEMPLATE_EXTENSION, encoding);
		StringWriter writer = new StringWriter();
		template.merge(ctx, writer);
		return writer.toString();
	}

}
