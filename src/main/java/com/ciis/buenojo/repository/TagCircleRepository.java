package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.TagCircle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TagCircle entity.
 */
public interface TagCircleRepository extends JpaRepository<TagCircle,Long> {

}
