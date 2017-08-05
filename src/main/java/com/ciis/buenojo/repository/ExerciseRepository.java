package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Exercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Exercise entity.
 */
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {

    

}
