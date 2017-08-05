package com.ciis.buenojo.domain.factories;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;
@Component

public class CurrentSessionSimpleFactory {
	
	

	
	public CurrentSession getNewSessionFor(User user)
{
	CurrentSession currentSession;
	currentSession = new CurrentSession();
	currentSession.setUser(user);
	return currentSession;
}

public CourseLevelSession getCourseLevelSessionEmpty(User user, CourseLevelMap courseLevelMap)
{
	
	 CourseLevelSession courseLevelSession;
     courseLevelSession = new CourseLevelSession();
     courseLevelSession.setCourseLevelMap(courseLevelMap);
     courseLevelSession.setExerciseCompletedCount(0);
     courseLevelSession.setExperiencePoints(0.0);
     courseLevelSession.setUser(user); 
     courseLevelSession.setPercentage(0.0);
     courseLevelSession.setStatus(CourseLevelStatus.NotStarted);
     return courseLevelSession;
}

public CourseLevelSession getCourseLevelSessionRandom()
{
	
	 CourseLevelSession courseLevelSession;
	 Random rand= new Random();
     courseLevelSession = new CourseLevelSession();
     //courseLevelSession.setCourseLevelMap(courseLevelMap);
     courseLevelSession.setExerciseCompletedCount(rand.nextInt(Integer.SIZE - 1));
     courseLevelSession.setExperiencePoints(rand.nextDouble());
    // courseLevelSession.setUser(user.get()); 
     courseLevelSession.setPercentage(rand.nextDouble());
     courseLevelSession.setStatus(CourseLevelStatus.values()[rand.nextInt(CourseLevelStatus.values().length)]);
     return courseLevelSession;
}


}
