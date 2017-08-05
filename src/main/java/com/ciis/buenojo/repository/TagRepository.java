package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Tag;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
	public List<Tag> findByCourseOrderByNumber(Course course);
	
	@Query("select t from Tag t where t.course = :course and t not in (:tags)")
	public List<Tag> findByCourseNotInList(@Param("course") Course course,@Param("tags") Collection<Tag> tags);

}
