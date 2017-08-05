package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.domain.PhotoLocationKeyword;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the PhotoLocationExtraImage entity.
 */
public interface PhotoLocationExtraImageRepository extends JpaRepository<PhotoLocationExtraImage,Long> {

	List<PhotoLocationExtraImage> findAllByCourse(Course course);

	@Query("select extraImage from PhotoLocationExtraImage extraImage where extraImage.course = :course and extraImage.image.keywords in :keywords")
	List<PhotoLocationExtraImage> findByCourseAndKeywords(Course course, Set<PhotoLocationKeyword> keywords);
	
}
