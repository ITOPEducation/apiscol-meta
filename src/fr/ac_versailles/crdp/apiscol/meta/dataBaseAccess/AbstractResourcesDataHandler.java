package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.util.Map;

import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;
import fr.apiscol.metadata.scolomfr3utils.IScolomfr3Utils;

public abstract class AbstractResourcesDataHandler implements
		IResourceDataHandler {
	protected static Logger logger;
	protected IScolomfr3Utils scolomfrUtils;

	public enum MetadataProperties {
		title("title"), description("description"), arkIdentifier("ark_identifier"), keyword("keyword"), contentUrl(
				"content-url"), contentRestUrl("content-rest-url"), contentMime(
				"content-mime"), icon("icon"), author("author"), contributor(
				"contributor"), aggregationLevel("agregation-level"), aggregationLevelLabel(
				"agregation-level-label"), educationalResourceType(
				"educational_resource_type"), educationalResourceTypeTitle(
				"educational_resource_type_title"), parentUri("parent_uri"), parentTitle(
				"parent_title"), separator("::::");
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

	@Override
	public void setScolomfrUtils(IScolomfr3Utils scolomfrUtils) {
		this.scolomfrUtils = scolomfrUtils;
	}
}
