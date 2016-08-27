package fr.ac_versailles.crdp.apiscol.meta;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;

import fr.ac_versailles.crdp.apiscol.ApiscolApi;
import fr.ac_versailles.crdp.apiscol.ParametersKeys;
import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessBuilder;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessBuilder.DBTypes;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.FileSystemAccessException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry;
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
public class MaintenanceApi extends ApiscolMetaApi {

	private static Logger logger;
	private static ISearchEngineQueryHandler searchEngineQueryHandler;
	private static boolean staticInitialization = false;
	private static KeyLockManager keyLockManager;
	private static ISearchEngineFactory searchEngineFactory;
	private static MaintenanceRegistry maintenanceRegistry;
	@Context
	UriInfo uriInfo;
	@Context
	ServletContext context;

	public MaintenanceApi(@Context ServletContext context)
			throws FileSystemAccessException {
		super(context);
		if (!staticInitialization) {
			fetchScolomfrUtils(context);
			MetadataApi.initializeResourceDirectoryInterface(context);
			createLogger();
			createKeyLockManager();
			createSearchEngineQueryHandler(context);
			maintenanceRegistry = new MaintenanceRegistry();
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
		rb.setScolomfrUtils(scolomfrUtils);
		searchEngineQueryHandler.processOptimizationQuery();
		return Response.ok(
				rb.getSuccessfullOptimizationReport(requestedFormat,
						getExternalUri()), rb.getMediaType()).build();
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
				IResourceDataHandler resourceDataHandler = new DBAccessBuilder()
						.setScolomfrUtils(scolomfrUtils)
						.setDbType(DBTypes.mongoDB)
						.setParameters(getDbConnexionParameters()).build();
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
		rb.setScolomfrUtils(scolomfrUtils);
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
		Integer maintenanceRecoveryId;
		try {
			keyLock = keyLockManager.getLock(KeyLockManager.GLOBAL_LOCK_KEY);
			keyLock.lock();
			try {
				IResourceDataHandler resourceDataHandler = new DBAccessBuilder()
						.setScolomfrUtils(scolomfrUtils)
						.setDbType(DBTypes.mongoDB)
						.setParameters(getDbConnexionParameters()).build();

				rb = EntitiesRepresentationBuilderFactory
						.getRepresentationBuilder(
								MediaType.APPLICATION_ATOM_XML, context);
				rb.setScolomfrUtils(scolomfrUtils);

				if (maintenanceRegistry.hasRunningWorker())
					maintenanceRecoveryId = maintenanceRegistry
							.getRunninWorkerId();
				else
					maintenanceRecoveryId = maintenanceRegistry.newMaintenance(
							searchEngineQueryHandler, resourceDataHandler);

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
		return Response
				.ok()
				.entity(rb.getMaintenanceRecoveryRepresentation(
						maintenanceRecoveryId, getExternalUri(),
						maintenanceRegistry, 0)).build();
	}

	@GET
	@Path("/recovery/{maintenancerecoveryid}")
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_XML })
	public Response getMaintenanceRecoveryState(
			@Context HttpServletRequest request,
			@PathParam(value = "maintenancerecoveryid") final Integer urlParsingId,
			@DefaultValue("0") @QueryParam(value = "nblines") final Integer nblines)
			throws IOException {
		IEntitiesRepresentationBuilder<?> rb = EntitiesRepresentationBuilderFactory
				.getRepresentationBuilder(MediaType.APPLICATION_ATOM_XML,
						context);
		rb.setScolomfrUtils(scolomfrUtils);
		return Response
				.ok()
				.entity(rb.getMaintenanceRecoveryRepresentation(urlParsingId,
						getExternalUri(), maintenanceRegistry, nblines))
				.header("Access-Control-Allow-Origin", "*")
				.type(rb.getMediaType()).build();
	}
}
