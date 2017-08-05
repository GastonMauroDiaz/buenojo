package com.ciis.buenojo.domain.factories;

import org.springframework.stereotype.Component;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.enumeration.EnrollmentStatus;
@Component
public class EnrollmentSimpleFactory {
	public Enrollment getEnrollmentEmpty()
	{

		Enrollment enrollment;
		enrollment = new Enrollment();
		return enrollment;
	}

	public Enrollment getEnrollment(User user, Course course, EnrollmentStatus status)
	{

		Enrollment enrollment;
		enrollment= getEnrollmentEmpty();
		enrollment.setUser(user);
		enrollment.setCourse(course);
		enrollment.setStatus(status);
		return enrollment;
	}


}
