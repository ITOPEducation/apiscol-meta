package fr.ac_versailles.crdp.apiscol.meta.references;

public enum RelationKinds {
	VIGNETTE("a pour vignette"),APERCU("a pour aperçu"),CONTIENT("contient"),FAIT_PARTIE_DE("est une partie de");
	private String value;

	private RelationKinds(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
