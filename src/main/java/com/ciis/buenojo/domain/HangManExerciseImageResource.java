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
 * A HangManExerciseImageResource.
 */
@Entity
@Table(name = "hang_man_exercise_image_resource")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManExerciseImageResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private HangManExercise hangManExercise;

    @OneToOne
    private ImageResource imageResource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HangManExercise getHangManExercise() {
        return hangManExercise;
    }

    public void setHangManExercise(HangManExercise hangManExercise) {
        this.hangManExercise = hangManExercise;
    }

    public ImageResource getImageResource() {
        return imageResource;
    }

    public void setImageResource(ImageResource imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HangManExerciseImageResource hangManExerciseImageResource = (HangManExerciseImageResource) o;

        if ( ! Objects.equals(id, hangManExerciseImageResource.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManExerciseImageResource{" +
            "id=" + id +
            '}';
    }
}
