package net.lehnert.tasks.jpa;

import java.util.*;

import javax.persistence.*;

@Entity
public class Task {
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Id private long id;
	private String title;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	@ManyToOne
	private Swimlane swimlane;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setSwimlane(Swimlane swimlane) {
		this.swimlane =swimlane;
	}
	public Swimlane getSwimlane() {
		return swimlane;
	}

}
