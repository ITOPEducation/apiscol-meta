package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;

import com.sun.jersey.api.json.JSONWithPadding;

import fr.ac_versailles.crdp.apiscol.CustomMediaType;
import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;
import fr.ac_versailles.crdp.apiscol.utils.JSonUtils;
import fr.apiscol.metadata.scolomfr3utils.Scolomfr3Utils;

public class JSonPRepresentationBuilder extends
		AbstractRepresentationBuilder<JSONWithPadding> {
	private AbstractRepresentationBuilder<Document> innerBuilder;

	@Override
	public MediaType getMediaType() {
		return CustomMediaType.JSONP;
	}

	public JSonPRepresentationBuilder() {
		innerBuilder = new XMLRepresentationBuilder();
	}

	@Override
	public JSONWithPadding getMetadataRepresentation(URI baseUri,
			String apiscolInstanceName, String resourceId,
			boolean includeDescription, boolean includeHierarchy, boolean includeTimestamp, int maxDepth,
			Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException, DBAccessException {

		Document xmlRepresentation = innerBuilder.getMetadataRepresentation(
				baseUri, apiscolInstanceName, resourceId, includeDescription,
				includeHierarchy, includeTimestamp, maxDepth, params, resourceDataHandler,
				editUri);
		String jsonSource = JSonUtils.convertXMLToJson(xmlRepresentation);
		JSONWithPadding metadataResponseJson = new JSONWithPadding(jsonSource,
				"callback");
		return metadataResponseJson;
	}

	@Override
	public JSONWithPadding getMetadataSnippetRepresentation(URI baseUri,
			String apiscolInstanceName, String metadataId, String version) {
		Document xmlRepresentation = (Document) innerBuilder
				.getMetadataSnippetRepresentation(baseUri, apiscolInstanceName,
						metadataId, version);
		String jsonSource = JSonUtils.convertXMLToJson(xmlRepresentation);
		JSONWithPadding metadataResponseJson = new JSONWithPadding(jsonSource,
				"callback");
		return metadataResponseJson;
	}

	@Override
	public JSONWithPadding getMetadataSuccessfulDestructionReport(URI baseUri,
			String apiscolInstanceName, String metadataId, String warnings) {
		return null;
	}

	@Override
	public JSONWithPadding getSuccessfullOptimizationReport(
			String requestedFormat, URI baseUri) {
		return null;
	}

	@Override
	public JSONWithPadding getSuccessfulGlobalDeletionReport() {
		return null;
	}

	@Override
	public JSONWithPadding selectMetadataFollowingCriterium(URI baseUri,
			String requestPath, String apiscolInstanceName,
			String apiscolInstanceLabel, ISearchEngineResultHandler handler,
			int start, int rows, boolean includeDescription, boolean includeHierarchy,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException {
		return null;
	}

	@Override
	public JSONWithPadding getCompleteMetadataListRepresentation(URI baseUri,
			String requestPath, String apiscolInstanceName,
			String apiscolInstanceLabel, int start, int rows,
			boolean includeDescription, boolean includeHierarchy,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException {
		return null;
	}

	@Override
	public Object getMaintenanceRecoveryRepresentation(
			Integer maintenanceRecoveryId, URI baseUri,
			MaintenanceRegistry maintenanceRegistry, Integer nbLines) {
		return null;
	}

	@Override
	public void setScolomfrUtils(Scolomfr3Utils scolomfrUtils) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addWarningMessages(JSONWithPadding metadataRepresentation,
			List<String> warningMessages) {
		// TODO Auto-generated method stub

	}
}
