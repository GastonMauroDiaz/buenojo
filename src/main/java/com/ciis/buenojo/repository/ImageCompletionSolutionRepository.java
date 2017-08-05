package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.ImageCompletionSolution;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ImageCompletionSolution entity.
 */
public interface ImageCompletionSolutionRepository extends JpaRepository<ImageCompletionSolution,Long> {

}
