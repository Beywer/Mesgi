package ru.beywer.tester;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	
	public void start(BundleContext context) throws Exception {

		String karafFolder = System.getProperty("user.dir");
		System.out.println(karafFolder);
		File file = new File(karafFolder + "/mesgi.properties");
		System.out.println(file);
		FileInputStream stream = new FileInputStream(file);
		System.out.println(stream);
		
		Properties properties = new Properties();
		properties.load(stream);
		System.out.println(stream);
		
		System.out.println(properties.get("ip"));
		
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Tester stopped");
		
	}

}
