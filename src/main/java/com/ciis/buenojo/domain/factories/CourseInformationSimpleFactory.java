package com.ciis.buenojo.domain.factories;

import org.springframework.stereotype.Component;

import com.ciis.buenojo.domain.nonPersistent.CourseInformation;
@Component
public class CourseInformationSimpleFactory {
public CourseInformation getCourseInformationEmpty(){
CourseInformation courseInformation;
courseInformation= new CourseInformation(0L, 0.0, 0L);
courseInformation.setExerciseCount(0L);
courseInformation.setPassedStages(0);
courseInformation.setApprovedExercises(0L);
courseInformation.setApprovedPercentage(0.0);
return courseInformation;

}

public CourseInformation getCourseInformation(Long exerciseCount, Long approvedExercises){
CourseInformation courseInformation;
courseInformation= new CourseInformation(exerciseCount, 0.0, approvedExercises);
courseInformation.setExerciseCount(exerciseCount);
courseInformation.setPassedStages(0);
courseInformation.setApprovedExercises(approvedExercises);
courseInformation.setApprovedPercentage(calculatePercentage(approvedExercises, exerciseCount));
return courseInformation;

}

private Double calculatePercentage(Long approvedExercises, Long exerciseCount) {
	if (exerciseCount==0) return 0.0;
	else return ((double)approvedExercises/(double)exerciseCount)*100;
	// TODO Auto-generated method stub
	
}


}
