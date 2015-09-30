package fr.ac_versailles.crdp.apiscol.meta.maintenance;

public enum MaintenanceRecoveryStates {
	initiated("initiated"), aborted("aborted"), pending(
			"pending"), unknown("unknown"), done("done");
	private String value;

	private MaintenanceRecoveryStates(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
