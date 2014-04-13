package net.lehnert.tasks.wicket;

import javax.persistence.*;

import org.apache.wicket.markup.html.*;
import org.apache.wicket.protocol.http.*;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see net.lehnert.tasks.wicket.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

	EntityManagerFactory entityManagerFactory;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();
		entityManagerFactory = Persistence.createEntityManagerFactory("persistence_unit");
		getRequestCycleListeners().add(new OpenEntityManagerListener());
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
}
