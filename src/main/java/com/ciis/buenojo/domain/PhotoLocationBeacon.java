package com.ciis.buenojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationBeacon.
 */
@Entity
@Table(name = "photo_location_beacon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationBeacon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "x")
    private Integer x;

    @NotNull
    @Column(name = "y", nullable = false)
    private Integer y;

    @NotNull
    @Min(value = 0)
    @Column(name = "tolerance", nullable = false)
    private Integer tolerance;

    @OneToOne(mappedBy = "beacon")
    @JsonIgnore
    private PhotoLocationExercise exercise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getTolerance() {
        return tolerance;
    }

    public void setTolerance(Integer tolerance) {
        this.tolerance = tolerance;
    }

    public PhotoLocationExercise getExercise() {
        return exercise;
    }

    public void setExercise(PhotoLocationExercise photoLocationExercise) {
        this.exercise = photoLocationExercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationBeacon photoLocationBeacon = (PhotoLocationBeacon) o;

        if ( ! Objects.equals(id, photoLocationBeacon.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoLocationBeacon{" +
            "id=" + id +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            ", tolerance='" + tolerance + "'" +
            '}';
    }
}
