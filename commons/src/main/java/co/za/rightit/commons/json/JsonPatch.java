package co.za.rightit.commons.json;

public class JsonPatch {
	/**
	 * Operation
	 */
	private String op;
	private String path;
	private Object value;
	
	public static enum Operation {
		ADD("add"),
		REMOVE("remove"),
		REPLACE("replace");
		
		private String name;
		
		private Operation(String name) {
			this.name = name;
		}
	}
	
}
