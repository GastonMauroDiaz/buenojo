package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.Level;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the CourseLevelMap entity.
 */
public interface CourseLevelMapRepository extends JpaRepository<CourseLevelMap,Long> {
	List<CourseLevelMap> findByCourse_Id(long course_id);
	List<CourseLevelMap> findByCourse_IdAndParentIsNull(long course_id);
	Optional<CourseLevelMap> findOneByCourse_IdAndParentIsNull(long course_id);
	List<CourseLevelMap> findByCourseAndParent(Course course, Level parent);
	
	
}
