package com.ciis.buenojo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.LoadedExercise;
import com.ciis.buenojo.domain.LoaderTrace;
import com.ciis.buenojo.repository.ExerciseRepository;
import com.ciis.buenojo.repository.HangManGameContainerRepository;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.LoadedExerciseRepository;
import com.ciis.buenojo.repository.LoaderTraceRepository;
import com.ciis.buenojo.repository.MultipleChoiceExerciseContainerRepository;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;



@Service
public class LoaderTraceService {
	
	@Inject
	private LoadedExerciseRepository loadedExerciseRepository;
	
	@Inject 
	private LoaderTraceRepository traceRepository;
	
	@Inject
	private ExerciseRepository exerciseRepository;
	
	@Inject 
	private PhotoLocationExerciseRepository photoLocationRespository;
	
	@Inject
	private ImageCompletionExerciseRepository imageCompletionRepository;
	
	@Inject 
	private HangManGameContainerRepository hangManRepository;
	
	@Inject 
	private MultipleChoiceExerciseContainerRepository multipleChoiceRepository;
	
	public LoaderTrace addLoadedExercises(LoaderTrace trace, ExerciseDatasetLoaderResultDTO dto) {
	
		trace = traceRepository.save(trace);
		
		if (trace != null && dto.getLoadedExerciseIds() != null && !dto.getLoadedExerciseIds().isEmpty()) {
			List<LoadedExercise> loadedExercises = new ArrayList<LoadedExercise>(dto.getLoadedExerciseIds().size()); 
			for (Long id : dto.getLoadedExerciseIds()) {
				LoadedExercise e = new LoadedExercise();
				e.setExerciseId(id);
				e.setLoaderTrace(trace);
				loadedExercises.add(e);
				
			}
			
			loadedExercises = loadedExerciseRepository.save(loadedExercises);
			
			if (loadedExercises != null && !loadedExercises.isEmpty()){
				trace.setLoadedExercises(new HashSet<LoadedExercise>(loadedExercises));
			}
		}
		return traceRepository.save(trace);
	}
	
	public void deleteLoadedExercises(LoaderTrace trace){
		
		List<LoadedExercise> loadedExercises = loadedExerciseRepository.findAllByLoaderTrace(trace);
		
		for (LoadedExercise loadedExercise : loadedExercises) {
			Long id = loadedExercise.getExerciseId();
			loadedExerciseRepository.delete(loadedExercises);
		
			switch (trace.getLoaderType()) {
			case ImageCompletion:
				imageCompletionRepository.delete(id);
				break;
			case PhotoLocation:
				photoLocationRespository.delete(id);
				break;
			case MultipleChoice:
				multipleChoiceRepository.delete(id);
				break;
			case HangMan:
				hangManRepository.delete(id);
				break;
			}
			
		}
		traceRepository.delete(trace);
	}
	
	 
}
