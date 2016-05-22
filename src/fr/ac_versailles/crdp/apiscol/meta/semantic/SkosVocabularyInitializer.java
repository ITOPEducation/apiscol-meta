package fr.ac_versailles.crdp.apiscol.meta.semantic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

		InputSource is = new InputSource(reader);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			logger.info("Error while loading skos file " + e.getMessage());
		}
		logger.info("Skos file succesfully read");
		return doc;
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
