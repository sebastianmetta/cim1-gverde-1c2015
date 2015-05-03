package ar.uba.fi.cim.app;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.uba.fi.cim.files.FilesProcessor;

public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("/application-context.xml");
		FilesProcessor process = (FilesProcessor) appContext.getBean("filesProccessorBean");
		process.startProcessing();
		appContext.close();
	}

}
