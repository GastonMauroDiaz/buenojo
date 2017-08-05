package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.HangManOptionListItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HangManOptionListItem entity.
 */
public interface HangManOptionListItemRepository extends JpaRepository<HangManOptionListItem,Long> {
	
	public List<HangManOptionListItem> findByOptionTypeIn(List<String> category);

}
