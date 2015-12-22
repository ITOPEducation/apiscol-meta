package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;

public interface IEntitiesRepresentationBuilder<T> {

	MediaType getMediaType();

	String getMetadataDownloadUri(URI baseUri, String metadataId);

	T getMetadataSuccessfulDestructionReport(URI baseUri,
			String apiscolInstanceName, String metadataId, String warnings);

	T getSuccessfullOptimizationReport(String requestedFormat, URI baseUri);

	String getMetadataUri(URI baseUri, String metadataId);

	T getSuccessfulGlobalDeletionReport();

	String getMetadataJsonpDownloadUri(URI baseUri, String metadataId);

	T getMetadataSnippetRepresentation(URI baseUri, String apiscolInstanceName,
			String metadataId, String version);

	String getMetadataSnippetUri(URI baseUri, String metadataId);

	T selectMetadataFollowingCriterium(URI baseUri, String requestPath,
			String apiscolInstanceName, String apiscolInstanceLabel,
			ISearchEngineResultHandler handler, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException;

	T getMetadataRepresentation(URI baseUri, String apiscolInstanceName,
			String resourceId, boolean includeDescription,
			boolean includeHierarchy, Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException, DBAccessException;

	T getCompleteMetadataListRepresentation(URI baseUri, String requestPath,
			String apiscolInstanceName, String apiscolInstanceLabel, int start,
			int rows, boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException;

	Object getMaintenanceRecoveryRepresentation(Integer maintenanceRecoveryId,
			URI baseUri, MaintenanceRegistry maintenanceRegistry,
			Integer nbLines);

}
