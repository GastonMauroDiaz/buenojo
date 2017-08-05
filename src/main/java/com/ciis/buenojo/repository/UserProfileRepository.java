package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.UserProfile;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    @Query("select userProfile from UserProfile userProfile where userProfile.user.login = ?#{principal.username}")
    UserProfile findByUserIsCurrentUser();
    
    UserProfile findOneByUserId(Long id);

}
