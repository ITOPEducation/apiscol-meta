package fr.ac_versailles.crdp.apiscol.meta.maintenance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.solr.common.util.Pair;

import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.maintenance.MaintenanceRegistry.MessageTypes;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineQueryHandler;

public class MaintenanceRegistry {

	private static Integer counter;
	private static Map<Integer, Thread> maintenanceRecoveries = new HashMap<Integer, Thread>();
	private static Map<Integer, MaintenanceRecoveryStates> maintenanceRecoveryStates = new HashMap<Integer, MaintenanceRecoveryStates>();
	private static Map<Integer, LinkedList<Pair<String, MessageTypes>>> messages = new HashMap<Integer, LinkedList<Pair<String, MaintenanceRegistry.MessageTypes>>>();
	private int totalNumberOfDocuments = 0;
	private int numberOfDocumentsProcessed = 0;
	private int runninWorkerId;
	private boolean hasRunningWorker;

	public MaintenanceRegistry() {
		counter = 0;
	}

	public synchronized Integer newMaintenance(
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
			messages.put(counter, new LinkedList<Pair<String, MessageTypes>>());
			setState(counter, MaintenanceRecoveryStates.initiated);
			messages.get(counter)
					.add(new Pair<String, MaintenanceRegistry.MessageTypes>(
							"Recovery process initiated", MessageTypes.infoType));
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

	public LinkedList<Pair<String, MessageTypes>> getMessages(
			Integer maintenanceRecoveryId) {
		if (!messages.containsKey(maintenanceRecoveryId)) {
			LinkedList<Pair<String, MessageTypes>> errorMessages = new LinkedList<Pair<String, MessageTypes>>();
			Pair<String, MessageTypes> pair = new Pair<String, MaintenanceRegistry.MessageTypes>(
					String.format(
							"The maintenance recovery operation %d is unknown from the system",
							maintenanceRecoveryId), MessageTypes.errorType);
			errorMessages.add(pair);
			return errorMessages;
		}
		return messages.get(maintenanceRecoveryId);
	}

	public void addMessage(String message, Integer identifier) {

		addMessage(message, identifier, MessageTypes.infoType);
	}

	public void addMessage(String message, Integer identifier, MessageTypes type) {
		Pair<String, MessageTypes> pair = new Pair<String, MaintenanceRegistry.MessageTypes>(
				message, type);
		messages.get(identifier).add(pair);
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

	public synchronized boolean hasRunningWorker() {
		return hasRunningWorker;
	}

	public void setHasRunningWorker(boolean hasRunningWorker) {
		this.hasRunningWorker = hasRunningWorker;
	}

	public enum MessageTypes {
		errorType("error_type"), warningType("warning_type"), infoType(
				"info_type");
		private String value;

		private MessageTypes(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

}
