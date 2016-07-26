package co.za.rightit.taxibook.template;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityTemplateMerger implements TemplateMerger {

	public VelocityTemplateMerger(String directoryName) {
		this.init(directoryName);
	}

	@Override
	public String mergeTemplateIntoString(String templateName, Map<String, ?> dataObjects) throws MergeException {
		try {
			Template template = Velocity.getTemplate(templateName + ".vm");

			VelocityContext ctx = fillContext(dataObjects);

			StringWriter out = new StringWriter();
			template.merge(ctx, out);
			
			return out.toString();
		} catch (ResourceNotFoundException e) {
			throw new MergeException(e.getMessage());
		}
	}

	protected VelocityContext fillContext(Map<String, ?> dataObjects) {
		VelocityContext ctx = new VelocityContext();
		for (Map.Entry<String, ?> data : dataObjects.entrySet()) {
			ctx.put(data.getKey(), data.getValue());
		}
		return ctx;
	}
	
	private void init(String directoryName) {
		File dir = new File(directoryName);
		if (dir == null || !dir.exists() || !dir.isDirectory() || !dir.canRead()) {
			throw new RuntimeException("Template directory " + directoryName + " is invalid.");
		}

		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, directoryName);
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, Boolean.TRUE.toString());
		Velocity.init(properties);
	}

}
