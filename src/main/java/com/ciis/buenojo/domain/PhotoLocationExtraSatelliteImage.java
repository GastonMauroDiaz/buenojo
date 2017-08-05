package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationExtraSatelliteImage.
 */
@Entity
@Table(name = "photo_location_extra_satellite_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationExtraSatelliteImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)    
    private PhotoLocationSatelliteImage image;

    @OneToOne    
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhotoLocationSatelliteImage getImage() {
        return image;
    }

    public void setImage(PhotoLocationSatelliteImage photoLocationSatelliteImage) {
        this.image = photoLocationSatelliteImage;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationExtraSatelliteImage photoLocationExtraSatelliteImage = (PhotoLocationExtraSatelliteImage) o;

        if ( ! Objects.equals(id, photoLocationExtraSatelliteImage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoLocationExtraSatelliteImage{" +
            "id=" + id +
            '}';
    }
}
