package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationExtraSatelliteImage;
import com.ciis.buenojo.domain.PhotoLocationKeyword;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the PhotoLocationExtraSatelliteImage entity.
 */
public interface PhotoLocationExtraSatelliteImageRepository extends JpaRepository<PhotoLocationExtraSatelliteImage,Long> {
	
	List<PhotoLocationExtraSatelliteImage> findAllByCourse(Course course);
	@Query("select extraImage from PhotoLocationExtraSatelliteImage extraImage where extraImage.course = :course and extraImage.image.keywords in :keywords")
	List<PhotoLocationExtraSatelliteImage> findByCourseAndKeywords(Course course, Set<PhotoLocationKeyword> keywords);
}
