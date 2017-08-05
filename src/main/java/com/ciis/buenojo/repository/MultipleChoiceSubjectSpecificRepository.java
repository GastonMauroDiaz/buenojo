package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.MultipleChoiceSubjectSpecific;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultipleChoiceSubjectSpecific entity.
 */
public interface MultipleChoiceSubjectSpecificRepository extends JpaRepository<MultipleChoiceSubjectSpecific,Long> {

}
