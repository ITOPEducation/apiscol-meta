package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

public interface ISearchEngineFactory {

	ISearchEngineQueryHandler getQueryHandler(String solrAddress,
			String solrSearchPath, String solrUpdatePath, String solrExtractPath, String solrSuggestPath);

	ISearchEngineResultHandler getResultHandler();

}
