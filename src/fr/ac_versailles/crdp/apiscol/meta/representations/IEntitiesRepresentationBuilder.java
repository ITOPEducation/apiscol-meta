package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;

public interface IEntitiesRepresentationBuilder<T> {

	MediaType getMediaType();

	String getMetadataDownloadUri(UriInfo uriInfo, String metadataId);

	T getMetadataSuccessfulDestructionReport(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId, String warnings);

	T getSuccessfullOptimizationReport(String requestedFormat, UriInfo uriInfo);

	String getMetadataUri(UriInfo uriInfo, String metadataId);

	T getSuccessfulGlobalDeletionReport();

	String getMetadataJsonpDownloadUri(UriInfo uriInfo, String metadataId);

	T getMetadataSnippetRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId, String version);

	String getMetadataSnippetUri(UriInfo uriInfo, String metadataId);

	T selectMetadataFollowingCriterium(UriInfo uriInfo,
			String apiscolInstanceName, String apiscolInstanceLabel,
			ISearchEngineResultHandler handler, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException;

	T getMetadataRepresentation(UriInfo uriInfo, String apiscolInstanceName,
			String resourceId, boolean includeDescription,
			Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException, DBAccessException;

	T getCompleteMetadataListRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String apiscolInstanceLabel, int start,
			int rows, boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException;

	Object getMaintenanceRecoveryRepresentation(Integer maintenanceRecoveryId,
			UriInfo uriInfo, MaintenanceRegistry maintenanceRegistry);

}
