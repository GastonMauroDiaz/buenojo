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
 * A HangManExerciseOption.
 */
@Entity
@Table(name = "hang_man_exercise_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManExerciseOption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "text", length = 25, nullable = false)
    private String text;

    @NotNull
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

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

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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

        HangManExerciseOption hangManExerciseOption = (HangManExerciseOption) o;

        if ( ! Objects.equals(id, hangManExerciseOption.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManExerciseOption{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", isCorrect='" + isCorrect + "'" +
            '}';
    }
}
