package com.ciis.buenojo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Enrollment;

/**
 * Spring Data JPA repository for the Enrollment entity.
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("select enrollment from Enrollment enrollment where enrollment.user.login = ?#{principal.username}")
    List<Enrollment> findByUserIsCurrentUser();

    @Query("select enrollment from Enrollment enrollment  where enrollment.user.login = ?#{principal.username} and enrollment.course.id=?1 and (enrollment.status='Started' or enrollment.status='InProgress')")
    Optional<Enrollment> findByUserIsCurrentUserAndCourse(Long courseId);
    
    List<Enrollment> findAllByCourseId(Long courseId);
    
   Enrollment findOneByUserIdAndCourseId(Long userId, Long courseId);

}
