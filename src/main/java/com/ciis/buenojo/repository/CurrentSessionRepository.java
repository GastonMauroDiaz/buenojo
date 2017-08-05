package com.ciis.buenojo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ciis.buenojo.domain.CurrentSession;

/**
 * Spring Data JPA repository for the CurrentSession entity.
 */
public interface CurrentSessionRepository extends JpaRepository<CurrentSession,Long> {

    @Query("select currentSession from CurrentSession currentSession where currentSession.user.login = ?#{principal.username}")
    List<CurrentSession> findByUserIsCurrentUser();
}
