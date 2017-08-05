package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.ImageResource;

import java.util.List;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ImageResource entity.
 */
public interface ImageResourceRepository extends JpaRepository<ImageResource,Long> {
	
	public ImageResource findOneByName(String name);
	public List<ImageResource> findAllByOrderByIdAsc();
}
