package com.ciis.buenojo.domain.factories;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;


@Component
public class CourseLevelSessionSimpleFactory {
public CourseLevelSession getCourseLevelSessionEmpty()
{
	
	 CourseLevelSession courseLevelSession;
     courseLevelSession = new CourseLevelSession();
     //courseLevelSession.setCourseLevelMap(courseLevelMap);
     courseLevelSession.setExerciseCompletedCount(0);
     courseLevelSession.setExperiencePoints(0.0);
    // courseLevelSession.setUser(user.get()); 
     courseLevelSession.setPercentage(0.0);
     courseLevelSession.setApprovedExercises(0);
 
     courseLevelSession.setStatus(CourseLevelStatus.NotStarted);
     return courseLevelSession;
}

public CourseLevelSession getCourseLevelSessionEmpty(User user, CourseLevelMap courseLevelMap)
{
	
	 CourseLevelSession courseLevelSession;
     courseLevelSession = getCourseLevelSessionEmpty();
     courseLevelSession.setCourseLevelMap(courseLevelMap);
     courseLevelSession.setUser(user);
     
     return courseLevelSession;
}

public CourseLevelSession getCourseLevelSessionRandom()
{
	
	 CourseLevelSession courseLevelSession;
	 Random rand= new Random();
     courseLevelSession = new CourseLevelSession();
     //courseLevelSession.setCourseLevelMap(courseLevelMap);
     courseLevelSession.setExerciseCompletedCount(rand.nextInt(Integer.SIZE - 1)+10);
     courseLevelSession.setExperiencePoints(rand.nextDouble());
    // courseLevelSession.setUser(user.get()); 
     courseLevelSession.setPercentage(rand.nextDouble());
     courseLevelSession.setApprovedExercises(rand.nextInt(courseLevelSession.getExerciseCompletedCount() - 1));
     courseLevelSession.setStatus(CourseLevelStatus.values()[rand.nextInt(CourseLevelStatus.values().length)]);
     
     return courseLevelSession;
}


}
