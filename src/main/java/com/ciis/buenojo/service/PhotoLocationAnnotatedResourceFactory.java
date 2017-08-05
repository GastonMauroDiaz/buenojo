package com.ciis.buenojo.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.IKeywordAnnotated;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.domain.PhotoLocationExtraSatelliteImage;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.enumeration.PhotoLocationDifficulty;
import com.ciis.buenojo.domain.parsers.PhotoLocationExtraPhotosKeywordCSVParser;
import com.ciis.buenojo.domain.util.BuenOjoRandomUtils;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageResourceRepository;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraImageRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraSatelliteImageRepository;
import com.ciis.buenojo.repository.PhotoLocationImageRepository;
import com.ciis.buenojo.repository.PhotoLocationKeywordRepository;
import com.ciis.buenojo.repository.PhotoLocationSatelliteImageRepository;
import com.ciis.buenojo.repository.SatelliteImageRepository;

@Service	
@Transactional(rollbackOn = Exception.class)
public class PhotoLocationAnnotatedResourceFactory {
	
	@Inject
	private ImageResourceRepository imageRepository;
	
	@Inject 
	private SatelliteImageRepository satelliteImageRepository;
	
	@Inject
	private PhotoLocationKeywordRepository keywordRepository;
	
	@Inject 
	private PhotoLocationImageRepository photoLocationImageRepository;
	
	@Inject
	private PhotoLocationSatelliteImageRepository photoLocationSatelliteImageRepository;
	
	@Inject 
	private PhotoLocationExerciseRepository photoLocationExerciseRepository;
	
	@Inject 
	private PhotoLocationExtraImageRepository photoLocationExtraImageRepository;
	
	@Inject
	private PhotoLocationExtraSatelliteImageRepository photoLocationExtraSatelliteImageRepository;
	
	public PhotoLocationImage image(String name, List<String>keywords) throws BuenOjoCSVParserException {
	
		return image(name, keywords,null);
	}
	
	public PhotoLocationImage image(String name, List<String>keywords, Course course) throws BuenOjoCSVParserException {
		ImageResource image = imageRepository.findOneByName(name);
		
		if (image == null) {
			throw new BuenOjoCSVParserException("la imagen '"+name+"' no existe en la base de datos");
		}
		PhotoLocationImage img = new PhotoLocationImage();
		img.setImage(image);
		img = (PhotoLocationImage) this.addKeywords(img, keywords, course);
		
		return img;
	}
	public PhotoLocationSatelliteImage satelliteImage(String name, List<String>keywords, Course course) throws BuenOjoCSVParserException {
		SatelliteImage satImag = satelliteImageRepository.findOneByName(name);
		
		if (satImag == null) {
			throw new BuenOjoCSVParserException("la imagen satelital '"+name+"' no existe en la base de datos");
		}
		PhotoLocationSatelliteImage plSatImg = new PhotoLocationSatelliteImage();
		plSatImg.setSatelliteImage(satImag);
	
		return (PhotoLocationSatelliteImage)this.addKeywords(plSatImg, keywords, course);
	}
	
