package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Activity;

import org.springframework.data.jpa.repository.*;
import java.util.Set;

/**
 * Spring Data JPA repository for the Activity entity.
 */
public interface ActivityRepository extends JpaRepository<Activity,Long> {
//	@Query("select activity from Activity activity where activity.level.id = ?1")
	Set<Activity> findByLevelId(Long levelId);
}
