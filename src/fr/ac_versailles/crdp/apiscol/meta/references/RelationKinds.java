package fr.ac_versailles.crdp.apiscol.meta.references;

import org.apache.commons.lang.StringUtils;

public enum RelationKinds {
	VIGNETTE("a pour vignette"), APERCU("a pour aperçu"), CONTIENT("contient"), FAIT_PARTIE_DE(
			"est une partie de"), REQUIERT("requiert"), EST_REQUIS_PAR(
			"est requis par");
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
