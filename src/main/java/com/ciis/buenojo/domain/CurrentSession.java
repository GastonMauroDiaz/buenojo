package com.ciis.buenojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CurrentSession.
 */
@Entity
@Table(name = "current_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurrentSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JsonIgnore
    private User user;

    @OneToOne
    @JsonIgnore
    private CourseLevelSession courseLevelSession;

    @JsonIgnore
    private Long tempId;

    public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CourseLevelSession getCourseLevelSession() {
        return courseLevelSession;
    }

    public void setCourseLevelSession(CourseLevelSession courseLevelSession) {
        this.courseLevelSession = courseLevelSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrentSession currentSession = (CurrentSession) o;

        if ( ! Objects.equals(id, currentSession.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CurrentSession{" +
            "id=" + id +
            '}';
    }
}
