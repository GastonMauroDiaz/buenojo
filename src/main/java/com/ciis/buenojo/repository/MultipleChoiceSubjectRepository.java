package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.MultipleChoiceSubject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultipleChoiceSubject entity.
 */
public interface MultipleChoiceSubjectRepository extends JpaRepository<MultipleChoiceSubject,Long> {

}
