package fr.ac_versailles.crdp.apiscol.meta.representations;

public class SemanticUriProvider {
	public static final String MIME_TYPE_VOCABULARY = "MIME_TYPE_VOCABULARY";
	private static final String MIME_TYPE_VOCABULARY_ROOT = "http://purl.org/NET/mediatypes/";

	public static String convertToUri(String vocabulary, String value) {
		switch (vocabulary) {
		case MIME_TYPE_VOCABULARY:
			return new StringBuilder().append(MIME_TYPE_VOCABULARY_ROOT)
					.append(value).toString();
		default:
			throw new RuntimeException("Unknown wocabulary " + vocabulary);
		}
	}

}
