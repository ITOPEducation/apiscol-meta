package fr.ac_versailles.crdp.apiscol.meta;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import fr.ac_versailles.crdp.apiscol.ApiscolApi;
import fr.ac_versailles.crdp.apiscol.ParametersKeys;
import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessFactory;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessFactory.DBTypes;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.FileSystemAccessException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.meta.representations.EntitiesRepresentationBuilderFactory;
import fr.ac_versailles.crdp.apiscol.meta.representations.IEntitiesRepresentationBuilder;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.AbstractSearchEngineFactory;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineFactory;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineQueryHandler;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.SearchEngineCommunicationException;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.SearchEngineErrorException;
import fr.ac_versailles.crdp.apiscol.transactions.KeyLock;
import fr.ac_versailles.crdp.apiscol.transactions.KeyLockManager;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

@Path("/maintenance")
public class MaintenanceApi extends ApiscolApi {

	private static Logger logger;
	private static ISearchEngineQueryHandler searchEngineQueryHandler;
	private static boolean staticInitialization = false;
	private static KeyLockManager keyLockManager;
	private static ISearchEngineFactory searchEngineFactory;

	@Context
	UriInfo uriInfo;
	@Context
	ServletContext context;

	public MaintenanceApi(@Context ServletContext context)
			throws FileSystemAccessException {
		super(context);
		if (!staticInitialization) {
			MetadataApi.initializeResourceDirectoryInterface(context);
			createLogger();
			createKeyLockManager();
			createSearchEngineQueryHandler(context);
			staticInitialization = true;
		}
	}

	private void createSearchEngineQueryHandler(ServletContext context) {
		String solrAddress = MetadataApi.getProperty(
				ParametersKeys.solrAddress, context);
		String solrSearchPath = MetadataApi.getProperty(
				ParametersKeys.solrSearchPath, context);
		String solrUpdatePath = MetadataApi.getProperty(
				ParametersKeys.solrUpdatePath, context);
		String solrExtractPath = MetadataApi.getProperty(
				ParametersKeys.solrExtractPath, context);
		String solrSuggestPath = MetadataApi.getProperty(
				ParametersKeys.solrSuggestPath, context);
		try {
			searchEngineFactory = AbstractSearchEngineFactory
					.getSearchEngineFactory(AbstractSearchEngineFactory.SearchEngineType.SOLRJ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		searchEngineQueryHandler = searchEngineFactory.getQueryHandler(
				solrAddress, solrSearchPath, solrUpdatePath, solrExtractPath,
				solrSuggestPath);
	}

	private void createKeyLockManager() {
		keyLockManager = KeyLockManager.getInstance();
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());
	}

	/**
	 * Creates a void resource
	 * 
	 * @return resource representation
	 * @throws SearchEngineCommunicationException
	 * @throws SearchEngineErrorException
	 * @throws DBAccessException
	 * @throws InexistentResourceInDatabaseException
	 * @throws DOMException
	 * @throws FileSystemAccessException
	 * @throws ResourceDirectoryNotFoundException
	 */
	@POST
	@Path("/optimization")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_ATOM_XML })
	public Response createResource(
			@QueryParam(value = "format") final String format,
			@Context HttpServletRequest request)
			throws SearchEngineErrorException,
			SearchEngineCommunicationException {
		String requestedFormat = guessRequestedFormat(request, format);
		IEntitiesRepresentationBuilder<?> rb = EntitiesRepresentationBuilderFactory
				.getRepresentationBuilder(requestedFormat, context);
		searchEngineQueryHandler.processOptimizationQuery();
		return Response.ok(
				rb.getSuccessfullOptimizationReport(requestedFormat, uriInfo),
				rb.getMediaType()).build();
	}

	@POST
	@Path("/deletion")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_ATOM_XML })
	public Response deleteAllContents(
			@QueryParam(value = "format") final String format,
			@Context HttpServletRequest request)
			throws SearchEngineErrorException,
			SearchEngineCommunicationException, DBAccessException {
		KeyLock keyLock = null;
		IEntitiesRepresentationBuilder<?> rb = null;
		try {
			keyLock = keyLockManager.getLock(KeyLockManager.GLOBAL_LOCK_KEY);
			keyLock.lock();
			try {
				ResourceDirectoryInterface.deleteAllFiles();
				searchEngineQueryHandler.deleteIndex();
				IResourceDataHandler resourceDataHandler = new DBAccessFactory()
						.setDbType(DBTypes.mongoDB)
						.setParameters(dbConnexionParameters).build();
				resourceDataHandler.deleteAllDocuments();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				keyLock.unlock();

			}
		} finally {
			if (keyLock != null) {
				keyLock.release();
			}
			logger.info(String
					.format("Leaving critical section with mutual exclusion for all the content service"));
		}
		rb = EntitiesRepresentationBuilderFactory.getRepresentationBuilder(
				MediaType.APPLICATION_ATOM_XML, context);
		return Response.ok().entity(rb.getSuccessfulGlobalDeletionReport())
				.build();
	}

	@POST
	@Path("/recovery")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_ATOM_XML })
	public Response startRecovery(
			@QueryParam(value = "format") final String format,
			@Context HttpServletRequest request)
			throws SearchEngineErrorException,
			SearchEngineCommunicationException, FileSystemAccessException,
			DBAccessException {
		KeyLock keyLock = null;
		IEntitiesRepresentationBuilder<?> rb = null;
		try {
			keyLock = keyLockManager.getLock(KeyLockManager.GLOBAL_LOCK_KEY);
			keyLock.lock();
			try {
				IResourceDataHandler resourceDataHandler = new DBAccessFactory()
				.setDbType(DBTypes.mongoDB)
				.setParameters(dbConnexionParameters).build();

				rb = EntitiesRepresentationBuilderFactory
						.getRepresentationBuilder(
								MediaType.APPLICATION_ATOM_XML, context);
				searchEngineQueryHandler.deleteIndex();
				resourceDataHandler.deleteAllDocuments();
				ArrayList<String> resourceList = ResourceDirectoryInterface
						.getMetadataList();
				Iterator<String> it = resourceList.iterator();
				boolean solrIsWaitingForCommit = false;
				while (it.hasNext()) {
					String metadataId = it.next();

					String filePath = ResourceDirectoryInterface
							.getFilePath(metadataId);
					ResourceDirectoryInterface.renewJsonpFile(metadataId);
					searchEngineQueryHandler.processAddQuery(filePath);
					solrIsWaitingForCommit = true;
					Document metadata = null;
					try {
						metadata = ResourceDirectoryInterface
								.getMetadataAsDocument(metadataId);
					} catch (MetadataNotFoundException e) {
						logger.warn(String
								.format("It is impossible, we are listing the file from metadata files directory, file for %s must exist",
										metadataId));
					}
					resourceDataHandler.createMetadataEntry(metadataId,
							metadata);
				}
				if (solrIsWaitingForCommit)
					searchEngineQueryHandler.processCommitQuery();
			} finally {
				keyLock.unlock();

			}
		} finally {
			if (keyLock != null) {
				keyLock.release();
			}
			logger.info(String
					.format("Leaving critical section with mutual exclusion for all the content service"));
		}
		return Response.ok().entity(rb.getSuccessfulRecoveryReport()).build();
	}
}
