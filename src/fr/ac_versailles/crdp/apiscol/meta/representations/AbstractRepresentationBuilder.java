package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.util.ArrayList;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.utils.FileUtils;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public abstract class AbstractRepresentationBuilder<T> implements
		IEntitiesRepresentationBuilder<T> {
	protected static final String SEARCH_ENGINE_CONCATENED_FIELDS_SEPARATOR = "~";
	protected static Logger logger;

	public AbstractRepresentationBuilder() {
		createLogger();
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());

	}

	@Override
	public String getMetadataDownloadUri(UriInfo uriInfo, String metadataId) {
		return String.format("%slom%s.xml", uriInfo.getBaseUri().toString(),
				FileUtils.getFilePathHierarchy("", metadataId));
	}

	@Override
	public String getMetadataSnippetUri(UriInfo uriInfo, String metadataId) {
		return String.format("%s/snippet", getMetadataUri(uriInfo, metadataId));
	}

	@Override
	public String getMetadataJsonpDownloadUri(UriInfo uriInfo, String metadataId) {
		return String.format("%slom%s.js", uriInfo.getBaseUri().toString(),
				FileUtils.getFilePathHierarchy("", metadataId));
	}

	protected String getMetadataAtomXMLUri(UriInfo uriInfo, String metadataId) {
		return String.format("%s?format=xml",
				getMetadataUri(uriInfo, metadataId));
	}

	@Override
	public String getMetadataUri(UriInfo uriInfo, String metadataId) {
		return String.format("%s%s", uriInfo.getBaseUri().toString(),
				metadataId);
	}

	public String getMetadataEditUri(String editUri, String metadataId) {
		return String.format("%smeta/%s", editUri, metadataId);
	}

	protected String getMetadataHTMLUri(UriInfo uriInfo, String metadataId) {
		return getMetadataUri(uriInfo, metadataId);
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

}
