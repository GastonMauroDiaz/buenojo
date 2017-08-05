package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.LoaderTrace;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LoaderTrace entity.
 */
public interface LoaderTraceRepository extends JpaRepository<LoaderTrace,Long> {

}
