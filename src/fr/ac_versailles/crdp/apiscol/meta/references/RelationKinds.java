package fr.ac_versailles.crdp.apiscol.meta.references;

import org.apache.commons.lang.StringUtils;

public enum RelationKinds {
	VIGNETTE(
			"http://data.education.fr/voc/scolomfr/concept/scolomfr-voc-009-num-021"), APERCU(
			"http://data.education.fr/voc/scolomfr/concept/scolomfr-voc-009-num-023"), CONTIENT(
			"http://purl.org/dc/terms/hasPart"), FAIT_PARTIE_DE(
			"http://purl.org/dc/terms/isPartOf"), REQUIERT(
			"http://purl.org/dc/terms/requires"), EST_REQUIS_PAR(
			"http://purl.org/dc/terms/isRequiredBy");
	private String value;

	private RelationKinds(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static RelationKinds getFromString(String value) {
		for (RelationKinds rk : RelationKinds.values()) {
			if (StringUtils.equals(rk.toString(), value))
				return rk;
		}
		return null;
	}

}
