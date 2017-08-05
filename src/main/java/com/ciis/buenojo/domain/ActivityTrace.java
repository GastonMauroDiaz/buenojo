package com.ciis.buenojo.domain;


import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActivityTrace.
 */
@Entity
@Table(name = "activity_trace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@JsonIdentityInfo(scope=ActivityTrace.class, generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ActivityTrace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull        
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

          
    @Column(name = "end_date", nullable = true)
    private ZonedDateTime endDate;

    
    @Min(value = 0)        
    @Column(name = "score", nullable = true)
    private Float score;

    @NotNull
    @Column(name = "enrollment_id", nullable = false)
    private Long enrollmentId;

    
    @OneToOne
    private Activity activity;
    
    @Column(name = "passed", nullable = true)
    private Boolean passed;
    
    public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getEnrollment() {
        return enrollmentId;
    }

    public void setEnrollment(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Activity getActivity() {
        return activity;
    }
    
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityTrace activityTrace = (ActivityTrace) o;

        if ( ! Objects.equals(id, activityTrace.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActivityTrace{" +
                "id=" + id +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                ", score='" + score + "'" +
                '}';
    }
}
