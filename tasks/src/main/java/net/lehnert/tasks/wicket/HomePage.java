package net.lehnert.tasks.wicket;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;

import net.lehnert.tasks.jpa.*;

import org.apache.log4j.*;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.*;
import org.apache.wicket.markup.html.basic.*;
import org.apache.wicket.markup.html.list.*;
import org.apache.wicket.markup.repeater.*;
import org.apache.wicket.markup.repeater.data.*;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.*;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HomePage.class);

	public HomePage(final PageParameters parameters) {
		super(parameters);

		final EntityManager entityManager = OpenEntityManager.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Task> root = criteriaQuery.from(Task.class);
		Expression<Long> count = criteriaBuilder.count(root);
		criteriaQuery.select(count);	
		long c = entityManager.createQuery(criteriaQuery).getSingleResult();
		
		if (c == 0) {
			createData();
		}
		

		
//		add(new Label("version", getApplication().getFrameworkSettings().getVersion() + " FOO "+c));

//		ListView<Task> taskView = new ListView<Task>("tasks", new LoadableDetachableModel<List<Task>>() {
//
//			@Override
//			protected List<Task> load() {
//				EntityManager entityManager = OpenEntityManager.getEntityManager();
//				CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//				CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
//				Root<Task> root = criteriaQuery.from(Task.class);
//				criteriaQuery.select(root);
//				return entityManager.createQuery(criteriaQuery).getResultList();
//			}
//		}) {
//
//			@Override
//			protected void populateItem(ListItem<Task> item) {
//				item.add(new Label("entry", item.getModelObject().getTitle()));
//			}
//			
//		};
//		add(taskView);
		// TODO Add your page's components here

		
		
		JpaDataProvider<Swimlane> swimlaneProvider = new JpaDataProvider<Swimlane>(Swimlane.class);		
		DataView<Swimlane> swimlaneView = new DataView<Swimlane>("swimlanes", swimlaneProvider) {
			@Override
			protected void populateItem(Item<Swimlane> item) {
                Swimlane swimlane = item.getModelObject();
                item.add(new Label("name", swimlane.getName()));
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Task> cq = cb.createQuery(Task.class);
                Root<Task> root = cq.from(Task.class);
                cq.where(cb.equal(root.get(Task_.swimlane), swimlane));
                JpaDataProvider<Task> taskProvider = new JpaDataProvider<Task>(Task.class, cq);
                DataView<Task> taskView = new DataView<Task>("tasks", taskProvider) {
					@Override
					protected void populateItem(Item<Task> item) {
						Task task = item.getModelObject();
						item.add(new Label("title", task.getTitle()));
					}
                };
                item.add(taskView);                
			}			
		};
		add(swimlaneView);
		
		
    }
	
	private void createData() {
		logger.debug("Creating dummy data...");
		EntityManager entityManager = OpenEntityManager.getEntityManager();
		String[] swimlanes = new String[] { "AI-intern", "HW", "Gremien", "audimex" };
		int counter = 1;
		for(String name: swimlanes) {
			Swimlane swimlane = new Swimlane();
			swimlane.setName(name);
			int count = (int)(Math.random()*10+1);
			logger.debug("Creating swimlane "+name+" with "+count+" tasks");
			for(int i=0; i<count;i++) {
				Task task = new Task();
				task.setTitle("Task "+counter);
				counter++;
				task.setDescription("Some description");
				task.setDueDate(new Date());
				swimlane.addTask(task);
				task.setSwimlane(swimlane);
			}
			entityManager.persist(swimlane);
		}
	}
}
