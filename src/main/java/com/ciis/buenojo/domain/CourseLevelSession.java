package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;

/**
 * A CourseLevelSession.
 */
@Entity
@Table(name = "course_level_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseLevelSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseLevelStatus status;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "percentage", nullable = false)
    private Double percentage;

    @NotNull
    @Min(value = 0)
    @Column(name = "experience_points", nullable = false)
    private Double experiencePoints;

    @NotNull
    @Min(value = 0)
    @Column(name = "exercise_completed_count", nullable = false)
    private Integer exerciseCompletedCount;

   
    @NotNull
    @Min(value = 0)
    @Column(name = "approved_exercises", nullable = false)
    private Integer approvedExercises;
    
  

	public Integer getApprovedExercises() {
		return approvedExercises;
	}

	public void setApprovedExercises(Integer approvedExercises) {
		this.approvedExercises = approvedExercises;
	}

	@ManyToOne(optional=true, fetch= FetchType.EAGER)
    private CourseLevelMap courseLevelMap;																																																																																																																																							

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseLevelStatus getStatus() {
        return status;
    }

    public void setStatus(CourseLevelStatus status) {
        this.status = status;
    }

    public Double getPercentage() {
    	return this.percentage;
    }

    public Double estimatePercentage () {
    	if (courseLevelMap != null && courseLevelMap.getLevel() != null && courseLevelMap.getLevel().getActivities() != null){
    		int size = courseLevelMap.getLevel().getActivities().size();
    		if (size > 0){
    			Double p =approvedExercises/new Double(size)*100;
    			if (p>100) return 100.0;
    			return p;
    		}
    	} 
    	return 0.0;
    }
    
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(Double experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public Integer getExerciseCompletedCount() {
        return exerciseCompletedCount;
    }

    public void setExerciseCompletedCount(Integer exerciseCompletedCount) {
        this.exerciseCompletedCount = exerciseCompletedCount;
    }

    public CourseLevelMap getCourseLevelMap() {
        return courseLevelMap;
    }

    public void setCourseLevelMap(CourseLevelMap courseLevelMap) {
        this.courseLevelMap = courseLevelMap;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseLevelSession courseLevelSession = (CourseLevelSession) o;

        if ( ! Objects.equals(id, courseLevelSession.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CourseLevelSession{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", percentage='" + percentage + "'" +
            ", experiencePoints='" + experiencePoints + "'" +
            ", exerciseCompletedCount='" + exerciseCompletedCount + "'" +
            '}';
    }
}
