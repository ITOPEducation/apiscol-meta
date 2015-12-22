package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.w3c.dom.Document;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.Modification;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.Node;

public interface IResourceDataHandler {

	void createMetadataEntry(String metadataId, Document metadata)
			throws DBAccessException;

	void setMetadata(String metadataId, Document lomData)
			throws DBAccessException, InexistentResourceInDatabaseException;

	void deInitialize();

	void deleteAllDocuments() throws DBAccessException;

	HashMap<String, String> getMetadataProperties(String metadataId)
			throws DBAccessException;

	void updateMetadataEntry(String metadataId, Document metadata)
			throws DBAccessException;;

	void deleteMetadataEntry(String metadataId) throws DBAccessException;

	Node getMetadataHierarchyFromRoot(String rootId, URI baseUri)
			throws DBAccessException;

	HashMap<String, ArrayList<Modification>> getModificationsToApplyToRelatedResources(
			String url) throws DBAccessException;

}
