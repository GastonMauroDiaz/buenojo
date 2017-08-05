package com.ciis.buenojo.web.rest.dto;

import java.util.List;

import com.ciis.buenojo.domain.enumeration.LoaderResult;

public class ExerciseDatasetLoaderResultDTO {

	
	public ExerciseDatasetLoaderResultDTO(ExerciseDataSetDTO dto){
		this.dataSet = dto;
	}
	
	private ExerciseDataSetDTO dataSet;
	private LoaderResult result;
	private String message;
	private List<Long> loadedExerciseIds;
	
	public ExerciseDataSetDTO getDataSet() {
		return dataSet;
	}
	public void setDataSet(ExerciseDataSetDTO dataSet) {
		this.dataSet = dataSet;
	}
	public LoaderResult getResult() {
		return result;
	}
	public void setResult(LoaderResult result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Long> getLoadedExerciseIds() {
		return loadedExerciseIds;
	}
	public void setLoadedExerciseIds(List<Long> loadedExerciseIds) {
		this.loadedExerciseIds = loadedExerciseIds;
	}
	
	
}
