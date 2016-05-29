package fr.ac_versailles.crdp.apiscol.meta.references;

public enum Source {
	LOM("LOMv1.0"), SCOLOMFR("SCOLOMFRv2.1");
	private String value;

	private Source(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
