package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

import java.util.List;

public interface ISearchEngineQueryHandler {

	public abstract Object processSearchQuery(String keywords,
			String[] supplementsIds, float fuzzy,
			List<String> staticFiltersList, List<String> dynamicFiltersList,
			boolean disableHighlighting, Integer start, Integer rows)
			throws SearchEngineErrorException;

	public Object processSearchQuery(String identifiers)
			throws SearchEngineErrorException;

	public abstract Object processSpellcheckQuery(String query)
			throws SearchEngineErrorException;

	public abstract String processAddQuery(String filePath)
			throws SearchEngineCommunicationException,
			SearchEngineErrorException;

	public abstract String processCommitQuery()
			throws SearchEngineErrorException,
			SearchEngineCommunicationException;

	public abstract String processDeleteQuery(String documentIdentifier)
			throws SearchEngineErrorException,
			SearchEngineCommunicationException;

	public abstract void processOptimizationQuery()
			throws SearchEngineErrorException,
			SearchEngineCommunicationException;

	public abstract void deleteIndex() throws SearchEngineErrorException,
			SearchEngineCommunicationException;

}