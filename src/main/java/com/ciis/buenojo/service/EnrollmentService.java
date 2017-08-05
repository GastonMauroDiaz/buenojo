package com.ciis.buenojo.service;

import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
	
	@Inject 
	private EnrollmentRepository enrollmentRepository;

	
	public Optional<Enrollment>currentEnrollment(Course course) {
		return enrollmentRepository.findByUserIsCurrentUserAndCourse(course.getId());
		
	}
	
}
