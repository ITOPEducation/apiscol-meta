package fr.ac_versailles.crdp.apiscol.meta.maintenance;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineQueryHandler;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.SearchEngineCommunicationException;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.SearchEngineErrorException;

public class MaintenanceRecoveryWorker implements Runnable {

	private final Integer identifier;
	private ISearchEngineQueryHandler searchEngineQueryHandler;
	private IResourceDataHandler resourceDataHandler;
	private MaintenanceRegistry maintenanceRegistry;

	public MaintenanceRecoveryWorker(Integer identifier,
			ISearchEngineQueryHandler searchEngineQueryHandler,
			IResourceDataHandler resourceDataHandler,
			MaintenanceRegistry maintenanceRegistry) {
		this.identifier = identifier;
		this.searchEngineQueryHandler = searchEngineQueryHandler;
		this.resourceDataHandler = resourceDataHandler;
		this.maintenanceRegistry = maintenanceRegistry;

	}

	@Override
	public void run() {
		maintenanceRegistry.addMessage("Solr index is going to be erased",
				identifier);
		try {
			searchEngineQueryHandler.deleteIndex();
		} catch (SearchEngineErrorException
				| SearchEngineCommunicationException e2) {
			maintenanceRegistry
					.addMessage(
							String.format(
									"Communication problem with Search Engine while trying to delete index with message %s",
									e2.getMessage()), identifier);
		}
		maintenanceRegistry
				.addMessage("Solr index has been erased", identifier);
		maintenanceRegistry.addMessage("Database is going to be erased",
				identifier);
		try {
			resourceDataHandler.deleteAllDocuments();
		} catch (DBAccessException e1) {
			maintenanceRegistry.addMessage(String.format(
					"Impossible, to erase database content, error : %s",
					e1.getMessage()), identifier);
		}
		maintenanceRegistry.addMessage("Database has been erased", identifier);
		ArrayList<String> resourceList = ResourceDirectoryInterface
				.getMetadataList();
		maintenanceRegistry
				.addMessage(
						String.format(
								"		**** The recovery process will now loop on the %d metadata files. ****",
								resourceList.size()), identifier);
		Iterator<String> it = resourceList.iterator();

		boolean solrIsWaitingForCommit = false;
		while (it.hasNext()) {
			String metadataId = it.next();
			maintenanceRegistry.addMessage(
					String.format("				+++ Métadonnée %s", metadataId),
					identifier);
			String filePath = ResourceDirectoryInterface
					.getFilePath(metadataId);
			maintenanceRegistry.addMessage(
					String.format("Chemin du fichier : %s", filePath),
					identifier);
			ResourceDirectoryInterface.renewJsonpFile(metadataId);
			try {
				searchEngineQueryHandler.processAddQuery(filePath);
			} catch (SearchEngineCommunicationException
					| SearchEngineErrorException e1) {
				maintenanceRegistry
						.addMessage(
								String.format(
										"Communication problem with Search Engine while trying to add metadata %s with message %s",
										metadataId, e1.getMessage()),
								identifier);
			}
			solrIsWaitingForCommit = true;

			Document metadata = null;
			try {
				metadata = ResourceDirectoryInterface
						.getMetadataAsDocument(metadataId);
				maintenanceRegistry.addMessage(
						"Le document xml a été lu sur le disque", identifier);
			} catch (MetadataNotFoundException e) {
				maintenanceRegistry
						.addMessage(
								String.format(
										"It is impossible, we are listing the file from metadata files directory, file for %s must exist , error : %s",
										metadataId, e.getMessage()), identifier);

			}
			try {
				resourceDataHandler.createMetadataEntry(metadataId, metadata);
			} catch (DBAccessException e) {
				maintenanceRegistry
						.addMessage(
								String.format(
										"Impossible, to read metadata in database , error : %s",
										e.getMessage()), identifier);
			}
		}
		maintenanceRegistry.addMessage(
				"		**** End of metadata files processing. ****", identifier);
		if (solrIsWaitingForCommit)
			try {
				searchEngineQueryHandler.processCommitQuery();
				maintenanceRegistry.addMessage(
						"		**** Solr asked for commit. ****", identifier);
			} catch (SearchEngineErrorException
					| SearchEngineCommunicationException e) {
				maintenanceRegistry
						.addMessage(
								String.format(
										"Communication problem with Search Engine while trying to commit  : %s",
										e.getMessage()), identifier);
			}

	}
}
