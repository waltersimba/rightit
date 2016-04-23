package com.rightit.taxibook;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class TestDir {

	public static void main(String[] args) throws URISyntaxException {
		URL y = TestDir.class.getResource("/META-INF/velocity");
		File x = new File(y.toURI());
		System.out.println(x.getAbsolutePath());
	}

}
