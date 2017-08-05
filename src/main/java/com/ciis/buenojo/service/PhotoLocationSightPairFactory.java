package com.ciis.buenojo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.domain.parsers.PhotoLocationSightPairCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.repository.PhotoLocationSightPairRepository;

@Service
public class PhotoLocationSightPairFactory {
	
	
	@Inject
	private PhotoLocationSightPairRepository sightPairRepository;
	
	@Inject 
	private PhotoLocationExerciseRepository exerciseRepository;
	
	
	@Transactional
	public List<PhotoLocationSightPair> sightPairsFromFileForExercise(MultipartFile file,Long exerciseId) throws BuenOjoInconsistencyException, IOException, BuenOjoCSVParserException{
		
		PhotoLocationExercise exercise = exerciseRepository.findOne(exerciseId);
		
		if (exercise == null){
			throw new BuenOjoInconsistencyException("no existe ejercicio de fotolocalización con el ID: "+exerciseId);
		}
		
		List<PhotoLocationSightPair> sightPairs = setSightPairs(exercise, file);
		sightPairs = sightPairRepository.save(sightPairs);
		
		exercise.setSightPairs(new HashSet<>(sightPairs));
		exerciseRepository.saveAndFlush(exercise);
		
		return sightPairs.stream().collect(Collectors.toList());
	}
	
	public List<PhotoLocationSightPair> setSightPairs(PhotoLocationExercise exercise, MultipartFile fromFile) throws IOException, BuenOjoCSVParserException{
		PhotoLocationSightPairCSVParser parser = new PhotoLocationSightPairCSVParser(fromFile);
		
		List<PhotoLocationSightPair> sightPairs = parser.parse();
		
		for (PhotoLocationSightPair photoLocationSightPair : sightPairs) {

			photoLocationSightPair.setExercise(exercise);
		}
		
		return new ArrayList<>(sightPairs);
	}
}


