package com.ciis.buenojo.web.rest.dto;

import com.ciis.buenojo.domain.enumeration.ExerciseType;

public class ExerciseDataSetDTO {

	private String path; 
	private String name;
	private ExerciseType type;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ExerciseType getType() {
		return type;
	}
	public void setType(ExerciseType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ExerciseDataSetDTO [path=" + path + ", name=" + name + ", type=" + type + "]";
	} 
	
}
