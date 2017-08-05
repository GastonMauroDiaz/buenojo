package com.ciis.buenojo.domain.enumeration;

/**
 * The LoaderType enumeration.
 */
public enum LoaderType {
    ImageCompletion,PhotoLocation,HangMan,MultipleChoice;
    
    public static LoaderType fromExerciseType (ExerciseType exerciseType) {
    	
    	switch (exerciseType) {
		case HangMan:
			return LoaderType.HangMan;
		case ImageCompletion:
			return LoaderType.ImageCompletion;
		case MultipleChoice:
			return LoaderType.MultipleChoice;
		case PhotoLocation:
			return LoaderType.PhotoLocation;
    	}
		return null;	
    }
}

