package fr.ac_versailles.crdp.apiscol.meta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataKeySyntax {
	private final static String uidPortion = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";
	private final static Pattern apiscolMetadataIdSchema = Pattern
			.compile(String.format("^(%s)$", uidPortion));
	private final static Pattern apiscolMetadataUrlSchema = Pattern
			.compile(String.format("^https?://.*/meta/(%s)$", uidPortion));

	public static boolean metadataIdIsCorrect(String resourceId) {
		Matcher matcher = apiscolMetadataIdSchema.matcher(resourceId);
		return matcher.matches();
	}

	public static String extractMetadataIdFromUrl(String url) {
		Matcher matcher = apiscolMetadataUrlSchema.matcher(url);
		boolean matchFound = matcher.find();
		if (matchFound) {
			return matcher.group(1);
		}
		return null;
	}
}
