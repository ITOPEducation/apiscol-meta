package fr.ac_versailles.crdp.apiscol.meta;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.Application;

import com.sun.jersey.spi.container.servlet.ServletContainer;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessFactory;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.DBAccessFactory.DBTypes;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;

public class ApiscolMeta extends ServletContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApiscolMeta() {

	}

	public ApiscolMeta(Class<? extends Application> appClass) {
		super(appClass);
	}

	public ApiscolMeta(Application app) {
		super(app);
	}

	@PreDestroy
	public void deinitialize() {
		DBAccessFactory.deinitialize();
	}

	@PostConstruct
	public void initialize() {
		// nothing at this time
	}
}
