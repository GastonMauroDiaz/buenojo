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
 * A HangManExerciseHint.
 */
@Entity
@Table(name = "hang_man_exercise_hint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManExerciseHint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

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

    @ManyToOne
    private HangManExercise hangManExercise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        HangManExerciseHint hangManExerciseHint = (HangManExerciseHint) o;

        if ( ! Objects.equals(id, hangManExerciseHint.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManExerciseHint{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            '}';
    }
}
