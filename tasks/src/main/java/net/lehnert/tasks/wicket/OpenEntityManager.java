package net.lehnert.tasks.wicket;

import java.util.*;

import javax.persistence.*;

import org.apache.wicket.request.cycle.*;

public class OpenEntityManager {

	public static EntityManager getEntityManager() {
		Iterator<IRequestCycleListener> listenerIterator = RequestCycle.get().getListeners().iterator();

		while (listenerIterator.hasNext()) {
			IRequestCycleListener listener = listenerIterator.next();

			if (listener instanceof RequestCycleListenerCollection) {
				Iterator<IRequestCycleListener> iterator = ((RequestCycleListenerCollection) listener).iterator();

				while (iterator.hasNext()) {
					IRequestCycleListener appListener = iterator.next();

					if (appListener instanceof OpenEntityManagerListener) {

						return ((OpenEntityManagerListener) appListener).getEntityManager();

					}
				}
			}
		}
		return null;
	}
}