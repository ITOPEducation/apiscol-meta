package fr.ac_versailles.crdp.apiscol.meta.scolomfr;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.utils.LogUtility;
import fr.apiscol.metadata.scolomfr3utils.Scolomfr3Utils;

public class ScolomfrUtilsInitializer implements ServletContextListener {

	public static final String ENVIRONMENT_PARAMETER_KEY = "scolomfr3utils";
	private Logger logger;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// nothing
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		createLogger();
		Scolomfr3Utils scolomfrUtils = new Scolomfr3Utils();
		//TODO move to config
		scolomfrUtils.setScolomfrVersion("3.0");
		ServletContext context = servletContextEvent.getServletContext();
		context.setAttribute(ScolomfrUtilsInitializer.ENVIRONMENT_PARAMETER_KEY,
				scolomfrUtils);

	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());
	}
}
