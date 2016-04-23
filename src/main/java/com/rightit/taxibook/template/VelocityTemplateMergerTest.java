package com.rightit.taxibook.template;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VelocityTemplateMergerTest {

	public static void main(String[] args) throws MergeException, URISyntaxException {
		Map<String, String> model = new HashMap<>();
		model.put("firstName", "Walter");
		model.put("verificationUrl", "www.taxibook.co.za/verify/d6087c23-3197-42f7-a6a4-a309b95ef2b6");
		model.put("daysBeforeExpiry", "5");
		model.put("generateEmailTokenUrl", "www.taxibook.co.za/verify/tokens/d6087c23-3197-42f7-a6a4-a309b95ef2b6");
		model.put("helpEmailAddress", "support@rightit.co.za");
		
		URL y = VelocityTemplateMergerTest.class.getResource("/META-INF/velocity");
		File x = new File(y.toURI());
		String path = x.getAbsolutePath();
		System.out.println(new VelocityTemplateMerger(path).mergeTemplateIntoString("VerifyMail", model));
	}

}
