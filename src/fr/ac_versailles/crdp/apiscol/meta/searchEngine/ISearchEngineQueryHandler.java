package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

import java.util.List;

public interface ISearchEngineQueryHandler {

	public abstract Object processSearchQuery(String keywords,
			String[] supplementsIds, float fuzzy,
			List<String> staticFiltersList,
			List<String> additiveStaticFiltersList,
			List<String> dynamicFiltersList,
			List<String> additiveDynamicFiltersList,
			boolean disableHighlighting, Integer start, Integer rows,
			String sort) throws SearchEngineErrorException;

	public Object processSearchQuery(String identifiers)
			throws SearchEngineErrorException;

	public Object processSearchQuery(List<String> forcedMetadataIdList)
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