package com.ciis.buenojo.loaders;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.TagCircle;
import com.ciis.buenojo.domain.enumeration.LoaderResult;
import com.ciis.buenojo.domain.enumeration.LoaderType;
import com.ciis.buenojo.domain.factories.SatelliteImageFactory;
import com.ciis.buenojo.domain.parsers.TagCircleCSVParser;
import com.ciis.buenojo.domain.util.BuenOjoFileUtils;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.exceptions.InvalidSatelliteImageType;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.SatelliteImageRepository;
import com.ciis.buenojo.repository.TagCircleRepository;
import com.ciis.buenojo.service.ImageCompletionSolutionService;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;

@Service

public class ImageCompletionLoader implements DataSetLoader {
	
	@Inject 
	private SatelliteImageFactory satelliteImageFactory;
	
	@Inject 
	private SatelliteImageRepository satelliteImageRepository;
	
	@Inject
	private ImageCompletionExerciseRepository exerciseRepository;
	
	@Inject
	private ImageCompletionSolutionService solutionService;
	
	@Inject
	private TagCircleRepository circleRepository;
	
	private static final String circlesFileName = "representacion.csv";
	private static final String solutionFileName = "solucion.csv";
	private static final int maxImages = 8;
	private static final Float TOTAL_SCORE = 4.0f;
	private static final String placeholderImageName = "figura";
	
	@Override
	public LoaderType loaderType() {
		return LoaderType.HangMan;
	}

	@Override
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun) throws BuenOjoDataSetException {
		ExerciseDatasetLoaderResultDTO result = new ExerciseDatasetLoaderResultDTO(dataSet);
		Set<ImageCompletionExercise> exercises = new HashSet<>();
		try {
			Path rootPath = Paths.get(dataSet.getPath());
			List<Path> exercisePaths = Files.walk(rootPath,1,FileVisitOption.FOLLOW_LINKS).filter(p -> p.compareTo(rootPath) != 0 && Files.isDirectory(p)).collect(Collectors.toList());
			for (Path exercisePath : exercisePaths) {
				
				ImageCompletionExercise exercise = new ImageCompletionExercise();
				exercise.setCourse(course);
				exercise.setTotalScore(TOTAL_SCORE);
				exercise.setName(exercisePath.getFileName().toString());
				exercise.setDescription(exercisePath.getFileName().toString());
				// saveImages
				setImages(exercisePath, exercise,dryRun);
				
				
				// solution
				setSolution(exercisePath.resolve(solutionFileName), exercise, dryRun);
				
				// tag circles	
				exerciseRepository.save(exercise);
				setTagCircles(exercisePath.resolve(circlesFileName), exercise, dryRun);
				
				exercises.add(exercise);
				
			}
			
			exerciseRepository.save(exercises);
			result = new ExerciseDatasetLoaderResultDTO(dataSet);
			result.setMessage("Ejercicio creado con éxito");
			result.setResult(LoaderResult.Done);
			
		} catch (IOException e) {
			
			result.setMessage(e.getMessage());
			result.setResult(LoaderResult.Failed);
		}
		
		
		return result;
	}
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course) throws BuenOjoDataSetException {
		return load(dataSet, course, false);
	}
	
	private boolean isPlaceholderImage(Path p) {
		return FilenameUtils.removeExtension(p.getFileName().toString()).equalsIgnoreCase(placeholderImageName);
	}
	
	private void setImages(Path path, ImageCompletionExercise exercise, Boolean dryRun) throws BuenOjoDataSetException{
		try {
			
			Set<SatelliteImage> images = new HashSet<>(maxImages);
			List<Path> imageList = Files.walk(path, 1, FileVisitOption.FOLLOW_LINKS).filter(p-> {
				return BuenOjoFileUtils.contentType(p).isPresent() && !BuenOjoFileUtils.contentType(p).get().equals("text/csv")  && !isPlaceholderImage(p);
				}).collect(Collectors.toList());
			
			for (Path imagePath : imageList) {
				
				String fileName = FilenameUtils.removeExtension(imagePath.getFileName().toString());
				Path csvPath = path.resolve(fileName + FilenameUtils.EXTENSION_SEPARATOR+ BuenOjoFileUtils.CSV_EXTENSION);
				
				SatelliteImage image = satelliteImageFactory.imageFromFile(BuenOjoFileUtils.multipartFile(imagePath), BuenOjoFileUtils.multipartFile(csvPath), dryRun);
				images.add(image);
				
			}
			exercise.setSatelliteImages(new HashSet<>(satelliteImageRepository.save(images)));
		} catch (BuenOjoCSVParserException | InvalidSatelliteImageType | BuenOjoFileException | IOException
				| BuenOjoInconsistencyException e) {
			throw new BuenOjoDataSetException(e.getMessage());
		}
	}
	
	private void setSolution(Path solutionPath, ImageCompletionExercise exercise, Boolean dryRun) throws BuenOjoDataSetException {
		
		try {
			solutionService.createSolutionForExerciseFromCSVSource(exercise, BuenOjoFileUtils.multipartFile(solutionPath),dryRun);
		} catch (BuenOjoCSVParserException | BuenOjoInconsistencyException | IOException e) {
			throw new BuenOjoDataSetException(e.getMessage());
		}
	}
	
	private void setTagCircles(Path tagCirclePath, ImageCompletionExercise exercise, Boolean dryRun) throws BuenOjoDataSetException {
		
		try {
			TagCircleCSVParser parser = new TagCircleCSVParser(BuenOjoFileUtils.multipartFile(tagCirclePath).getInputStream());
			
			List<TagCircle> circles = parser.parse()
					.stream().
					map( 
							c -> { 	c.setImageCompletionExercise(exercise);
									return c;
								}).collect(Collectors.toList());
			
			circleRepository.save(circles);
																	
			
			exercise.setTagCircles(circles.stream().collect(Collectors.toSet()));
			
		} catch (IOException | BuenOjoCSVParserException e) {
			throw new BuenOjoDataSetException(e.getCause());
		}
		
	}
}
