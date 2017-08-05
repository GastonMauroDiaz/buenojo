package com.ciis.buenojo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;
import com.ciis.buenojo.domain.nonPersistent.CourseInformation;

/**
 * Spring Data JPA repository for the CourseLevelSession entity.
 */
public interface CourseLevelSessionRepository extends JpaRepository<CourseLevelSession,Long> {

    @Query("select courseLevelSession from CourseLevelSession courseLevelSession where courseLevelSession.user.login = ?#{principal.username}")
    List<CourseLevelSession> findByUserIsCurrentUser();
    @Query("select courseLevelSession from CourseLevelSession courseLevelSession where courseLevelSession.user.login = ?#{principal.username} and courseLevelSession.courseLevelMap.id in (select id from CourseLevelMap where course_id=?1) ")
    List<CourseLevelSession> findByUserIsCurrentUserAndCourse(Long courseId );
    
    CourseLevelSession findOneByCourseLevelMap(CourseLevelMap map);
    @Query("select courseLevelSession from CourseLevelSession courseLevelSession where courseLevelSession.user.login = ?#{principal.username} and courseLevelSession.status=?2 and courseLevelSession.courseLevelMap.id in (select id from CourseLevelMap where course_id=?1)")
    CourseLevelSession findOneByUserIsCurrentUserAndCourseAndStatus(Long courseId, CourseLevelStatus status );
    
    @Query("select new com.ciis.buenojo.domain.nonPersistent.CourseInformation(sum(courseLevelSession.exerciseCompletedCount) as exerciseCount, avg(courseLevelSession.percentage), sum(courseLevelSession.approvedExercises) as approvedExercises) from CourseLevelSession courseLevelSession where courseLevelSession.user.login = ?#{principal.username} and courseLevelSession.courseLevelMap.id in (select id from CourseLevelMap where course_id=?1)")
    CourseInformation findInformationByUserIsCurrentUserAndCourse(Long courseId);
    
    @Query("select new com.ciis.buenojo.domain.nonPersistent.CourseInformation(sum(courseLevelSession.exerciseCompletedCount) as exerciseCount, avg(courseLevelSession.percentage),sum(courseLevelSession.approvedExercises) as approvedExercises) from CourseLevelSession courseLevelSession where courseLevelSession.user.id = ?1 and courseLevelSession.courseLevelMap.id in (select id from CourseLevelMap where course_id=?2)")
    CourseInformation findInformationByUserIdAndCourseId(Long userId, Long courseId);
    
    @Query("select courseLevelSession from CourseLevelSession courseLevelSession where courseLevelSession.user.id = ?1 and courseLevelSession.courseLevelMap.course.id = ?2")
    List<CourseLevelSession> findByUserIdAndCourseId(Long userId, Long courseId);
    
}
