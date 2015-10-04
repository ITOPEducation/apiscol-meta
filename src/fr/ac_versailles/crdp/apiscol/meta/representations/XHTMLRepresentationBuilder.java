package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry;
import fr.ac_versailles.crdp.apiscol.meta.resources.ResourcesLoader;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;
import fr.ac_versailles.crdp.apiscol.utils.HTMLUtils;
import fr.ac_versailles.crdp.apiscol.utils.XMLUtils;

public class XHTMLRepresentationBuilder extends
		AbstractRepresentationBuilder<String> {

	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory
			.newInstance();
	private static DocumentBuilder dBuilder;
	static {
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AbstractRepresentationBuilder<Document> innerBuilder;

	public XHTMLRepresentationBuilder() {
		innerBuilder = new XMLRepresentationBuilder();
	}

	@Override
	public MediaType getMediaType() {
		return MediaType.TEXT_HTML_TYPE;
	}

	@Override
	public String getMetadataRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId,
			boolean includeDescription, Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException {

		File xmlFile = ResourceDirectoryInterface.getMetadataFile(metadataId);

		Document metadataDocument = null;

		try {
			metadataDocument = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream xslStream = ResourcesLoader
				.loadResource("xsl/metadataXMLToHTMLTransformer.xsl");
		if (xslStream == null) {
			logger.error("Impossible de charger la feuille de transformation xsl");
		}
		Node result = XMLUtils.xsltTransform(xslStream, metadataDocument,
				params);
		return HTMLUtils.WrapInHTML5Headers((Document) result);

	}

	@Override
	public String getMetadataSuccessfulDestructionReport(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId, String warnings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessfullOptimizationReport(String requestedFormat,
			UriInfo uriInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessfulGlobalDeletionReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMetadataSnippetRepresentation(UriInfo uriInfo,
			String apiscolInstanceName, String metadataId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompleteMetadataListRepresentation(UriInfo uriInfo,
			String apiscolInstanceLabel, String apiscolInstanceName, int start,
			int rows, boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException {
		// TODO Auto-generated method stub
		return XMLUtils.XMLToString(innerBuilder
				.getCompleteMetadataListRepresentation(uriInfo,
						apiscolInstanceName, apiscolInstanceLabel, start, rows,
						includeDescription, resourceDataHandler, editUri,
						version));
	}

	@Override
	public String selectMetadataFollowingCriterium(UriInfo uriInfo,
			String apiscolInstanceLabel, String apiscolInstanceName,
			ISearchEngineResultHandler handler, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException {
		Document xmlResponse = innerBuilder.selectMetadataFollowingCriterium(
				uriInfo, apiscolInstanceName, apiscolInstanceLabel, handler,
				start, rows, true, resourceDataHandler, editUri, version);
		InputStream xslStream = ResourcesLoader
				.loadResource("xsl/metadataListXMLToHTMLTransformer.xsl");
		if (xslStream == null) {
			logger.error("Impossible de charger la feuille de transformation xsl pour les listes xsl/metadataListXMLToHTMLTransformer.xsl");
		}
		Node result = XMLUtils.xsltTransform(xslStream, xmlResponse,
				Collections.<String, String> emptyMap());
		return HTMLUtils.WrapInHTML5Headers((Document) result);

	}

	@Override
	public Object getMaintenanceRecoveryRepresentation(
			Integer maintenanceRecoveryId, UriInfo uriInfo,
			MaintenanceRegistry maintenanceRegistry, Integer nbLines) {
		// TODO Auto-generated method stub
		return null;
	}
}
