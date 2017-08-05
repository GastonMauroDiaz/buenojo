package com.ciis.buenojo.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.enumeration.LoaderResult;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunFailedException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunSuccededException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.loaders.DataSetLoaderService;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class ExerciseDataSetResource {

	private final Logger log = LoggerFactory.getLogger(ExerciseDataSetResource.class);

	@Inject
	private DataSetLoaderService loaderService;

	@Inject
	private CourseRepository courseRepository;

	@RequestMapping(value = "/dataSets",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	public List<ExerciseDataSetDTO> getDataSets() throws BuenOjoDataSetException{
		log.debug("REST request to get all DataSets");

		return loaderService.getDataSets();
	}


	@RequestMapping(value = "/dataSets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	public ResponseEntity<ExerciseDatasetLoaderResultDTO> loadDataset(@Valid @RequestBody ExerciseDataSetDTO dto, @Param(value = "courseId") Long courseId, @Param(value = "dryRun") Boolean dryRun, @Param(value = "loadExtra") Boolean loadExtraResources) throws BuenOjoInconsistencyException {
		if (dryRun){
			log.debug("REST request to DRY RUN load Dataset '"+dto+"' and course: ["+courseId+"]");
		} else {
			log.debug("REST request to load Dataset '"+dto+"' and course: ["+courseId+"]");
		}


		ExerciseDatasetLoaderResultDTO result = new ExerciseDatasetLoaderResultDTO(dto);
		Course course = courseRepository.findOne(courseId);
		if (course == null) {
			throw new BuenOjoInconsistencyException("No existe un curso con ID: '"+ courseId +"'");
		}
		try{
			result = loaderService.load(dto,course, dryRun);
		}catch (BuenOjoDryRunFailedException f){
			result = new ExerciseDatasetLoaderResultDTO(dto);
			result.setResult(LoaderResult.Failed);
			result.setMessage(f.getMessage());
		} catch (BuenOjoDryRunSuccededException s){
			result = new ExerciseDatasetLoaderResultDTO(dto);
			result.setResult(LoaderResult.Done);
			result.setMessage(s.getMessage());
		} catch (Exception e){
			result = new ExerciseDatasetLoaderResultDTO(dto);
			result.setResult(LoaderResult.Unknown);
			result.setMessage(e.getMessage());
		}

		if (result.getResult().equals(LoaderResult.Done)){
			return ResponseEntity.ok()
	                .headers(HeaderUtil.createAlert(dryRun?"[DRY_RUN]":""+"Se importó el set de datos exitosamente", ""))
	                .body(result);
		}

		return ResponseEntity.accepted().body(result);

	}
}
