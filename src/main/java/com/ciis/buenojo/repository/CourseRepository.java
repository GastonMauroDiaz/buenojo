package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Course entity.
 */
public interface CourseRepository extends JpaRepository<Course,Long> {
	public List<Course> findAllByOrderByPriorityDesc();
}
