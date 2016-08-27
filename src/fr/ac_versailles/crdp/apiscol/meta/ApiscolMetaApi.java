package fr.ac_versailles.crdp.apiscol.meta;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import fr.ac_versailles.crdp.apiscol.ApiscolApi;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.FileSystemAccessException;
import fr.ac_versailles.crdp.apiscol.meta.scolomfr.ScolomfrUtilsInitializer;
import fr.apiscol.metadata.scolomfr3utils.Scolomfr3Utils;

public class ApiscolMetaApi extends ApiscolApi {

	protected static Scolomfr3Utils scolomfrUtils;

	public ApiscolMetaApi(@Context ServletContext context)
			throws FileSystemAccessException {
		super(context);
	}

	protected void fetchScolomfrUtils(ServletContext context) {
		scolomfrUtils = (Scolomfr3Utils) context
				.getAttribute(ScolomfrUtilsInitializer.ENVIRONMENT_PARAMETER_KEY);
		if (!(scolomfrUtils instanceof Scolomfr3Utils)) {
			getLogger()
					.error("Impossible to fetch instance of SkosVocabulary from servlet context");
		}
		getLogger()
				.info("Successfully fetched instance of SkosVocabulary from servlet context");
	}

}
