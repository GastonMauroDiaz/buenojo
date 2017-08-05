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
 * A LoadedExercise.
 */
@Entity
@Table(name = "loaded_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LoadedExercise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull        
    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    @ManyToOne
    private LoaderTrace loaderTrace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public LoaderTrace getLoaderTrace() {
        return loaderTrace;
    }

    public void setLoaderTrace(LoaderTrace loaderTrace) {
        this.loaderTrace = loaderTrace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoadedExercise loadedExercise = (LoadedExercise) o;

        if ( ! Objects.equals(id, loadedExercise.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LoadedExercise{" +
                "id=" + id +
                ", exerciseId='" + exerciseId + "'" +
                '}';
    }
}
