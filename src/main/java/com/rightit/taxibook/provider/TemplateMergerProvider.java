package com.rightit.taxibook.provider;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Provider;

import com.rightit.taxibook.template.TemplateMerger;
import com.rightit.taxibook.template.VelocityTemplateMerger;

public class TemplateMergerProvider implements Provider<TemplateMerger> {

	private TemplateMerger templateMerger;
	
	public TemplateMergerProvider() {
		templateMerger = new VelocityTemplateMerger(getTemplateDir());
	}
	
	@Override
	public TemplateMerger get() {
		return templateMerger;
	}
	
	private String getTemplateDir() {
		URL templateDirUrl = TemplateMergerProvider.class.getResource("/META-INF/velocity");
		File templateDir;
		try {
			templateDir = new File(templateDirUrl.toURI());
			return templateDir.getAbsolutePath();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}		
	}

}
