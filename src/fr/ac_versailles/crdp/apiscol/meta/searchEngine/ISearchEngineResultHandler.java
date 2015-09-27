package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISearchEngineResultHandler {

	public abstract Map<String, String> getResultScoresById();

	public abstract Map<String, List<String>> getResultSnippetsById();

	public abstract LinkedHashSet<String> getResultsIds();

	public abstract Map<String, List<String>> getWordSuggestionsByQueryTerms();

	public abstract List<String> getQuerySuggestions();

	public abstract void parse(Object searchResult);

	HashMap<String, HashMap<String, String>> getStaticFacetGroups();

	HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getDynamicFacetGroups();

	HashMap<String, String> getRangefacetsGaps();

	public abstract long getTotalResultsFound();

}