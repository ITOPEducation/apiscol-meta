package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.semantic.SkosVocabulary;
import fr.ac_versailles.crdp.apiscol.utils.FileUtils;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public abstract class AbstractRepresentationBuilder<T> implements
		IEntitiesRepresentationBuilder<T> {
	protected static final String SEARCH_ENGINE_CONCATENED_FIELDS_SEPARATOR = "~";
	protected static Logger logger;
	protected SkosVocabulary skosVocabulary;

	public AbstractRepresentationBuilder() {
		createLogger();
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());

	}

	@Override
	public String getMetadataDownloadUri(URI baseUri, String metadataId) {
		return String.format("%s/lom%s.xml", baseUri.toString(),
				FileUtils.getFilePathHierarchy("", metadataId));
	}

	@Override
	public String getMetadataSnippetUri(URI baseUri, String metadataId) {
		return String.format("%s/snippet", getMetadataUri(baseUri, metadataId));
	}

	@Override
	public String getMetadataJsonpDownloadUri(URI baseUri, String metadataId) {
		return String.format("%s/lom%s.js", baseUri.toString().toString(),
				FileUtils.getFilePathHierarchy("", metadataId));
	}

	protected String getMetadataAtomXMLUri(URI baseUri, String metadataId) {
		return String.format("%s?format=xml",
				getMetadataUri(baseUri, metadataId));
	}

	@Override
	public String getMetadataUri(URI baseUri, String metadataId) {
		return String.format("%s/%s", baseUri.toString(), metadataId);
	}

	public String getMetadataEditUri(String editUri, String metadataId) {
		return String.format("%s/meta/%s", editUri, metadataId);
	}

	protected String getMetadataHTMLUri(URI baseUri, String metadataId) {
		return getMetadataUri(baseUri, metadataId);
	}

	protected String getMetadataUrn(String metadataId,
			String apiscolInstanceName) {
		return String.format("urn:apiscol:%s:meta:metadata:%s",
				apiscolInstanceName, metadataId);
	}

	protected String getEtagForMetadata(String metadataId)
			throws MetadataNotFoundException {
		return ResourceDirectoryInterface.getTimeStamp(metadataId);
	}

	protected ArrayList<String> getMetadataList() {
		return ResourceDirectoryInterface.getMetadataList();
	}

	protected Object getUrlForMaintenanceRecovery(UriBuilder uriBuilder,
			Integer maintenanceRecoveryIdentifier) {
		return uriBuilder.path("maintenance/recovery")
				.path(maintenanceRecoveryIdentifier.toString()).build();

	}
	
	public void setSkosVocabulary(SkosVocabulary skosVocabulary) {
		this.skosVocabulary = skosVocabulary;
	}

}
