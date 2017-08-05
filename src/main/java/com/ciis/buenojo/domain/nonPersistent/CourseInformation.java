package com.ciis.buenojo.domain.nonPersistent;

public class CourseInformation {
private Long exerciseCount;
private Long approvedExercises;
private Double approvedPercentage;
private Integer passedStages;
private Integer courseStages;
private Integer currentStage;
private Integer remainingStages;

private Integer passedLevels;


public Long getExerciseCount() {
	return exerciseCount;
}
public void setExerciseCount(Long exerciseCount) {
	this.exerciseCount = exerciseCount;
}
public Double getApprovedPercentage() {
	return approvedPercentage;
}
public void setApprovedPercentage(Double approvedPercentage) {
	this.approvedPercentage = approvedPercentage;
}
public Integer getPassedStages() {
	return passedStages;
}
public void setPassedStages(Integer passedStages) {
	this.passedStages = passedStages;
}
public Long getApprovedExercises() {
	return approvedExercises;
}
public void setApprovedExercises(Long approvedExercises) {
	this.approvedExercises = approvedExercises;
}
public Integer getCourseStages() {
	return courseStages;
}
public void setCourseStages(Integer courseStages) {
	this.courseStages = courseStages;
}
public Integer getCurrentStage() {
	return currentStage;
}
public void setCurrentStage(Integer currentStage) {
	this.currentStage = currentStage;
}
public Integer getPassedLevels() {
	return passedLevels;
}
public void setPassedLevels(Integer passedLevels) {
	this.passedLevels = passedLevels;
}
public Integer getRemainingStages() {
	return remainingStages;
}
public void setRemainingStages(Integer remainigStages) {
	this.remainingStages = remainigStages;
}
public CourseInformation(Long exerciseCount, Double approvedPercentage,Long approvedExercises) {
	super();
	this.exerciseCount = exerciseCount;
	this.approvedExercises = approvedExercises;
	this.approvedPercentage = approvedPercentage;
}


}
