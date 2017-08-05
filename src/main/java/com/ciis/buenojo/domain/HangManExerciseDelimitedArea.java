package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A HangManExerciseDelimitedArea.
 */
@Entity
@Table(name = "hang_man_exercise_delimited_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManExerciseDelimitedArea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1024)
    @Column(name = "x", nullable = false)
    private Integer x;

    @NotNull
    @Min(value = 0)
    @Max(value = 1024)
    @Column(name = "y", nullable = false)
    private Integer y;

    @NotNull
    @Min(value = 10)
    @Max(value = 512)
    @Column(name = "radius", nullable = false)
    private Integer radius;

    @OneToOne    private HangManExercise hangManExercise;

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

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public HangManExercise getHangManExercise() {
        return hangManExercise;
    }

    public void setHangManExercise(HangManExercise hangManExercise) {
        this.hangManExercise = hangManExercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HangManExerciseDelimitedArea hangManExerciseDelimitedArea = (HangManExerciseDelimitedArea) o;

        if ( ! Objects.equals(id, hangManExerciseDelimitedArea.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManExerciseDelimitedArea{" +
            "id=" + id +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            ", radius='" + radius + "'" +
            '}';
    }
}
