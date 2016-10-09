package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.database.InexistentResourceInDatabaseException;
import fr.ac_versailles.crdp.apiscol.database.MongoUtils;
import fr.ac_versailles.crdp.apiscol.meta.MetadataKeySyntax;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.HierarchyAnalyser.Differencies;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.Modification;
import fr.ac_versailles.crdp.apiscol.meta.hierarchy.Node;
import fr.ac_versailles.crdp.apiscol.meta.references.RelationKinds;
import fr.ac_versailles.crdp.apiscol.utils.JSonUtils;

public class MongoResourceDataHandler extends AbstractResourcesDataHandler {

	public MongoResourceDataHandler(Map<String, String> dbParams)
			throws DBAccessException {
		super(dbParams);
	}

	public enum DBKeys {
		id("_id"), relationEntry("relation.resource.identifier.entry"), mainFile(
				"main"), type("type"), metadata("metadata"), url("url"), etag(
				"etag"), deadLink("dead_link"), relation("relation");
		private String value;

		private DBKeys(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

	private static final String DB_NAME = "apiscol";
	private static final String COLLECTION_NAME = "metadata";
	private static final int TREE_MAX_DEPTH = 14;
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
		convertVcardsLines(document);
		String jsonSource = JSonUtils.convertXMLToJson(document);
		DBObject dbObject = (DBObject) JSON.parse(jsonSource);
		dbObject.put(DBKeys.id.toString(), metadataId);
		metadataCollection.insert(dbObject);
	}

	private void convertVcardsLines(Document document) {
		NodeList entities = document.getElementsByTagName("entity");
		for (int i = 0; i < entities.getLength(); i++) {
			Element entity = (Element) entities.item(i);
			String vcardText = entity.getTextContent();
			vcardText = vcardText.replaceAll("(\r\n|\r|\n)", "§");
			entity.setTextContent(vcardText);
		}
	}

	@Override
	public void setMetadata(String metadataId, Document lomData)
			throws DBAccessException, InexistentResourceInDatabaseException {
		// noop

	}

	@Override
	public Node getMetadataHierarchyFromRoot(String rootId, URI baseuri)
			throws DBAccessException {
		return getMetadataHierarchyFromRoot(rootId, baseuri, 0);
	}

