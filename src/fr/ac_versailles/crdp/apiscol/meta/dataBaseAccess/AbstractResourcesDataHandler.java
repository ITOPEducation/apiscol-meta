package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.util.Map;

import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public abstract class AbstractResourcesDataHandler implements
		IResourceDataHandler {
	protected static Logger logger;

	public enum MetadataProperties {
		title("title"), description("description"), contentUrl("content-url"), contentRestUrl(
				"content-rest-url"), contentMime("content-mime"), icon("icon"), author(
				"author"), aggregationLevel("agregation-level");
		private String value;

		private MetadataProperties(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public AbstractResourcesDataHandler(Map<String, String> dbParams)
			throws DBAccessException {
		createLogger();
		dbConnect(dbParams);
	}

	@Override
	public void deInitialize() {
		dbDisconnect();

	}

	protected abstract void dbDisconnect();

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());

	}

	abstract protected void dbConnect(Map<String, String> dbParams)
			throws DBAccessException;
}
