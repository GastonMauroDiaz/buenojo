package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * A drag and drop Image Completion Exercise
 */
@Entity
@Table(name = "image_completion_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@PrimaryKeyJoinColumn(name = "id")

public class ImageCompletionExercise extends Exercise implements Serializable {

	 
    @OneToOne
    private ImageCompletionSolution imageCompletionSolution;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "image_completion_exercise_tag",
               joinColumns = @JoinColumn(name="image_completion_exercises_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "image_completion_exercise_satellite_image",
               joinColumns = @JoinColumn(name="image_completion_exercises_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="satellite_images_id", referencedColumnName="ID"))
    private Set<SatelliteImage> satelliteImages = new HashSet<>();

    @OneToMany(mappedBy = "imageCompletionExercise")

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TagCircle> tagCircles = new HashSet<>();

    public void setTagCircles(Set<TagCircle> tagCircles) {
    	this.tagCircles = tagCircles;
    }

    public Set<TagCircle> getTagCircles(){
    	return this.tagCircles;
    }
    public ImageCompletionSolution getImageCompletionSolution() {
        return imageCompletionSolution;
    }

    public void setImageCompletionSolution(ImageCompletionSolution imageCompletionSolution) {
        this.imageCompletionSolution = imageCompletionSolution;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<SatelliteImage> getSatelliteImages() {
        return satelliteImages;
    }

    public void setSatelliteImages(Set<SatelliteImage> satelliteImages) {
        this.satelliteImages = satelliteImages;
    }

    
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageCompletionExercise imageCompletionExercise = (ImageCompletionExercise) o;

        if ( ! Objects.equals(this.getId(), imageCompletionExercise.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return "ImageCompletionExercise{" +
            "id=" + this.getId() +
            '}';
    }
}
