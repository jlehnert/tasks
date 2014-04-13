package net.lehnert.tasks.wicket;

import java.lang.reflect.*;

import javax.persistence.*;

import org.apache.wicket.model.*;

public class JpaLoadableDetachableModel<T> extends LoadableDetachableModel<T> {

	private static final long serialVersionUID = 1L;
	private long key;
	private Class<T> clazz;

	public JpaLoadableDetachableModel(Class<T> clazz, T object) {
		super(object);
		this.clazz = clazz;
		if (object != null) {
			try {
				Method method = object.getClass().getMethod("getId", new Class<?>[] {});
				key = (Long) method.invoke(object, new Object[] {});
			} catch (SecurityException e) {
				throw new Error(e);
			} catch (IllegalArgumentException e) {
				throw new Error(e);
			} catch (NoSuchMethodException e) {
				throw new Error(e);
			} catch (IllegalAccessException e) {
				throw new Error(e);
			} catch (InvocationTargetException e) {
				throw new Error(e);
			}
		}
	}

	@Override
	protected T load() {
		EntityManager entityManager = OpenEntityManager.getEntityManager();
		return entityManager.find(clazz, key);
	}
}
