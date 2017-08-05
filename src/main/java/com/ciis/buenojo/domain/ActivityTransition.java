package com.ciis.buenojo.domain;

public class ActivityTransition {
	
	private Activity previous;
	
	private Activity next;
	
	private Course course;
	
	private Boolean levelUp;
	
	private Boolean courseFinished;
		
	public ActivityTransition() {
		this.courseFinished = false;
		this.levelUp = false;
	}
	public Boolean getLevelUp() {
		return levelUp;
	}

	public void setLevelUp(Boolean levelUp) {
		this.levelUp = levelUp;
	}

	public Boolean getCourseFinished() {
		return courseFinished;
	}

	public void setCourseFinished(Boolean courseFinished) {
		this.courseFinished = courseFinished;
	}

	public Activity getPrevious() {
		return previous;
	}

	public void setPrevious(Activity previous) {
		this.previous = previous;
	}

	public Activity getNext() {
		return next;
	}

	public void setNext(Activity next) {
		this.next = next;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	public String toString(){
		return "ActivityTransition: previous: " +
					this.previous +
					" next: "+ this.next +
					" levelUp: " + this.levelUp +
					" courseFinished: " + this.courseFinished;
	}
	
}
