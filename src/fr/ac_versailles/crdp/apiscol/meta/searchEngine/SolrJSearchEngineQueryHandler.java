package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;

import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public class SolrJSearchEngineQueryHandler implements ISearchEngineQueryHandler {

	private static final String FILTERS_DECLARATION_SEPARATOR = "::";
	private final String solrSearchPath;
	private final String solrUpdatePath;
	private final String solrSuggestPath;

	private HttpSolrClient solr;
	private static Logger logger;

	public SolrJSearchEngineQueryHandler(String solrAddress,
			String solrSearchPath, String solrUpdatePath,
			String solrExtractPath, String solrSuggestPath) {
		this.solrSearchPath = solrSearchPath;
		this.solrUpdatePath = solrUpdatePath;
		this.solrSuggestPath = solrSuggestPath;
		solr = new HttpSolrClient(solrAddress);
		solr.setParser(new XMLResponseParser());
	}

	@Override
	public Object processSearchQuery(String keywords,
			String[] supplementsIdentifiers, float fuzzy,
			List<String> staticFiltersList, List<String> dynamicFiltersList,
			boolean disableHighlighting, Integer start, Integer rows,
			String sort) throws SearchEngineErrorException {
		createLogger();
		SolrQuery parameters = new SolrQuery();

		// strange behaviour. Rows means end
		parameters.setStart(start);
		parameters.setRows(start + rows);
		parameters.set("spellcheck.q", keywords);
		if (fuzzy > 0) {
			String[] words = keywords.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				words[i] += "~" + fuzzy;
			}
			keywords = StringUtils.join(words, " ");
		}
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(keywords);
		for (int i = 0; i < supplementsIdentifiers.length; i++) {
			queryBuilder.append(" OR id:\"").append(supplementsIdentifiers[i])
			/* .replace("/", "\\/")) */.append("\"");
		}
		parameters.set("q", queryBuilder.toString());
		parameters.set("qt", solrSearchPath);
		if (disableHighlighting)
			parameters.set("hl", false);
		for (int i = 0; i < staticFiltersList.size(); i++) {

			String filter = staticFiltersList.get(i);
			if (StringUtils.isEmpty(filter))
				continue;
			String[] split = filter.split(FILTERS_DECLARATION_SEPARATOR);

			String fieldName = split[0];
			String fieldValue = split[1];
			parameters.addFilterQuery(String.format("{!term f=%s}%s",
					fieldName, fieldValue));
		}
		for (int i = 0; i < dynamicFiltersList.size(); i++) {

			String filter = dynamicFiltersList.get(i);
			if (StringUtils.isEmpty(filter))
				continue;
			String[] split = filter.split(FILTERS_DECLARATION_SEPARATOR);
			if (split.length > 3)
				parameters.addFilterQuery(String.format(
						"{!field f=%s-taxon}%s!_!%s(__%s__)", split[0],
						split[1], split[2], split[3]));
		}

		switch (sort) {
		case "score":
			parameters.addSort("score", ORDER.desc);
			break;
		case "title":
			parameters.addSort("general.title", ORDER.asc);
			break;
		default:
			parameters.addSort("score", ORDER.desc);
			break;
		}

		QueryResponse response = null;

		try {
			response = solr.query(parameters);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to search keywords  %s whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (SolrException e) {
			String error = String
					.format("Solr was not able to parse the request  %s whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("Solr has thrown an IO exception when he was asked to search keywords  %s whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		}
		return response;
	}

	@Override
	public Object processSearchQuery(String identifier)
			throws SearchEngineErrorException {
		createLogger();
		SolrQuery parameters = new SolrQuery();

		// strange behaviour. Rows means end
		parameters.setStart(0);
		parameters.setRows(1);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("id:\"").append(identifier).append("\"");
		parameters.set("q", queryBuilder.toString());
		parameters.set("qt", solrSearchPath);
		parameters.set("hl", false);

		QueryResponse response = null;

		try {
			response = solr.query(parameters);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to search for metadata  %s whith the message %s",
							identifier, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (SolrException e) {
			String error = String
					.format("Solr was not able to parse the request  %s whith the message %s",
							identifier, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("Solr has thrown an IO exception when he was asked to search for metadata  %s whith the message %s",
							identifier, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		}
		return response;
	}

	@Override
	public Object processSearchQuery(List<String> forcedMetadataIdList)
			throws SearchEngineErrorException {
		createLogger();
		SolrQuery parameters = new SolrQuery();
		// strange behaviour. Rows means end
		parameters.setStart(0);
		parameters.setRows(forcedMetadataIdList.size());
		StringBuilder queryBuilder = new StringBuilder();
		Iterator<String> it = forcedMetadataIdList.iterator();
		boolean first = true;
		while (it.hasNext()) {
			String identifier = (String) it.next();
			if (!first)
				queryBuilder.append(" OR ");
			queryBuilder.append("id:\"").append(identifier).append("\"");
			first = false;
		}

		parameters.set("q", queryBuilder.toString());
		parameters.set("qt", solrSearchPath);
		parameters.set("hl", false);

		QueryResponse response = null;

		try {
			response = solr.query(parameters);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to search for metadata list %s whith the message %s",
							forcedMetadataIdList, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (SolrException e) {
			String error = String
					.format("Solr was not able to parse the request  %s whith the message %s",
							forcedMetadataIdList, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("Solr has thrown an IO exception when he was asked to search for metadata list %s whith the message %s",
							forcedMetadataIdList, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		}
		return response;
	}

	@Override
	public String processAddQuery(String filePath)
			throws SearchEngineCommunicationException,
			SearchEngineErrorException {
		createLogger();
		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest(
				solrUpdatePath);
		File file = new File(filePath);
		try {
			req.addFile(file, "text/xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.setParam("tr", "scolomfr_import.xsl");
		req.setParam("resource.name", file.getName());
		NamedList<Object> result = null;
		try {
			result = solr.request(req);
		} catch (SolrException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to index file %s whith the message %s",
							filePath, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown a server exception when he was asked to index file %s whith the message %s",
							filePath, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("There was a connexion problem with solr when he was asked to index file %s whith the message %s",
							filePath, e.getMessage());
			logger.error(error);
			throw new SearchEngineCommunicationException(error);
		}

		createLogger();
		logger.info(String.format("Query to solr : add the file %s", filePath));

		return "";
	}

	@Override
	public String processCommitQuery() throws SearchEngineErrorException,
			SearchEngineCommunicationException {
		createLogger();
		try {
			solr.commit();
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to commit, whith the message  %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("There was a connexion problem with solr when he was asked commit, whith the message %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineCommunicationException(error);
		}
		// TODO what's that
		return "";
	}

	@Override
	public String processDeleteQuery(String documentIdentifier)
			throws SearchEngineErrorException,
			SearchEngineCommunicationException {
		createLogger();
		logger.info(String.format("Query to solr : delete with id : %s",
				documentIdentifier));
		UpdateResponse result = null;
		try {
			result = solr.deleteById(documentIdentifier);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to erase document %s from index whith the message %s",
							documentIdentifier, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("There was a connexion problem with solr when he was asked to erase document %s from index whith the message %s",
							documentIdentifier, e.getMessage());
			logger.error(error);
			throw new SearchEngineCommunicationException(error);
		}
		// TODO rien n'est fait du résultat
		return result.toString();
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(SolrJSearchEngineQueryHandler.class
							.getCanonicalName());

	}

	@Override
	public Object processSpellcheckQuery(String keywords)
			throws SearchEngineErrorException {
		createLogger();
		SolrQuery parameters = new SolrQuery();
		parameters.set("spellcheck.q", keywords);
		parameters.set("qt", solrSuggestPath);
		QueryResponse response = null;
		try {
			response = solr.query(parameters);
		} catch (SolrException e) {
			String error = String
					.format("Solr has thrown a runtime exception when he was asked to search keywords  %s for completion whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to search keywords  %s  for completion whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("Solr has thrown an IO exception when he was asked to search keywords  %s whith the message %s",
							keywords, e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		}
		return response;
	}

	@Override
	public void processOptimizationQuery() throws SearchEngineErrorException,
			SearchEngineCommunicationException {
		createLogger();
		try {
			solr.optimize();
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to optimize index whith the message %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("There was a connexion problem with solr when he was asked to optimize index whith the message %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineCommunicationException(error);
		}

	}

	@Override
	public void deleteIndex() throws SearchEngineErrorException,
			SearchEngineCommunicationException {
		try {
			solr.deleteByQuery("*:*");
			solr.commit();
		} catch (SolrServerException e) {
			String error = String
					.format("Solr has thrown an exception when he was asked to delete the index whith the message %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineErrorException(error);
		} catch (IOException e) {
			String error = String
					.format("There was a connexion problem with solr when he was asked to delete the index whith the message %s",
							e.getMessage());
			logger.error(error);
			throw new SearchEngineCommunicationException(error);
		}

	}

}
