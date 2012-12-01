package org.klarblick.tomcat;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.server.impl.InitialContextHelper;
import com.sun.jersey.server.impl.cdi.CDIComponentProviderFactory;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.container.servlet.WebConfig;

public class TomcatCDIServlet extends ServletContainer {

	static {
		System.setProperty(
				"com.sun.jersey.server.impl.cdi.lookupExtensionInBeanManager",
				"true");
	}

	private static final long serialVersionUID = 4060402665350370288L;

	private static final Logger LOG = Logger.getLogger(TomcatCDIServlet.class
			.getName());

	@Override
	protected void configure(WebConfig wc, ResourceConfig rc, WebApplication wa) {
		super.configure(wc, rc, wa);
		InitialContext ic = InitialContextHelper.getInitialContext();
		if (ic == null)
			return;

		try {
			Object beanManager = ic.lookup("java:comp/env/BeanManager");
			if (beanManager == null) {
				LOG.info("The CDI BeanManager is not available.  JAX-RS CDI  support is disabled");
				return;
			}

			rc.getSingletons().add(
					new CDIComponentProviderFactory(beanManager, rc, wa));
			LOG.info("JAX-RS CDI support is enabled");
		} catch (NamingException e) {
			LOG.log(Level.WARNING,
					"The CDI BeanManager is not available.  JAX-RS CDI support is disabled.",
					e);
		}
	}
}