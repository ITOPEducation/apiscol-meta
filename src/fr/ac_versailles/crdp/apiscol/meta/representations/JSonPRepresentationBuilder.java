package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.w3c.dom.Document;

import com.sun.jersey.api.json.JSONWithPadding;

import fr.ac_versailles.crdp.apiscol.CustomMediaType;
import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;
import fr.ac_versailles.crdp.apiscol.utils.JSonUtils;

public class JSonPRepresentationBuilder extends
		AbstractRepresentationBuilder<JSONWithPadding> {
	private XMLRepresentationBuilder innerBuilder;

	@Override
	public MediaType getMediaType() {
		return CustomMediaType.JSONP;
	}

	public JSonPRepresentationBuilder() {
		innerBuilder = new XMLRepresentationBuilder();
	}

	@Override
	public JSONWithPadding getMetadataRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String resourceId,
			boolean includeDescription, Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException, DBAccessException {

		Document xmlRepresentation = innerBuilder.getMetadataRepresentation(
				uriInfo, apiscolInstanceName, resourceId, includeDescription,
				params, resourceDataHandler, editUri);
		String jsonSource = JSonUtils.convertXMLToJson(xmlRepresentation);
		JSONWithPadding metadataResponseJson = new JSONWithPadding(jsonSource,
				"callback");
		return metadataResponseJson;
	}

	@Override
	public JSONWithPadding getMetadataSnippetRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId, String version) {
		Document xmlRepresentation = (Document) innerBuilder
				.getMetadataSnippetRepresentation(uriInfo, apiscolInstanceName,
						metadataId, version);
		String jsonSource = JSonUtils.convertXMLToJson(xmlRepresentation);
		JSONWithPadding metadataResponseJson = new JSONWithPadding(jsonSource,
				"callback");
		return metadataResponseJson;
	}

	@Override
	public JSONWithPadding getMetadataSuccessfulDestructionReport(
			UriInfo uriInfo, String apiscolInstanceName, String metadataId,
			String warnings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONWithPadding getSuccessfullOptimizationReport(
			String requestedFormat, UriInfo uriInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONWithPadding getSuccessfulGlobalDeletionReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONWithPadding getSuccessfulRecoveryReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONWithPadding selectMetadataFollowingCriterium(UriInfo uriInfo,
			String apiscolInstanceName, String apiscolInstanceLabel,
			ISearchEngineResultHandler handler, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONWithPadding getCompleteMetadataListRepresentation(
			UriInfo uriInfo, String apiscolInstanceName,
			String apiscolInstanceLabel, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException {
		// TODO Auto-generated method stub
		return null;
	}
}
