package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationKeyword;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationKeyword entity.
 */
public interface PhotoLocationKeywordRepository extends JpaRepository<PhotoLocationKeyword,Long> {

	@Query ("select keyword from PhotoLocationKeyword keyword  where keyword.name in :names")
	public List<PhotoLocationKeyword>findKeywordWithNames(@Param("names") List<String> names);
	
	@Query ("select keyword from PhotoLocationKeyword keyword  where keyword.name in :names and keyword.course = :course")
	public List<PhotoLocationKeyword>findKeywordWithNames(@Param("names") List<String> names, @Param("course") Course course);
	
	public List<PhotoLocationKeyword>findAllByCourseId(@Param("courseId") Long courseId);
}