	public PhotoLocationSatelliteImage satelliteImage(String name, List<String>keywords) throws BuenOjoCSVParserException {
		return satelliteImage(name, keywords, null);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Set<PhotoLocationSatelliteImage> importSatelliteImageKeywordsFromCSV(MultipartFile file, Course course) throws IOException, BuenOjoCSVParserException {
		
		PhotoLocationExtraPhotosKeywordCSVParser parser = new PhotoLocationExtraPhotosKeywordCSVParser(file);
		
		Map <String,List<String>> map = parser.parse();
		ArrayList<PhotoLocationSatelliteImage> images = new ArrayList<>();
		for(String name : map.keySet()) {
			
			PhotoLocationSatelliteImage img = this.satelliteImage(name, map.get(name), course);
			images.add(img);
		}
		
		List<PhotoLocationSatelliteImage> plImages = photoLocationSatelliteImageRepository.save(images);
		return new HashSet<PhotoLocationSatelliteImage>(plImages);
		
	}
	@Transactional(rollbackOn = Exception.class)
	public Set<PhotoLocationSatelliteImage> importSatelliteImageKeywordsFromCSV(MultipartFile file) throws IOException, BuenOjoCSVParserException {
		return importSatelliteImageKeywordsFromCSV(file, null);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Set<PhotoLocationImage> importImageKeywordsFromCSV(MultipartFile file) throws IOException, BuenOjoCSVParserException {
		return importImageKeywordsFromCSV(file, null,true);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Set<PhotoLocationImage> importImageKeywordsFromCSV(MultipartFile file, Course course, boolean store) throws IOException, BuenOjoCSVParserException {
		
		PhotoLocationExtraPhotosKeywordCSVParser parser = new PhotoLocationExtraPhotosKeywordCSVParser(file);
		
		Map <String,List<String>> map = parser.parse();
		ArrayList<PhotoLocationImage> images = new ArrayList<>();
		for(String name : map.keySet()) {
			
			PhotoLocationImage img = this.image(name, map.get(name),course);
			images.add(img);
		}
		
		if (store) {
			
			List<PhotoLocationImage> imgs = photoLocationImageRepository.save(images);
			return new HashSet<PhotoLocationImage>(imgs);
		}
		return new HashSet<PhotoLocationImage>(images);
		
	}
	private IKeywordAnnotated addKeywords(IKeywordAnnotated annotatedResource, List<String> keywords, Course course) {
		List<PhotoLocationKeyword> plKeywords = null;
		if (course == null) {
			plKeywords = keywordRepository.findKeywordWithNames(keywords);
		}else {
			plKeywords = keywordRepository.findKeywordWithNames(keywords,course);
		}
		
		annotatedResource.setKeywords(new HashSet<>(plKeywords));
		return annotatedResource;
	}
	
	public List<PhotoLocationImage> getExtraImagesForExercise(Long exerciseId) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
		PhotoLocationExercise exercise = photoLocationExerciseRepository.findOne(exerciseId);
		
		if (exercise == null) {
			throw new BuenOjoCSVParserException("El ejercicio de fotolocalización "+ exerciseId +" no existe");
		}
		
		List<PhotoLocationImage> extraImages = getExtraImages(exercise);
		
		return extraImages;
	}
	
	public List<PhotoLocationSatelliteImage> getExtraSatelliteImagesForExercise(Long exerciseId) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
		PhotoLocationExercise exercise = photoLocationExerciseRepository.findOne(exerciseId);
		
		if (exercise == null) {
			throw new BuenOjoCSVParserException("El ejercicio de fotolocalización "+ exerciseId +" no existe");
		}
		
		List<PhotoLocationSatelliteImage> extraImages = getExtraSatelliteImages(exercise);
		
		return extraImages;
	}
	private List<PhotoLocationImage> getExtraImages(PhotoLocationExercise exercise) throws BuenOjoInconsistencyException {
		
		Course course = exercise.getCourse();
		if (course == null) {
			throw new BuenOjoInconsistencyException("El ejercicio de fotolocalización ID: '"+exercise.getId()+"'"+"no se encuentra asociado a ningún curso. no se pueden seleccionar imágenes extra para el mismo");
		}
		List<PhotoLocationExtraImage> extraImages = photoLocationExtraImageRepository.findAllByCourse(course);
		
		if (extraImages == null || extraImages.isEmpty()) {
			throw new BuenOjoInconsistencyException("No se encontraron imágenes extra para el curso ID:'"+course.getId()+"'");
		}
		ArrayList<IKeywordAnnotated> resources = this.annotatedResourcesFromExtraImagesList(extraImages);
		
		Optional<ArrayList<IKeywordAnnotated>> filteredResources = Optional.empty();
		
		if (exercise.getDifficulty() == PhotoLocationDifficulty.Intermediate){
			List<PhotoLocationExtraImage> filteredImages =  photoLocationExtraImageRepository.findByCourseAndKeywords(course, exercise.getLandscapeKeywords());
			
			filteredResources = Optional.ofNullable(this.annotatedResourcesFromExtraImagesList(filteredImages));
		}
		
		List<IKeywordAnnotated> filteredList =getExtraAnnotatedResources(exercise, resources, filteredResources, exercise.getDifficulty(), exercise.getExtraPhotosCount());
		
		return downCastImages(filteredList);
		
	}
private List<PhotoLocationSatelliteImage> getExtraSatelliteImages(PhotoLocationExercise exercise) throws BuenOjoInconsistencyException {
		
		Course course = exercise.getCourse();
		
		if (course == null) {
			throw new BuenOjoInconsistencyException("El ejercicio de fotolocalización ID: '"+exercise.getId()+"'"+"no se encuentra asociado a ningún curso. no se pueden seleccionar imágenes extra para el mismo");
		}
		
		List<PhotoLocationExtraSatelliteImage> extraImages = photoLocationExtraSatelliteImageRepository.findAllByCourse(course);
		ArrayList<IKeywordAnnotated> resources = this.annotatedResourcesFromExtraSatelliteImagesList(extraImages);
		
		Optional<ArrayList<IKeywordAnnotated>> filteredResources = Optional.empty();
		
		if (exercise.getDifficulty() == PhotoLocationDifficulty.Intermediate){
			List<PhotoLocationExtraImage> filteredImages =  photoLocationExtraImageRepository.findByCourseAndKeywords(course, exercise.getLandscapeKeywords());
			
			filteredResources = Optional.ofNullable(this.annotatedResourcesFromExtraImagesList(filteredImages));
		}
		
		List<IKeywordAnnotated> filteredList =getExtraAnnotatedResources(exercise, resources, filteredResources, exercise.getDifficulty(), 5);
		
		return downCastSatelliteImages(filteredList);
		
	}
	private ArrayList<IKeywordAnnotated> annotatedResourcesFromExtraImagesList(List<PhotoLocationExtraImage> extraImages) {
		ArrayList<IKeywordAnnotated> resources = new ArrayList<>(extraImages.size());
		for (PhotoLocationExtraImage photoLocationExtraImage : extraImages) {
			resources.add(photoLocationExtraImage.getImage());
		}
		return resources;
	}
	private ArrayList<IKeywordAnnotated> annotatedResourcesFromExtraSatelliteImagesList(List<PhotoLocationExtraSatelliteImage> extraImages) {
		ArrayList<IKeywordAnnotated> resources = new ArrayList<>(extraImages.size());
		for (PhotoLocationExtraSatelliteImage photoLocationExtraImage : extraImages) {
			resources.add(photoLocationExtraImage.getImage());
		}
		return resources;
	}
	private List<PhotoLocationImage> downCastImages(List<IKeywordAnnotated> r){
		List<PhotoLocationImage> imgs = new ArrayList<>();
		for (IKeywordAnnotated iKeywordAnnotated : r) {
			imgs.add((PhotoLocationImage)iKeywordAnnotated);
		}
		return imgs;
	}
	private List<PhotoLocationSatelliteImage> downCastSatelliteImages(List<IKeywordAnnotated> r){
		List<PhotoLocationSatelliteImage> imgs = new ArrayList<>();
		for (IKeywordAnnotated iKeywordAnnotated : r) {
			imgs.add((PhotoLocationSatelliteImage)iKeywordAnnotated);
		}
		return imgs;
	}
	
	/**
	 * Selects KeywordAnnotated resources based on a difficulty
	 * @param resources All resources for this exercise
	 * @param filteredResources filtered resources based on the keywords for this exercise, can be null
	 * @param difficulty
	 * @param count how many objects you want to have in the final collection
	 * @throws BuenOjoInconsistencyException 
	 * @returns a collection of filtered KeywordAnnotated resources
	 */
	private List<IKeywordAnnotated> getExtraAnnotatedResources(PhotoLocationExercise exercise,
															   ArrayList<IKeywordAnnotated> allResources,
															   Optional<ArrayList<IKeywordAnnotated>> filteredResources, 
															   PhotoLocationDifficulty difficulty, 
															   Integer count) throws BuenOjoInconsistencyException {
		
		switch (difficulty) {
		case Beginner:
			return this.getAllRandomImages(allResources, count);
			
		case Intermediate:
			
			return this.getIntermediateResources(exercise, allResources, filteredResources.get(), count);
			
		case Advanced:
			return this.getAllRandomImages(filteredResources.get(), count);

		default:
			throw new BuenOjoInconsistencyException("la dificultad "+difficulty+"es inválida");
			
		}
	}
	
	private ArrayList<IKeywordAnnotated> getAllRandomImages(ArrayList<IKeywordAnnotated> resources, Integer count ){
		Random r = new Random();
		IKeywordAnnotated allResources[] = resources.toArray(new IKeywordAnnotated[resources.size()]);
		IKeywordAnnotated randomResources [] = BuenOjoRandomUtils.pickSample(allResources, count, r);
		ArrayList<IKeywordAnnotated> randomList = new ArrayList<>(randomResources.length);
		Collections.addAll(randomList, randomResources);
		return randomList;
	}
	/**
	 * 
	 * @param allResources
	 * @param filteredResorces
	 * @param filteredResourcesRatio
	 * @param count
	 * @return
	 */
	private ArrayList<IKeywordAnnotated>shuffleWithRandomAndKeywordFilteredImages(ArrayList<IKeywordAnnotated>allResources,ArrayList<IKeywordAnnotated> filteredResources, Float filteredResourcesRatio, Integer count){
		Integer randomCount = Math.round(count *filteredResourcesRatio);
		Integer filteredCount = Math.round(count * (1-filteredResourcesRatio));
		Random random = new Random ();
		
		IKeywordAnnotated randomResources[] = new IKeywordAnnotated[randomCount];
		randomResources = BuenOjoRandomUtils.pickSample(randomResources, randomCount, random);
		
		IKeywordAnnotated filteredResourcesArray[] = filteredResources.toArray(new IKeywordAnnotated[filteredResources.size()]);
		filteredResourcesArray = BuenOjoRandomUtils.pickSample(filteredResourcesArray, filteredCount, random);
		
		ArrayList<IKeywordAnnotated> result = new ArrayList<>(count);
		Collections.addAll(result, randomResources);
		Collections.addAll(result, filteredResourcesArray);
		Collections.shuffle(result,random);
		
		return result;
		
	}
	
	private ArrayList<IKeywordAnnotated> getIntermediateResources(PhotoLocationExercise exercise,
															      ArrayList<IKeywordAnnotated> filteredResources,
															      ArrayList<IKeywordAnnotated> allResources,
															      Integer count){
		
		return this.shuffleWithRandomAndKeywordFilteredImages(allResources, filteredResources, new Float(0.5), count);
	}
}
