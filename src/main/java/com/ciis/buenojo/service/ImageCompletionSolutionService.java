package com.ciis.buenojo.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.domain.ImageCompletionSolution;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.domain.parsers.ImageCompletionSolutionCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.ImageCompletionSolutionRepository;
import com.ciis.buenojo.repository.TagPairRepository;
import com.ciis.buenojo.repository.TagRepository;

/**
 * Service that manages Image Completion Exercise solutions
 * @author franciscogindre
 *
 */

@Service
@Transactional

public class ImageCompletionSolutionService {
	
	@Inject
	private TagPairRepository tagPairRepository;
	
	@Inject 
	private ImageCompletionSolutionRepository solutionRepository;
	
	@Inject
	private ImageCompletionExerciseRepository imageCompletionExerciseRepository;
	@Inject 
	private TagRepository tagRepository;
	
	public ImageCompletionSolution createSolutionForExerciseIdFromCSVSource(Long exerciseId, InputStreamSource source) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
		ImageCompletionExercise exercise = imageCompletionExerciseRepository.findOne(exerciseId);
		return createSolutionForExerciseFromCSVSource(exercise, source);
		
	}
	/**
	 * Create a solution for an {@link ImageCompletionExercise} from it's id and the CSV source
	 * @param courseId
	 * @param source 
	 * @return the persisted {@link ImageCompletionSolution} 
	 * @throws BuenOjoCSVParserException
	 * @throws BuenOjoInconsistencyException 
	 */
	public ImageCompletionSolution createSolutionForExerciseFromCSVSource(ImageCompletionExercise exercise, InputStreamSource source) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
		return createSolutionForExerciseFromCSVSource(exercise, source, false);
	}
	
	/**
	 * Create a solution for an {@link ImageCompletionExercise} from it's id and the CSV source
	 * @param courseId
	 * @param source 
	 * @param dryRun won't save anything. just for testing
	 * @return the persisted {@link ImageCompletionSolution} 
	 * @throws BuenOjoCSVParserException
	 * @throws BuenOjoInconsistencyException 
	 */
	public ImageCompletionSolution createSolutionForExerciseFromCSVSource(ImageCompletionExercise exercise, InputStreamSource source, Boolean dryRun) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
		
		List<Tag> tagList = tagRepository.findByCourseOrderByNumber(exercise.getCourse());
		
		if (tagList == null || tagList.size() == 0) {
			throw new BuenOjoInconsistencyException("No hay etiquetas cargadas para el curso ["+exercise.getCourse()+"]");
		}
		
		ImageCompletionSolution solution = new ImageCompletionSolution();
		ImageCompletionSolutionCSVParser parser = new ImageCompletionSolutionCSVParser(source, tagList);
		
		List<TagPair> tagPairs = null;
		try {
			tagPairs = parser.parse();
		} catch (IOException e) {
			throw new BuenOjoCSVParserException(e.getMessage());
		}
		
		tagPairRepository.save(tagPairs);
		
		solutionRepository.save(solution);
		HashSet<Tag> tagSet = new HashSet<>();
		solution.setTagPairs(new HashSet<TagPair>(tagPairs));
		for (TagPair tagPair : tagPairs) {
			tagPair.setImageCompletionSolution(solution);
			tagSet.add(tagPair.getTag());
		}
		solution.setImageCompletionExercise(exercise);
		
		solutionRepository.save(solution);
		exercise.setImageCompletionSolution(solution);
		exercise.setTags(tagSet);
		
		
		imageCompletionExerciseRepository.save(exercise);
		
		return solution;
		
	}

}
