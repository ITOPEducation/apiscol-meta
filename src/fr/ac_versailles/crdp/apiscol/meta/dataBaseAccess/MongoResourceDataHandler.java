package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;
import fr.ac_versailles.crdp.apiscol.database.MongoUtils;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.Node;
import fr.ac_versailles.crdp.apiscol.meta.references.RelationKinds;
import fr.ac_versailles.crdp.apiscol.utils.JSonUtils;

public class MongoResourceDataHandler extends AbstractResourcesDataHandler {

	public MongoResourceDataHandler(Map<String, String> dbParams)
			throws DBAccessException {
		super(dbParams);
	}

	public enum DBKeys {
		id("_id"), mainFile("main"), type("type"), metadata("metadata"), url(
				"url"), etag("etag"), deadLink("dead_link");
		private String value;

		private DBKeys(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public enum AggregationLevels {
		UNKNOWN("unknown"), LEARNING_OBJECT("learning object"), LESSON("lesson"), COURSE(
				"course"), CURRICULUM("curriculum");
		private String value;

		private AggregationLevels(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private static final String DB_NAME = "apiscol";
	private static final String COLLECTION_NAME = "metadata";
	private static DBCollection metadataCollection;
	private static Mongo mongo;

	@Override
	protected void dbConnect(Map<String, String> dbParams)
			throws DBAccessException {
		if (mongo != null) {
			return;
		}
		mongo = MongoUtils.getMongoConnection(dbParams);
		initMetadataCollection();

	}

	private void initMetadataCollection() throws DBAccessException {
		metadataCollection = MongoUtils.getCollection(DB_NAME, COLLECTION_NAME,
				mongo);
	}

	@Override
	protected void dbDisconnect() {
		MongoUtils.dbDisconnect(mongo);
	}

	@Override
	public void deleteAllDocuments() throws DBAccessException {
		metadataCollection.drop();
	}

	@Override
	public void createMetadataEntry(String metadataId, Document document)
			throws DBAccessException {
		String jsonSource = JSonUtils.convertXMLToJson(document);
		DBObject dbObject = (DBObject) JSON.parse(jsonSource);
		dbObject.put(DBKeys.id.toString(), metadataId);
		metadataCollection.insert(dbObject);
	}

	@Override
	public void setMetadata(String metadataId, Document lomData)
			throws DBAccessException, InexistentResourceInDatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	@SuppressWarnings("unchecked")
	public Node getMetadataHierarchyFromRoot(String rootId, UriInfo uriInfo)
			throws DBAccessException {
		Node node = new Node();
		node.setMdid(new StringBuilder().append(uriInfo.getBaseUri()).append(rootId).toString());
		DBObject rootMetadataObject = getMetadataById(rootId);
		if (rootMetadataObject != null
				&& rootMetadataObject.containsField("relation")) {
			ArrayList<BasicDBObject> relationsObject;
			try {
				relationsObject = (ArrayList<BasicDBObject>) rootMetadataObject
						.get("relation");
			} catch (ClassCastException e) {
				relationsObject = new ArrayList<BasicDBObject>();
				BasicDBObject relationObject = (BasicDBObject) rootMetadataObject
						.get("relation");
				relationsObject.add(relationObject);
			}
			if (relationsObject != null)
				for (BasicDBObject relationObject : relationsObject) {
					if (relationObject != null
							&& relationObject.containsField("kind")) {
						DBObject kindObject = (DBObject) relationObject
								.get("kind");
						if (kindObject.containsField("value")) {
							String value = (String) kindObject.get("value");
							if (StringUtils.equals(value,
									RelationKinds.CONTIENT.toString())) {
								if (relationObject.containsField("resource")) {
									DBObject resourceObject = (DBObject) relationObject
											.get("resource");
									if (resourceObject
											.containsField("identifier")) {
										DBObject identifierObject = (DBObject) resourceObject
												.get("identifier");
										if (identifierObject
												.containsField("entry")) {
											String childUri = (String) identifierObject
													.get("entry");
											String childId = childUri
													.replaceAll(uriInfo
															.getBaseUri()
															.toString(), "");
											node.addChild(getMetadataHierarchyFromRoot(
													childId, uriInfo));
										}
									}

								}
							}

						}
					}
				}
		}

		return node;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getMetadataProperties(String metadataId)
			throws DBAccessException {
		HashMap<String, String> mdProperties = new HashMap<String, String>();
		DBObject metadataObject = getMetadataById(metadataId);
		String title = "";
		String description = "";
		String icon = "";
		String aggregationLevel = "";
		String contentUrl = "";
		String contentRestUrl = "";
		String contentMime = "";
		List<String> authors = new ArrayList<String>();
		if (metadataObject != null && metadataObject.containsField("general")) {
			DBObject generalObject = (DBObject) metadataObject.get("general");
			if (generalObject != null && generalObject.containsField("title")) {
				DBObject titleObject = (DBObject) generalObject.get("title");
				title = getStringInUserLanguage(titleObject);
			}
			if (generalObject != null
					&& generalObject.containsField("description")) {
				DBObject descObject = (DBObject) generalObject
						.get("description");
				description = getStringInUserLanguage(descObject);
			}
			if (generalObject != null
					&& generalObject.containsField("identifier")) {
				DBObject identifierObject = (DBObject) generalObject
						.get("identifier");
				if (identifierObject.containsField("entry")) {
					contentRestUrl = (String) identifierObject.get("entry");
				}
			}
			if (generalObject != null
					&& generalObject.containsField("aggregationLevel")) {
				DBObject aggregationLevelObject = (DBObject) generalObject
						.get("aggregationLevel");
				if (aggregationLevelObject.containsField("value")) {
					int aggregationLevelInt = 0;
					try {
						aggregationLevelInt = Integer
								.parseInt((String) aggregationLevelObject
										.get("value"));
						if (aggregationLevelInt >= 0
								&& aggregationLevelInt <= 4) {
							aggregationLevel = AggregationLevels.values()[aggregationLevelInt]
									.toString();
						}

					} catch (NumberFormatException e) {
						logger.error("The ressource "
								+ metadataId
								+ " has the following data as aggregation Level "
								+ (String) aggregationLevelObject.get("value"));
					}

				}
			}
		}
		if (metadataObject != null && metadataObject.containsField("relation")) {
			ArrayList<BasicDBObject> relationsObject;
			try {
				relationsObject = (ArrayList<BasicDBObject>) metadataObject
						.get("relation");
			} catch (ClassCastException e) {
				relationsObject = new ArrayList<BasicDBObject>();
				BasicDBObject relationObject = (BasicDBObject) metadataObject
						.get("relation");
				relationsObject.add(relationObject);
			}
			if (relationsObject != null)
				for (BasicDBObject relationObject : relationsObject) {
					if (relationObject != null
							&& relationObject.containsField("kind")) {
						DBObject kindObject = (DBObject) relationObject
								.get("kind");
						if (kindObject.containsField("value")) {
							String value = (String) kindObject.get("value");
							if (StringUtils.equals(value, "a pour vignette")) {
								if (relationObject.containsField("resource")) {
									DBObject resourceObject = (DBObject) relationObject
											.get("resource");
									if (resourceObject
											.containsField("identifier")) {
										DBObject identifierObject = (DBObject) resourceObject
												.get("identifier");
										if (identifierObject
												.containsField("entry")) {
											icon = (String) identifierObject
													.get("entry");
										}
									}

								}
							}

						}
					}
				}
		}
		if (metadataObject != null && metadataObject.containsField("lifeCycle")) {
			DBObject lifeCycleObject = (DBObject) metadataObject
					.get("lifeCycle");
			if (lifeCycleObject != null
					&& lifeCycleObject.containsField("contribute")) {
				ArrayList<BasicDBObject> contributesObject;
				try {
					contributesObject = (ArrayList<BasicDBObject>) lifeCycleObject
							.get("contribute");
				} catch (ClassCastException e) {
					contributesObject = new ArrayList<BasicDBObject>();
					BasicDBObject contributeObject = (BasicDBObject) lifeCycleObject
							.get("contribute");
					contributesObject.add(contributeObject);

				}
				for (BasicDBObject contributeObject : contributesObject) {
					if (contributeObject != null
							&& contributeObject.containsField("role")) {
						DBObject roleObject = (DBObject) contributeObject
								.get("role");
						if (roleObject.containsField("value")) {
							String value = (String) roleObject.get("value");
							if (StringUtils.equals(value, "author")
									|| StringUtils.equals(value, "auteur")) {
								if (contributeObject.containsField("entity")) {
									authors.add((String) contributeObject
											.get("entity"));
								}
							}
						}
					}
				}
			}
		}

		if (metadataObject != null && metadataObject.containsField("technical")) {
			DBObject technicalObject = (DBObject) metadataObject
					.get("technical");
			if (technicalObject != null
					&& technicalObject.containsField("format")) {
				try {
					contentMime = (String) technicalObject.get("format");
				} catch (ClassCastException e) {
					BasicDBList contentMimes = ((BasicDBList) technicalObject
							.get("format"));
					contentMime = (String) contentMimes.get(0);
				}
			}
			if (technicalObject != null
					&& technicalObject.containsField("location")) {
				try {
					contentUrl = (String) technicalObject.get("location");
				} catch (ClassCastException e) {
					BasicDBList contentUrls = ((BasicDBList) technicalObject
							.get("location"));
					contentUrl = (String) contentUrls.get(0);
				}
			}
		}
		mdProperties.put(MetadataProperties.title.toString(), title);
		mdProperties
				.put(MetadataProperties.description.toString(), description);
		mdProperties.put(MetadataProperties.icon.toString(), icon);
		mdProperties.put(MetadataProperties.contentUrl.toString(), contentUrl);
		mdProperties
				.put(MetadataProperties.contentMime.toString(), contentMime);
		mdProperties.put(MetadataProperties.contentRestUrl.toString(),
				contentRestUrl);
		mdProperties.put(MetadataProperties.aggregationLevel.toString(),
				aggregationLevel);
		for (int i = 0; i < authors.size(); i++) {
			mdProperties.put(MetadataProperties.author.toString() + i,
					authors.get(i));
		}
		return mdProperties;
	}

	private String getStringInUserLanguage(DBObject titleObject) {
		if (titleObject.containsField("string")) {
			DBObject stringObject = (DBObject) titleObject.get("string");
			if (stringObject.containsField("#text")) {
				return (String) stringObject.get("#text");
			}
		}

		return "";
	}

	private DBObject getMetadataById(String metadataId)
			throws DBAccessException {
		BasicDBObject query = new BasicDBObject();
		query.put(DBKeys.id.toString(), metadataId);
		try {
			return metadataCollection.findOne(query);
		} catch (MongoException e) {
			String message = "Error while trying to read in metadata collection "
					+ e.getMessage();
			logger.error(message);
			throw new DBAccessException(message);
		}

	}

	@Override
	public void updateMetadataEntry(String metadataId, Document document)
			throws DBAccessException {
		String jsonSource = JSonUtils.convertXMLToJson(document);
		DBObject newMetadata = (DBObject) JSON.parse(jsonSource);
		newMetadata.put(DBKeys.id.toString(), metadataId);
		metadataCollection.update(
				new BasicDBObject().append(DBKeys.id.toString(), metadataId),
				newMetadata);
	}

	@Override
	public void deleteMetadataEntry(String metadataId) throws DBAccessException {
		metadataCollection.remove(getMetadataById(metadataId));
	}

}
