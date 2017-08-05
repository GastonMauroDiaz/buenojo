package com.ciis.buenojo.web.rest;


import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.domain.ImageCompletionSolution;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.ImageCompletionSolutionRepository;
import com.ciis.buenojo.service.ImageCompletionSolutionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ImageCompletionSolutionUploadResource {
	
	
	private final Logger log = LoggerFactory.getLogger(ImageCompletionSolutionResource.class); 
	@Inject
	private ImageCompletionSolutionRepository solutionRepository;
	
	@Inject
	private ImageCompletionExerciseRepository exerciseRepository;

	@Inject
	private ImageCompletionSolutionService solutionService;
	
	@RequestMapping(value = "/upload/ImageCompletionExerciseSolution/{exerciseId}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public @ResponseBody ResponseEntity<ImageCompletionExercise> upload (@RequestParam("file") MultipartFile file, @PathVariable Long exerciseId ) throws BuenOjoCSVParserException,URISyntaxException {
		log.debug("REST request to save ImageCompletionSolution: to Exercise {}",exerciseId);
		ImageCompletionExercise exercise = exerciseRepository.findOne(exerciseId);
		
        if (exercise == null ) {
            return ResponseEntity.badRequest().header("Failure", "ImageCompletionExercise not found").body(null);
        }
        
        ImageCompletionSolution solution = null;
		try {
			solution = solutionService.createSolutionForExerciseFromCSVSource(exercise, file);
		} catch (BuenOjoInconsistencyException e) {
			return ResponseEntity.badRequest().header("Failure", "ImageCompletionSolution could not be parsed").body(null);
		}
        
        if (solution == null) {
        	return ResponseEntity.badRequest().header("Failure", "ImageCompletionSolution could not be parsed").body(null);
        }else {
	        exercise.setImageCompletionSolution(solution);
	        for (TagPair tagPair : exercise.getImageCompletionSolution().getTagPairs()) {
	        	exercise.getTags().add(tagPair.getTag());
			}
	        solutionRepository.save(solution);
	        exerciseRepository.save(exercise);
        }
        return ResponseEntity.created(new URI("/api/imageCompletionSolution/" + solution.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imageCompletionSolution", solution.getId().toString()))
            .body(exercise);
		
	}
	
	
}
