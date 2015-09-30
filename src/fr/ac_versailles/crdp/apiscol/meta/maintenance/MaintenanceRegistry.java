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

	public MaintenanceRegistry() {

		counter = 0;

	}

	public Integer newMaintenance(
			ISearchEngineQueryHandler searchEngineQueryHandler,
			IResourceDataHandler resourceDataHandler) {
		synchronized (counter) {
			counter++;
			MaintenanceRecoveryWorker worker = new MaintenanceRecoveryWorker(
					counter, searchEngineQueryHandler, resourceDataHandler,
					this);
			Thread thread = new Thread(worker);
			thread.start();
			maintenanceRecoveries.put(counter, thread);
			messages.put(counter, new LinkedList<String>());
			maintenanceRecoveryStates.put(counter,
					MaintenanceRecoveryStates.initiated);
			messages.get(counter).add("Recovery process initiated");
			return counter;
		}
	}

	public void notifyParsingSuccess(Integer identifier) {
		maintenanceRecoveries.put(identifier, null);
		maintenanceRecoveryStates.put(identifier,
				MaintenanceRecoveryStates.done);
		messages.get(identifier).add("Recovery process successful");

	}

	public MaintenanceRecoveryStates getTransferState(Integer urlParsingId) {
		if (!maintenanceRecoveryStates.containsKey(urlParsingId))
			return MaintenanceRecoveryStates.unknown;
		return maintenanceRecoveryStates.get(urlParsingId);
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

}
