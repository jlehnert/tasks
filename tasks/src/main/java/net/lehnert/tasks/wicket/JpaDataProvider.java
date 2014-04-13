package net.lehnert.tasks.wicket;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.apache.wicket.markup.repeater.data.*;
import org.apache.wicket.model.*;

public class JpaDataProvider<T extends Object> implements IDataProvider<T> {

	private static final long serialVersionUID = 1L;
	private Class<T> clazz;
	private CriteriaQuery<T> cq;
	private TypedQuery<Long> countQuery;

	public JpaDataProvider(Class<T> clazz, CriteriaQuery<T> criteriaQuery) {
		this.clazz = clazz;
		this.cq = criteriaQuery;
		createCountQuery();
	}

	public JpaDataProvider(Class<T> clazz) {
		this.clazz = clazz;
		EntityManager entityManager = OpenEntityManager.getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        createCountQuery();
	}

	private void createCountQuery() {
		EntityManager entityManager = OpenEntityManager.getEntityManager();
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		countCriteria.select(builder.count(cq.getRoots().iterator().next()));
		final Predicate groupRestriction = cq.getGroupRestriction();
		final Predicate fromRestriction = cq.getRestriction();
		if (groupRestriction != null) {
			countCriteria.having(groupRestriction);
		}
		if (fromRestriction != null) {
			countCriteria.where(fromRestriction);
		}
		countCriteria.groupBy(cq.getGroupList());
		countCriteria.distinct(cq.isDistinct());
		countQuery = entityManager.createQuery(countCriteria);
	}
	
	
	@Override
	public void detach() {
		// NOP
	}

	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		EntityManager entityManager = OpenEntityManager.getEntityManager();
		return entityManager.createQuery(cq).getResultList().iterator();
		// cq.
		//
		// Query query =
		// entityManager.createQuery(queryString).setFirstResult((int)first).setMaxResults((int)count);
		// //broken: why int?!
		// return query.getResultList().iterator();
	}

	@Override
	public long size() {
		// String countQuery = "select count(" + "e" + ") from " +
		// clazz.getName() + " " + "e";
		// EntityManager entityManager = OpenEntityManager.getEntityManager();
		// Root<T> root = (Root<T>) cq.getRoots().iterator().next();
		// entityManager.getCriteriaBuilder().count(root);
		//
		// cq.getSelection().
		// Query query = entityManager.createQuery(countQuery);
		// return ((Long)query.getSingleResult()).longValue();

		
//		return entityManager.createQuery(countCriteria).getSingleResult();
		return countQuery.getSingleResult();
	}

	@Override
	public IModel<T> model(T object) {
		return new JpaLoadableDetachableModel<T>(clazz, object);
	}

	
}
