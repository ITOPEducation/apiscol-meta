package fr.ac_versailles.crdp.apiscol.meta.semantic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fr.ac_versailles.crdp.apiscol.meta.resources.ResourcesLoader;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public class SkosVocabularyInitializer implements ServletContextListener {

	private Logger logger;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// nothing
	}

	private Document loadSkosXml() {
		InputStream stream = ResourcesLoader.loadResource("skos/scolomfr.skos");
		Reader reader = new BufferedReader(new InputStreamReader(stream));

		SAXBuilder builder = new SAXBuilder();
		Document skosXml;
		try {
			skosXml = builder.build(reader);
		} catch (JDOMException | IOException e) {
			throw new RuntimeException(
					"Impossible to read scolomfr skos file :" + e.getMessage());
		}
		logger.info("Skos file succesfully read");
		return skosXml;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		createLogger();
		Document skosXml = loadSkosXml();
		ServletContext context = servletContextEvent.getServletContext();
		SkosVocabulary skosVocabulary = new SkosVocabulary(skosXml);
		context.setAttribute(SkosVocabulary.ENVIRONMENT_PARAMETER_KEY,
				skosVocabulary);

	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());
	}
}
