package com.ciis.buenojo.repository;


import com.ciis.buenojo.domain.TagPair;



import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TagPair entity.
 */
public interface TagPairRepository extends JpaRepository<TagPair,Long> {

}
