package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPool;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the TagPool entity.
 */
public interface TagPoolRepository extends JpaRepository<TagPool,Long> {

	public List<TagPool> findByTag(Tag tag);
	@Query("select tp from TagPool tp where tp.tag in (:tag) order by tp.similarity")	
	public List<TagPool> findByTagInListOrderBySimilarity(@Param("tag") Collection<Tag>tag);
	
	
}
