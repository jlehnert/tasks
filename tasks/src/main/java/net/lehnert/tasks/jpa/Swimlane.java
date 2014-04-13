package net.lehnert.tasks.jpa;

import java.util.*;

import javax.persistence.*;

@Entity
public class Swimlane {
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Id private long id;
	private String name;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Task> tasks;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTask(Task task) {
		if (this.tasks == null) {
			this.tasks = new ArrayList<Task>();
		}
		this.tasks.add(task);
	}
}
