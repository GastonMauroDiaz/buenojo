package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.HangManGameContainer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HangManGameContainer entity.
 */
public interface HangManGameContainerRepository extends JpaRepository<HangManGameContainer,Long> {

}
