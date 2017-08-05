package com.ciis.buenojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ImageCompletionSolution.
 */
@Entity
@Table(name = "image_completion_solution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ImageCompletionSolution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "imageCompletionSolution")
    @JsonIgnore
    private ImageCompletionExercise imageCompletionExercise;

    @OneToMany(mappedBy = "imageCompletionSolution", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TagPair> tagPairs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageCompletionExercise getImageCompletionExercise() {
        return imageCompletionExercise;
    }

    public void setImageCompletionExercise(ImageCompletionExercise imageCompletionExercise) {
        this.imageCompletionExercise = imageCompletionExercise;
    }

    public Set<TagPair> getTagPairs() {
        return tagPairs;
    }

    public void setTagPairs(Set<TagPair> tagPairs) {
        this.tagPairs = tagPairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageCompletionSolution imageCompletionSolution = (ImageCompletionSolution) o;

        if ( ! Objects.equals(id, imageCompletionSolution.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImageCompletionSolution{" +
            "id=" + id +
            '}';
    }
}
