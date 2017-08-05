package com.ciis.buenojo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.domain.PhotoLocationExerciseGame;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;

@Service
public class PhotoLocationExerciseFactory {
	
	@Inject 
	private PhotoLocationExerciseRepository photoLocationExerciseRepository;
	@Inject 
	private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;
	
	public PhotoLocationExerciseGame exerciseGameModelForExercise(Long exerciseId) throws BuenOjoCSVParserException, BuenOjoInconsistencyException{
		
		PhotoLocationExercise exercise = photoLocationExerciseRepository.findOneWithEagerRelationships(exerciseId);
		
		if (exercise == null){
			throw new BuenOjoInconsistencyException("No existe el ejercicio de fotolocalización: "+exerciseId);
			
		}
		
		List<PhotoLocationImage> extraImages = annotatedResourceFactory.getExtraImagesForExercise(exerciseId);
		
		extraImages.add(exercise.getTerrainPhoto());
		
		
		List<PhotoLocationSatelliteImage> extraSatelliteImages = annotatedResourceFactory.getExtraSatelliteImagesForExercise(exerciseId);
		ArrayList<PhotoLocationSatelliteImage> realSatelliteImages = new ArrayList<>();
		
		for (PhotoLocationSatelliteImage photoLocationSatelliteImage : exercise.getSatelliteImages()) {
			if (photoLocationSatelliteImage.getSatelliteImage().getImageType() != SatelliteImageType.EsriTopo){
				realSatelliteImages.add(photoLocationSatelliteImage);
				
			}
		}
		
		if (realSatelliteImages.size() > 1){
			throw new BuenOjoInconsistencyException("hay mas de una imagen satelital no EsriTopo en este ejericio");
			
		} else if (realSatelliteImages.size() == 0){
			throw new BuenOjoInconsistencyException("no hay imágenes satelitales asignadas a este ejercicio");
		}
		
		extraSatelliteImages.addAll(realSatelliteImages);
		
		
		// shuffle the images
		Collections.shuffle(extraSatelliteImages);
		
		Collections.shuffle(extraImages);
		
		
		PhotoLocationExerciseGame game = new PhotoLocationExerciseGame(exercise);
		game.setSatelliteSlides(new HashSet<PhotoLocationSatelliteImage>(extraSatelliteImages));
		game.setTerrainSlides(new HashSet<>(extraImages));
		
		
		return game ;
		
	}
	
	
}
