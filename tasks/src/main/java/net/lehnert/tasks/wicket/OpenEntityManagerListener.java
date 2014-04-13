package net.lehnert.tasks.wicket;

import javax.persistence.*;

import org.apache.wicket.*;
import org.apache.wicket.request.*;
import org.apache.wicket.request.cycle.*;

public class OpenEntityManagerListener implements IRequestCycleListener {

	private ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		EntityManagerFactory entityManagerFactory = ((WicketApplication) Application.get()).getEntityManagerFactory();
		entityManagerThreadLocal.set(entityManagerFactory.createEntityManager());
		getEntityManager().getTransaction().begin();
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {
		EntityManager entityManager = entityManagerThreadLocal.get();
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	@Override
	public void onDetach(RequestCycle cycle) {
		entityManagerThreadLocal.remove();
	}

	public EntityManager getEntityManager() {
		synchronized (entityManagerThreadLocal) {
			if (entityManagerThreadLocal.get() == null) {
				EntityManagerFactory entityManagerFactory = ((WicketApplication) Application.get()).getEntityManagerFactory();
				entityManagerThreadLocal.set(entityManagerFactory.createEntityManager());
				getEntityManager().getTransaction().begin();
			}
			return entityManagerThreadLocal.get();
		}

	}

	@Override
	public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onExceptionRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler, Exception exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
		// TODO Auto-generated method stub

	}

}