	@SuppressWarnings("unchecked")
	private Node getMetadataHierarchyFromRoot(String rootId, URI baseUri,
			int depth) throws DBAccessException {
		Node node = new Node();
		node.setMdid(new StringBuilder().append(baseUri.toString()).append('/')
				.append(rootId).toString());
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
													.replaceAll(
															baseUri.toString(),
															"").substring(1);
											if (depth < TREE_MAX_DEPTH)
												node.addChild(getMetadataHierarchyFromRoot(
														childId, baseUri,
														depth + 1));
										}
									}

								}
							}
							if (StringUtils.equals(value,
									RelationKinds.EST_REQUIS_PAR.toString())) {
								if (relationObject.containsField("resource")) {
									DBObject resourceObject = (DBObject) relationObject
											.get("resource");
									if (resourceObject
											.containsField("identifier")) {
										DBObject identifierObject = (DBObject) resourceObject
												.get("identifier");
										if (identifierObject
												.containsField("entry")) {
											String nextNodeuri = (String) identifierObject
													.get("entry");
											node.registerNextNodeUri(nextNodeuri);
										}
									}

								}
							}

						}
					}
				}

		}
		node.reorderChildren();
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
		String arkIdentifier = "";
		String icon = "";
		String aggregationLevelLabel = "";
		String aggregationLevelUri = "";
		String contentUrl = "";
		String contentRestUrl = "";
		String contentMime = "";
		List<String> educationalResourceTypes = new ArrayList<String>();
		List<String> keywords = new ArrayList<String>();
		List<String> authors = new ArrayList<String>();
		List<Pair<String, String>> contributors = new ArrayList<Pair<String, String>>();
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
				if (identifierObject instanceof BasicDBList) {
					BasicDBList identifierList = (BasicDBList) identifierObject;
					identifierObject = null;
					Iterator<Object> iterator = identifierList.iterator();
					while (iterator.hasNext()) {
						identifierObject = (BasicDBObject) iterator.next();
						String catalog = (String) identifierObject
								.get("catalog");
						if (catalog != null && catalog.equals("URI")) {
							break;
						}
					}

				}
				if (identifierObject != null
						&& identifierObject.containsField("entry")) {
					String catalog = (String) identifierObject.get("catalog");
					if (catalog != null && catalog.equals("URI")) {
						contentRestUrl = (String) identifierObject.get("entry");
					}

				}
			}
			if (generalObject != null
					&& generalObject.containsField("identifier")) {
				DBObject identifierObject = (DBObject) generalObject
						.get("identifier");
				if (identifierObject instanceof BasicDBList) {
					BasicDBList identifierList = (BasicDBList) identifierObject;
					identifierObject = null;
					Iterator<Object> iterator = identifierList.iterator();
					while (iterator.hasNext()) {
						identifierObject = (BasicDBObject) iterator.next();
						String catalog = (String) identifierObject
								.get("catalog");
						if (catalog != null && catalog.equals("ARK")) {
							break;
						}
					}

				}
				if (identifierObject != null
						&& identifierObject.containsField("entry")) {
					String catalog = (String) identifierObject.get("catalog");
					if (catalog != null && catalog.equals("ARK")) {
						arkIdentifier = (String) identifierObject.get("entry");
					}

				}
			}
			if (generalObject != null && generalObject.containsField("keyword")) {
				ArrayList<BasicDBObject> keywordObjects;
				try {
					keywordObjects = (ArrayList<BasicDBObject>) generalObject
							.get("keyword");
				} catch (ClassCastException e) {
					keywordObjects = new ArrayList<BasicDBObject>();
					BasicDBObject keywordObject = (BasicDBObject) generalObject
							.get("keyword");
					keywordObjects.add(keywordObject);
				}
				if (keywordObjects != null)
					for (BasicDBObject keywordObject : keywordObjects) {

						String keyword = getStringInUserLanguage(keywordObject);
						keywords.add(keyword);
					}
			}
			if (generalObject != null
					&& generalObject.containsField("aggregationLevel")) {
				DBObject aggregationLevelObject = (DBObject) generalObject
						.get("aggregationLevel");
				if (aggregationLevelObject.containsField("value")) {
					aggregationLevelUri = (String) aggregationLevelObject
							.get("value");

					aggregationLevelLabel = scolomfrUtils.getSkosApi()
							.getPrefLabelForResource(aggregationLevelUri);
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
							if (StringUtils.equals(value,
									RelationKinds.VIGNETTE.toString())) {
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
		if (metadataObject != null
				&& metadataObject.containsField("educational")) {
			ArrayList<BasicDBObject> educationalObjects;
			try {
				educationalObjects = (ArrayList<BasicDBObject>) metadataObject
						.get("educational");
			} catch (ClassCastException e) {
				educationalObjects = new ArrayList<BasicDBObject>();
				BasicDBObject educationalObject = (BasicDBObject) metadataObject
						.get("educational");
				educationalObjects.add(educationalObject);
			}
			// no support for multiple educational tags.
			// we keep the first one we meet
			if (educationalObjects != null)
				for (BasicDBObject educationalObject : educationalObjects) {
					if (educationalObject != null
							&& educationalObject
									.containsField("learningResourceType")) {
						ArrayList<BasicDBObject> learningResourceTypeObjects;
						try {
							learningResourceTypeObjects = (ArrayList<BasicDBObject>) educationalObject
									.get("learningResourceType");
						} catch (ClassCastException e) {
							learningResourceTypeObjects = new ArrayList<BasicDBObject>();
							BasicDBObject learningResourceTypeObject = (BasicDBObject) educationalObject
									.get("learningResourceType");
							learningResourceTypeObjects
									.add(learningResourceTypeObject);
						}
						for (BasicDBObject learningResourceTypeObject : learningResourceTypeObjects) {
							if (learningResourceTypeObject
									.containsField("value")) {
								educationalResourceTypes
										.add((String) learningResourceTypeObject
												.get("value"));

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
							ArrayList<BasicDBObject> entitiesObjects = new ArrayList<BasicDBObject>();
							ArrayList<String> entities = new ArrayList<String>();
							if (contributeObject.containsField("entity")) {
								try {
									entitiesObjects = (ArrayList<BasicDBObject>) contributeObject
											.get("entity");
									for (BasicDBObject entityObject : entitiesObjects) {
										entities.add(entityObject.toString());
									}
								} catch (ClassCastException e) {
									entitiesObjects = new ArrayList<BasicDBObject>();
									String entityObject = (String) contributeObject
											.get("entity");
									entities.add(entityObject);
								}
							}

							for (String entity : entities) {
								if (StringUtils.equals(value, "author")
										|| StringUtils.equals(value, "auteur")) {
									authors.add(entity);

								} else {

									Pair<String, String> contributor = new Pair<String, String>(
											value, entity);
									contributors.add(contributor);

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
							if (StringUtils.equals(value,
									RelationKinds.FAIT_PARTIE_DE.toString())) {
								if (relationObject.containsField("resource")) {
									DBObject resourceObject = (DBObject) relationObject
											.get("resource");
									if (resourceObject
											.containsField("identifier")) {
										DBObject identifierObject = (DBObject) resourceObject
												.get("identifier");
										if (identifierObject
												.containsField("entry")) {
											String parentUri = (String) identifierObject
													.get("entry");
											mdProperties
													.put(MetadataProperties.parentUri
															.toString(),
															parentUri);
											String parentId = MetadataKeySyntax
													.extractMetadataIdFromUrl(parentUri);
											DBObject parentObject = getMetadataById(parentId);
											if (parentObject != null
													&& parentObject
															.containsField("general")) {
												DBObject parentGeneralObject = (DBObject) parentObject
														.get("general");
												if (parentGeneralObject != null
														&& parentGeneralObject
																.containsField("title")) {
													DBObject parentTitleObject = (DBObject) parentGeneralObject
															.get("title");
													String parentTitle = getStringInUserLanguage(parentTitleObject);
													mdProperties
															.put(MetadataProperties.parentTitle
																	.toString(),
																	parentTitle);
												}
											}
										}
									}

								}
							}

						}
					}
				}

		}
		mdProperties.put(MetadataProperties.title.toString(), title);
		mdProperties
				.put(MetadataProperties.description.toString(), description);
		for (int i = 0; i < educationalResourceTypes.size(); i++) {
			String educationalresourceType = educationalResourceTypes.get(i);
			mdProperties.put(
					MetadataProperties.educationalResourceType.toString() + i,
					educationalresourceType);
			String educationalResourceTypeTiTle = scolomfrUtils.getSkosApi()
					.getPrefLabelForResource(educationalresourceType);
			mdProperties.put(
					MetadataProperties.educationalResourceTypeTitle.toString()
							+ i, educationalResourceTypeTiTle);
		}
		for (int i = 0; i < keywords.size(); i++) {
			mdProperties.put(MetadataProperties.keyword.toString() + i,
					keywords.get(i));
		}
		mdProperties.put(MetadataProperties.icon.toString(), icon);
		mdProperties.put(MetadataProperties.contentUrl.toString(), contentUrl);
		mdProperties
				.put(MetadataProperties.contentMime.toString(), contentMime);
		mdProperties.put(MetadataProperties.contentRestUrl.toString(),
				contentRestUrl);
		mdProperties.put(MetadataProperties.arkIdentifier.toString(),
				arkIdentifier);
		mdProperties.put(MetadataProperties.aggregationLevel.toString(),
				aggregationLevelUri);

		mdProperties.put(MetadataProperties.aggregationLevelLabel.toString(),
				aggregationLevelLabel);
		for (int i = 0; i < authors.size(); i++) {
			mdProperties.put(MetadataProperties.author.toString() + i,
					authors.get(i));
		}
		for (int i = 0; i < contributors.size(); i++) {
			Pair<String, String> contributor = contributors.get(i);
			String roleUri = contributor.getKey();
			String roleLabel = scolomfrUtils.getSkosApi()
					.getPrefLabelForResource(roleUri);
			if (StringUtils.isEmpty(roleLabel)) {
				roleLabel = roleUri;
			}
			String inlineContributor = new StringBuilder().append(roleUri)
					.append(MetadataProperties.separator).append(roleLabel)
					.append(MetadataProperties.separator)
					.append(contributor.getValue()).toString();
			mdProperties.put(MetadataProperties.contributor.toString() + i,
					inlineContributor);
		}
		return mdProperties;
	}

	private String getStringInUserLanguage(DBObject langStringObject) {
		String string = "";
		if (langStringObject.containsField("string")) {

			try {
				string = (String) langStringObject.get("string");
			} catch (ClassCastException e1) {
				DBObject stringObject;
				try {
					BasicDBList stringObjects = (BasicDBList) langStringObject
							.get("string");
					stringObject = (DBObject) stringObjects.get(0);
				} catch (ClassCastException e2) {
					stringObject = (DBObject) langStringObject.get("string");
				}
				if (stringObject != null && stringObject.containsField("#text")) {
					string = (String) stringObject.get("#text");
				}
			}

		}

		return string;
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

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, ArrayList<Modification>> getModificationsToApplyToRelatedResources(
			String url) throws DBAccessException {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject(DBKeys.id.toString(), true)
				.append(DBKeys.relation.toString(), true);
		query.put(DBKeys.relationEntry.toString(), url);
		try {
			DBCursor cursor = metadataCollection.find(query, fields);
			HashMap<String, ArrayList<Modification>> modifications = new HashMap<String, ArrayList<Modification>>();
			Iterator<DBObject> it = cursor.iterator();
			String relatedResourceId = null;
			while (it.hasNext()) {
				DBObject metadataObject = (DBObject) it.next();
				if (metadataObject != null
						&& metadataObject.containsField(DBKeys.id.toString())) {
					relatedResourceId = (String) metadataObject.get(DBKeys.id
							.toString());

				}
				if (StringUtils.isEmpty(relatedResourceId)) {
					continue;
				}
				if (metadataObject != null
						&& metadataObject.containsField("relation")) {
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
									String relation = (String) kindObject
											.get("value");
									if (relationObject
											.containsField("resource")) {
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
												if (StringUtils.equals(
														childUri, url)) {
													if (!modifications
															.containsKey(relatedResourceId)) {
														modifications
																.put(relatedResourceId,
																		new ArrayList<Modification>());
													}
													RelationKinds relationKind = RelationKinds
															.getFromString(relation
																	.trim());
													if (relationKind != null) {
														Modification modification = new Modification(
																Differencies.removed,
																relationKind,
																url);
														if (!modifications
																.get(relatedResourceId)
																.contains(
																		modification)) {
															modifications
																	.get(relatedResourceId)
																	.add(modification);
														}
													}
												}
											}
										}

									}

								}
							}
						}

				}
			}
			return modifications;
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
		convertVcardsLines(document);
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
