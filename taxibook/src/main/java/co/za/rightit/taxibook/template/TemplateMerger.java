package co.za.rightit.taxibook.template;

import java.util.Map;

public interface TemplateMerger {

	 /**
     * Merge a group of data objects into a template, specified by name.
     * @param templateName name of an object (e.g. file) which contains the template to be filled
     * @return Merged data
     * @throws MergeException if the merge fails for any reason (e.g. IOException reading a file).
     */
    String mergeTemplateIntoString(String templateName, Map<String, ?> dataObjects) throws MergeException;
}
