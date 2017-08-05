package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TagCircle.
 */
@Entity
@Table(name = "tag_circle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagCircle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 8)
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotNull
    @Min(value = 0)
    @Column(name = "y", nullable = false)
    private Integer y;

    @NotNull
    @Min(value = 0)
    @Column(name = "x", nullable = false)
    private Integer x;

    @NotNull
    @Min(value = 0)
    @Column(name = "radio_px", nullable = false)
    private Float radioPx;

    @ManyToOne
    private ImageCompletionExercise imageCompletionExercise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Float getRadioPx() {
        return radioPx;
    }

    public void setRadioPx(Float radioPx) {
        this.radioPx = radioPx;
    }
    @JsonIgnore
    public ImageCompletionExercise getImageCompletionExercise() {
        return imageCompletionExercise;
    }

    public void setImageCompletionExercise(ImageCompletionExercise imageCompletionExercise) {
        this.imageCompletionExercise = imageCompletionExercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagCircle tagCircle = (TagCircle) o;

        if ( ! Objects.equals(id, tagCircle.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TagCircle{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", y='" + y + "'" +
            ", x='" + x + "'" +
            ", radioPx='" + radioPx + "'" +
            ", imageCompletionExercise='" + imageCompletionExercise.getId() + "'"+
            '}';
    }
}
