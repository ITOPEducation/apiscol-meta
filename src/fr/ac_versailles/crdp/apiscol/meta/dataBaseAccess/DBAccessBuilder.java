package fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess;

import java.util.Map;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.apiscol.metadata.scolomfr3utils.Scolomfr3Utils;

public class DBAccessBuilder {

	private static DBTypes dbType;
	private static IResourceDataHandler resourceDataHandler;
	private static Map<String, String> parameters;
	private Scolomfr3Utils scolomfrUtils;

	public enum DBTypes {
		mongoDB("mongodb");
		private String value;

		private DBTypes(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public IResourceDataHandler build() throws DBAccessException {
		if (resourceDataHandler != null)
			return resourceDataHandler;
		if (dbType == null)
			throw new DBAccessException(
					"Set dbType before asking for connexion");
		if (parameters == null)
			throw new DBAccessException("Please provide connexion parameters");
		switch (dbType) {
		case mongoDB:
			resourceDataHandler = new MongoResourceDataHandler(parameters);
			if (scolomfrUtils != null) {
				resourceDataHandler.setScolomfrUtils(scolomfrUtils);

			}
			return resourceDataHandler;
		default:
			throw new DBAccessException(String.format(
					"Type of connexion %s not implemented", dbType.toString()));
		}
	}

	public DBAccessBuilder setDbType(DBTypes dbType) {
		DBAccessBuilder.dbType = dbType;
		return this;
	}

	public DBAccessBuilder setParameters(Map<String, String> parameters) {
		DBAccessBuilder.parameters = parameters;
		return this;
	}

	public static void deinitialize() {
		if (resourceDataHandler != null)
			resourceDataHandler.deInitialize();

	}

	public DBAccessBuilder setScolomfrUtils(Scolomfr3Utils scolomfrUtils) {
		this.scolomfrUtils = scolomfrUtils;
		return this;
	}
}
