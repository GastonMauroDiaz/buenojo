package com.ciis.buenojo.loaders;

import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.enumeration.LoaderType;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunFailedException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunSuccededException;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;
@Service
public class MultipleChoiceLoader implements DataSetLoader {


	@Override
	public LoaderType loaderType() {
		
		return LoaderType.MultipleChoice;
	}

	@Override
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course) throws BuenOjoDataSetException {
		return null;
	}

	@Override
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun)
			throws BuenOjoDataSetException, BuenOjoDryRunFailedException, BuenOjoDryRunSuccededException {
		// TODO Auto-generated method stub
		return null;
	}

}
