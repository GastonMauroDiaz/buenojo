package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.PhotoLocationImage;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationImage entity.
 */
public interface PhotoLocationImageRepository extends JpaRepository<PhotoLocationImage,Long> {

    @Query("select distinct photoLocationImage from PhotoLocationImage photoLocationImage left join fetch photoLocationImage.keywords")
    List<PhotoLocationImage> findAllWithEagerRelationships();

    @Query("select photoLocationImage from PhotoLocationImage photoLocationImage left join fetch photoLocationImage.keywords where photoLocationImage.id =:id")
    PhotoLocationImage findOneWithEagerRelationships(@Param("id") Long id);
    
    @Query("select photoLocationImage from PhotoLocationImage photoLocationImage left join fetch photoLocationImage.keywords where photoLocationImage.image.name =:name")
    PhotoLocationImage findOneWithEagerRelationshipsByName(@Param("name") String name);

}
