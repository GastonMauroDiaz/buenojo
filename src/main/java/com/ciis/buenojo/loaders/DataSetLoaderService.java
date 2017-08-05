package com.ciis.buenojo.loaders;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ciis.buenojo.config.BuenOjoEnvironment;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.LoaderTrace;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.enumeration.ExerciseType;
import com.ciis.buenojo.domain.enumeration.LoaderResult;
import com.ciis.buenojo.domain.util.BuenOjoFileUtils;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunFailedException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunSuccededException;
import com.ciis.buenojo.repository.LoaderTraceRepository;
import com.ciis.buenojo.repository.UserRepository;
import com.ciis.buenojo.service.LoaderTraceService;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;

@Service
@Transactional(rollbackOn = Exception.class)
public class DataSetLoaderService {
	
	@Inject
	
	private BuenOjoEnvironment env;
	@Inject
	
	private LoaderTraceRepository loaderTraceRepository;
	@Inject 
	
	private UserRepository userRepository;
	
	@Inject
	private HangManLoader hangManLoader;
	
	@Inject
	private MultipleChoiceLoader multipleChoiceLoader;
	
	@Inject
	private ImageCompletionLoader imageCompletionLoader;
	
	@Inject
	private PhotoLocationLoader photoLocationLoader;
	
	@Inject
	private LoaderTraceService loaderTraceService;
	
	private final Logger log = LoggerFactory.getLogger(DataSetLoaderService.class);
	
	
	private ExerciseDataSetDTO fromPath(Path p) throws BuenOjoDataSetException{
		ExerciseDataSetDTO dto = new ExerciseDataSetDTO();
		ExerciseType type = BuenOjoFileUtils.exerciseTypeFromPath(p);
		if (type == null) 
				throw new BuenOjoDataSetException("No se puede determinar el tipo de ejercicio por la ruta del dataset: "+ 
		p + "Debe tener alguna de estas carpetas: "+ BuenOjoFileUtils.datasetFolderList());
		
		dto.setPath(p.toString());
		dto.setName(p.getFileName().toString());
		dto.setType(type);
		return dto;
		
	}
	
	public List<ExerciseDataSetDTO> getDataSets() throws BuenOjoDataSetException{
		
		Path path = new File(env.getFTPAbsolutePath()).toPath();
		List<ExerciseDataSetDTO> files = new ArrayList<>();
		try {
			List<Path> paths = Files.walk(path, 2 , FileVisitOption.FOLLOW_LINKS).filter(p -> p.compareTo(path) != 0 && p.toFile().isDirectory() && !BuenOjoFileUtils.isExerciseFolder(p) ).collect(Collectors.toList());
			for (Path p : paths) {
				
				files.add(fromPath(p));
			}
			
		}catch (IOException e){
			throw new BuenOjoDataSetException("Error de file system al obtener datasets desde: "+path+" "+e.getMessage());
		}
		
		return files;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun) throws BuenOjoDataSetException, BuenOjoDryRunFailedException, BuenOjoDryRunSuccededException {
		
		log.info("Attempt to load dataset:"+dataSet);
		User user = userRepository.findCurrentUser().orElseThrow(() -> new BuenOjoDataSetException("El usuario no esta logueado"));
		ExerciseDatasetLoaderResultDTO result = null;
		
		LoaderTrace trace = new LoaderTrace(dataSet);
		trace.setAuthor(user.getLogin());
		try{
			result = loaderFromType(dataSet.getType()).load(dataSet,course, dryRun);
		}catch(BuenOjoDataSetException | BuenOjoDryRunFailedException | BuenOjoDryRunSuccededException e){
			result = new ExerciseDatasetLoaderResultDTO(dataSet);
			result.setResult(LoaderResult.Failed);
			result.setMessage(e.getMessage());
			trace.setLoaderResult(LoaderResult.Failed);
			trace.setResultLog(e.getMessage());
		}finally {
			if (dryRun) {
				switch (result.getResult()) {
				case Failed:
				case Unknown:
					throw new BuenOjoDryRunFailedException(result.getMessage());
				case Done:
					throw new BuenOjoDryRunSuccededException(result.getMessage());

				}
			}else {
				
				trace.setLoaderResult(result.getResult());
				
				if (trace.getLoaderResult() == LoaderResult.Done){
					trace.setResultLog("dataset: '"+dataSet+"'creado con éxito");
					trace = loaderTraceService.addLoadedExercises(trace, result);
				}
				loaderTraceRepository.save(trace);
			}
		}
		
		return result;
	}
	
	private DataSetLoader loaderFromType(ExerciseType type){
		switch (type) {
		case HangMan:
			return hangManLoader;
		case ImageCompletion:
			return imageCompletionLoader;
		case MultipleChoice:
			return multipleChoiceLoader;
		case PhotoLocation:
			return photoLocationLoader;
		
		}
		return null;
		
	}
}
