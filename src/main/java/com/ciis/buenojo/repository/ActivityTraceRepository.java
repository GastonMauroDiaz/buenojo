package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.ActivityTrace;
import com.ciis.buenojo.domain.Level;
import com.ciis.buenojo.web.rest.dto.AverageScoreByExerciseTypeDTO;
import com.ciis.buenojo.web.rest.dto.ResolutionTimeByExerciseTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ActivityTrace entity.
 */
public interface ActivityTraceRepository extends JpaRepository<ActivityTrace,Long> {
	@Query("select trace from ActivityTrace trace where trace.enrollmentId=?1 and trace.activity.level=?2")
	List<ActivityTrace> findAllByEnrollmentAndLevel(Long enrollmentId, Level l);
	
	@Query("select trace from ActivityTrace trace where trace.enrollmentId=?1 and trace.activity.level=?2 and trace.passed=true")
	List<ActivityTrace> findAllByEnrollmentAndLevelWhereIsPassed(Long enrollmentId, Level l);
	
	@Query("select trace from ActivityTrace trace where trace.enrollmentId=?1 and trace.activity.level=?2 and trace.passed=false")
	List<ActivityTrace> findAllByEnrollmentAndLevelWhereIsNotPassed(Long enrollmentId, Level l);
	
	@Query("select trace from ActivityTrace trace where trace.enrollmentId=?1 and trace.activity.level=?2 and trace.endDate=null and (trace.passed=false or trace.passed=null)")
	List<ActivityTrace> findAllByEnrollmentAndLevelWhereEndDateIsNullAndIsNotPassedOrNull(Long enrollmentId, Level l);
	
	@Query("select count(trace) from ActivityTrace trace where trace.enrollmentId=?1 and trace.activity.level=?2")
	Integer countByEnrollmentAndLevelWhereIsPassed(Long enrollmentId, Level l);
	@Query("select max(t.startDate) from ActivityTrace t where t.enrollmentId=?1")
	Optional<ActivityTrace> findLatestForEnrollment(Long enrollmentId);
	
	Optional<ActivityTrace>findTopByEnrollmentIdOrderByStartDateDesc(Long enrollment);
	@Query("select trace from ActivityTrace trace where trace.enrollmentId=?1 and trace.startDate=?2")
	ActivityTrace findLatestWithEnrollmentAndDate(Long enrollmentId, ZonedDateTime date);
	
	Page<ActivityTrace> findAllByEnrollmentIdOrderByStartDateAsc(Long enrollmentId, Pageable pageable);
	
	
	@Query("select new com.ciis.buenojo.web.rest.dto.ResolutionTimeByExerciseTypeDTO(avg(extract(epoch from (activityTrace.endDate-activityTrace.startDate))) as duration, activity.type as exerciseType) from ActivityTrace activityTrace inner join activityTrace.activity as activity where activityTrace.enrollmentId in (select enrollment from Enrollment enrollment where enrollment.course.id=?1) and activityTrace.passed=true and activityTrace.endDate!=null group by activity.type")
	List<ResolutionTimeByExerciseTypeDTO> averageResolutionTimeByExerciseType(Long courseId);
	@Query("select new com.ciis.buenojo.web.rest.dto.AverageScoreByExerciseTypeDTO(activity.type as exerciseType, avg(activityTrace.score) as score) from ActivityTrace activityTrace inner join activityTrace.activity as activity where activityTrace.enrollmentId in (select enrollment from Enrollment enrollment where enrollment.course.id=?1) and activityTrace.passed=true and activityTrace.endDate!=null group by activity.type")
	List<AverageScoreByExerciseTypeDTO> averageScoreByExerciseType(Long courseId);
}
