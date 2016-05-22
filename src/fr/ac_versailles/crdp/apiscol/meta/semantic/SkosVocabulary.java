package fr.ac_versailles.crdp.apiscol.meta.semantic;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import fr.ac_versailles.crdp.apiscol.UsedNamespaces;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public class SkosVocabulary {
	private Document skosXml;

	private Logger logger;

	private XPath xpath;

	public static final String ENVIRONMENT_PARAMETER_KEY = "skos-vocabulary";

	private static NamespaceContext ctx = new NamespaceContext() {
		public String getNamespaceURI(String prefix) {
			String uri;
			if (prefix.equals(UsedNamespaces.RDF.getShortHand()))
				uri = UsedNamespaces.RDF.getUri();
			else if (prefix.equals(UsedNamespaces.SKOS.getShortHand())) {
				uri = UsedNamespaces.SKOS.getUri();
			} else
				uri = null;
			return uri;
		}

		public Iterator getPrefixes(String val) {
			return null;
		}

		public String getPrefix(String uri) {
			return null;
		}
	};

	public SkosVocabulary(Document skosXml) {
		this.skosXml = skosXml;
		createLogger();
		createXpath();
	}

	private void createXpath() {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(ctx);

	}

	public String getPrefLabelForUri(final String uri) {
		XPathExpression exp = null;
		String prefLabel = "";
		try {
			exp = xpath.compile("//rdf:Description[@rdf:about='" + uri
					+ "']/skos:prefLabel/text()");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		try {
			prefLabel = (String) exp.evaluate(skosXml, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			logger.error("Error while trying to fetch preflabel for uri " + uri
					+ " with message " + e.getMessage());
		}
		return prefLabel;
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());
	}

}
