package fr.ac_versailles.crdp.apiscol.meta.searchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SolrJSearchEngineResultHandler implements
		ISearchEngineResultHandler {
	private LinkedHashSet<String> resultsIds;
	private Map<String, String> resultScoresById;
	private Map<String, List<String>> resultSnippetsById;
	private Map<String, List<String>> wordSuggestionsByQueryTerms;
	private List<String> querySuggestions;
	private HashMap<String, HashMap<String, String>> staticFacetGroups;
	private HashMap<String, String> rangefacetsGaps;
	private HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> dynamicFacetGroups;
	private long totalResultsFound;

	public SolrJSearchEngineResultHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ac_versailles.crdp.apiscol.content.searchEngine.ISearchEngineResultHandler
	 * #getResultScoresById()
	 */
	@Override
	public Map<String, String> getResultScoresById() {
		return resultScoresById;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ac_versailles.crdp.apiscol.content.searchEngine.ISearchEngineResultHandler
	 * #getResultSnippetsById()
	 */
	@Override
	public Map<String, List<String>> getResultSnippetsById() {
		return resultSnippetsById;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ac_versailles.crdp.apiscol.content.searchEngine.ISearchEngineResultHandler
	 * #getResultsIds()
	 */
	@Override
	public LinkedHashSet<String> getResultsIds() {
		return resultsIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ac_versailles.crdp.apiscol.content.searchEngine.ISearchEngineResultHandler
	 * #getWordSuggestionsByQueryTerms()
	 */
	@Override
	public Map<String, List<String>> getWordSuggestionsByQueryTerms() {
		return wordSuggestionsByQueryTerms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ac_versailles.crdp.apiscol.content.searchEngine.ISearchEngineResultHandler
	 * #getQuerySuggestions()
	 */
	@Override
	public List<String> getQuerySuggestions() {
		return querySuggestions;
	}

	@Override
	public void parse(Object searchResult) {
		QueryResponse response = (QueryResponse) searchResult;
		SolrDocumentList documents = response.getResults();
		Map<String, Map<String, List<String>>> highlights = response
				.getHighlighting();
		totalResultsFound = documents.getNumFound();
		resultsIds = new LinkedHashSet<String>();
		resultScoresById = new HashMap<String, String>();
		resultSnippetsById = new HashMap<String, List<String>>();
		querySuggestions = new ArrayList<String>();
		wordSuggestionsByQueryTerms = new HashMap<String, List<String>>();
		staticFacetGroups = new HashMap<String, HashMap<String, String>>();
		rangefacetsGaps = new HashMap<String, String>();
		dynamicFacetGroups = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();
		if (documents == null)
			return;
		Iterator<SolrDocument> it = documents.iterator();
		while (it.hasNext()) {
			SolrDocument solrDocument = it.next();

			String id = (String) solrDocument.getFieldValue("id");
			resultsIds.add(id);
			resultScoresById.put(id, "" + solrDocument.getFieldValue("score"));
			resultSnippetsById.put(id, new ArrayList<String>());
		}
		if (highlights != null) {
			Iterator<String> it2 = highlights.keySet().iterator();
			while (it2.hasNext()) {
				String id = it2.next();
				Map<String, List<String>> highlight = highlights.get(id);
				if (highlight.keySet().contains("general.text")) {
					List<String> snippets = highlight.get("general.text");
					resultSnippetsById.put(id, snippets);
				}
			}
		}
		List<FacetField> facetFields = response.getFacetFields();
		if (facetFields != null) {
			Iterator<FacetField> itf = facetFields.iterator();
			String facetGroupName;
			while (itf.hasNext()) {
				FacetField facetField = itf.next();
				facetGroupName = facetField.getName();
				String dynamicGroupName = SolrRecordsSyntaxAnalyser
						.detectDynamicFacetGroupName(facetGroupName);
				if (StringUtils.isEmpty(dynamicGroupName))
					staticFacetGroups.put(facetGroupName,
							buildStaticFacetGroup(facetField));
				else
					dynamicFacetGroups.put(dynamicGroupName,
							buildDynamicFacetGroup(facetField));
			}
		}
		List<RangeFacet> facetRanges = response.getFacetRanges();
		if (facetRanges != null) {
			Iterator<RangeFacet> itf = facetRanges.iterator();
			String facetRangeName;

			while (itf.hasNext()) {
				RangeFacet<?, ?> rangefacet = itf.next();
				facetRangeName = rangefacet.getName();
				staticFacetGroups.put(facetRangeName,
						buildStaticRangeFacetGroup(rangefacet));
				rangefacetsGaps.put(facetRangeName, rangefacet.getGap()
						.toString());
			}
		}
		SpellCheckResponse spellchecks = response.getSpellCheckResponse();
		if (spellchecks != null) {
			Map<String, Suggestion> suggestionMap = spellchecks
					.getSuggestionMap();
			if (suggestionMap != null) {
				Iterator<String> it3 = suggestionMap.keySet().iterator();
				while (it3.hasNext()) {
					String word = it3.next();
					Suggestion suggestion = suggestionMap.get(word);
					List<String> alternatives = suggestion.getAlternatives();
					wordSuggestionsByQueryTerms.put(word, alternatives);

				}
			}
			List<Collation> collatedResults = spellchecks.getCollatedResults();
			if (collatedResults != null) {

				if (collatedResults != null) {
					Iterator<Collation> it4 = collatedResults.iterator();
					while (it4.hasNext()) {
						SpellCheckResponse.Collation collation = it4.next();
						querySuggestions.add(collation
								.getCollationQueryString());
					}
				}
			}
		}

	}

	private HashMap<String, HashMap<String, ArrayList<String>>> buildDynamicFacetGroup(
			FacetField facetField) {
		HashMap<String, HashMap<String, ArrayList<String>>> facets = new HashMap<String, HashMap<String, ArrayList<String>>>();
		List<Count> values = facetField.getValues();
		Iterator<Count> itv = values.iterator();
		while (itv.hasNext()) {
			FacetField.Count count = itv.next();
			ArrayList<String> segments = SolrRecordsSyntaxAnalyser
					.dynamicFacetEntrySegments(count.getName());
			long numDocs = count.getCount();
			if (segments.size() == 0)
				continue;
			String taxon = segments.get(0);
			String entryCode = segments.get(1);
			String entryLabel = segments.get(2);
			if (!facets.containsKey(taxon))
				facets.put(taxon, new HashMap<String, ArrayList<String>>());

			ArrayList<String> pair = new ArrayList<String>();
			pair.add(entryLabel);
			pair.add("" + numDocs);
			facets.get(taxon).put(entryCode, pair);
		}
		return facets;
	}

	private HashMap<String, String> buildStaticFacetGroup(FacetField facetField) {
		List<Count> values = facetField.getValues();
		Iterator<Count> itv = values.iterator();
		HashMap<String, String> facets = new HashMap<String, String>();
		while (itv.hasNext()) {
			FacetField.Count count = itv.next();
			facets.put(count.getName(), "" + count.getCount());
		}
		return facets;
	}

	private HashMap<String, String> buildStaticRangeFacetGroup(
			RangeFacet<?, ?> rangeFacet) {
		List<RangeFacet.Count> values = rangeFacet.getCounts();
		Iterator<RangeFacet.Count> itv = values.iterator();
		HashMap<String, String> facets = new HashMap<String, String>();
		while (itv.hasNext()) {
			RangeFacet.Count count = itv.next();
			facets.put(count.getValue(), "" + count.getCount());
		}
		return facets;
	}

	public HashMap<String, HashMap<String, String>> getStaticFacetGroups() {
		return staticFacetGroups;
	}

	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getDynamicFacetGroups() {
		return dynamicFacetGroups;
	}

	public HashMap<String, String> getRangefacetsGaps() {
		return rangefacetsGaps;
	}

	@Override
	public long getTotalResultsFound() {
		return totalResultsFound;
	}

}
