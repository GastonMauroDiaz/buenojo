package com.ciis.buenojo.loaders;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.enumeration.LoaderType;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunFailedException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunSuccededException;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;

public interface DataSetLoader {
	
	
	public LoaderType loaderType();
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun) throws BuenOjoDataSetException, BuenOjoDryRunFailedException, BuenOjoDryRunSuccededException;
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course) throws BuenOjoDataSetException;
}
