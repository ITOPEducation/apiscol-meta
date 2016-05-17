package fr.ac_versailles.crdp.apiscol.meta.semantic;

import org.jdom.Document;

public class SkosVocabulary {
	private Document skosXml;

	public static final String ENVIRONMENT_PARAMETER_KEY = "skos-vocabulary";

	public SkosVocabulary(Document skosXml) {
		this.skosXml = skosXml;
	}

	public String getPrefLabelForUri(String string) {
		// TODO get preflabel by xpath
		return "";
	}

}
