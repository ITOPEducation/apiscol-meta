package fr.ac_versailles.crdp.apiscol.meta.maintenance;

public enum MaintenanceRecoveryStates {
	initiated("initiated"), aborted("aborted"), content_deleted("content_deleted"), recovery_running(
			"recovery_running"), done("done"), unknown("unknown");
	private String value;

	private MaintenanceRecoveryStates(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
