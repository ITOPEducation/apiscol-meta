package fr.ac_versailles.crdp.apiscol.meta.maintenance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineQueryHandler;

public class MaintenanceRegistry {

	private static Integer counter;
	private static Map<Integer, Thread> maintenanceRecoveries = new HashMap<Integer, Thread>();
	private static Map<Integer, MaintenanceRecoveryStates> maintenanceRecoveryStates = new HashMap<Integer, MaintenanceRecoveryStates>();
	private static Map<Integer, LinkedList<String>> messages = new HashMap<Integer, LinkedList<String>>();
	private int totalNumberOfDocuments = 0;
	private int numberOfDocumentsProcessed = 0;
	private int runninWorkerId;
	private boolean hasRunningWorker;

	public MaintenanceRegistry() {

		counter = 0;

	}

	public Integer newMaintenance(
			ISearchEngineQueryHandler searchEngineQueryHandler,
			IResourceDataHandler resourceDataHandler) {
		synchronized (counter) {
			counter++;
			setRunninWorkerId(counter);
			
			MaintenanceRecoveryWorker worker = new MaintenanceRecoveryWorker(
					counter, searchEngineQueryHandler, resourceDataHandler,
					this);
			Thread thread = new Thread(worker);
			thread.start();
			maintenanceRecoveries.put(counter, thread);
			messages.put(counter, new LinkedList<String>());
			setState(counter, MaintenanceRecoveryStates.initiated);
			messages.get(counter).add("Recovery process initiated");
			return counter;
		}
	}

	public MaintenanceRecoveryStates getState(Integer maintenanceRecoveryId) {
		if (!maintenanceRecoveryStates.containsKey(maintenanceRecoveryId))
			return MaintenanceRecoveryStates.unknown;
		return maintenanceRecoveryStates.get(maintenanceRecoveryId);
	}

	public void setState(Integer maintenanceRecoveryId,
			MaintenanceRecoveryStates state) {
		maintenanceRecoveryStates.put(maintenanceRecoveryId, state);
	}

	public LinkedList<String> getMessages(Integer maintenanceRecoveryId) {
		if (!messages.containsKey(maintenanceRecoveryId)) {
			LinkedList<String> errorMessages = new LinkedList<String>();
			errorMessages
					.add(String
							.format("The maintenance recovery operation %d is unknown from the system",
									maintenanceRecoveryId));
			return errorMessages;
		}
		return messages.get(maintenanceRecoveryId);
	}

	public void addMessage(String message, Integer identifier) {
		messages.get(identifier).add(message);

	}

	public int getTotalNumberOfDocuments() {
		return totalNumberOfDocuments;
	}

	public void setTotalNumberOfDocuments(int totalNumberOfDocuments) {
		this.totalNumberOfDocuments = totalNumberOfDocuments;
	}

	public int getNumberOfDocumentsProcessed() {
		return numberOfDocumentsProcessed;
	}

	public void setNumberOfDocumentsProcessed(int numberOfDocumentsProcessed) {
		this.numberOfDocumentsProcessed = numberOfDocumentsProcessed;
	}

	public float getPercentageOfDocumentProcessed() {
		if (this.totalNumberOfDocuments == 0)
			return 0;
		return (float) this.numberOfDocumentsProcessed
				/ (float) this.totalNumberOfDocuments;
	}

	public int getRunninWorkerId() {
		return runninWorkerId;
	}

	public void setRunninWorkerId(int runninWorkerId) {
		this.runninWorkerId = runninWorkerId;
	}

	public boolean hasRunningWorker() {
		return hasRunningWorker;
	}

	public void setHasRunningWorker(boolean hasRunningWorker) {
		this.hasRunningWorker = hasRunningWorker;
	}

}
