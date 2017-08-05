package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationSatelliteImage.
 */
@Entity
@Table(name = "photo_location_satellite_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationSatelliteImage implements Serializable, IKeywordAnnotated {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.DETACH)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "photo_location_satellite_image_keyword",
               joinColumns = @JoinColumn(name="photo_location_satellite_images_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="keywords_id", referencedColumnName="ID"))
    private Set<PhotoLocationKeyword> keywords = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE)
    private SatelliteImage satelliteImage;
  
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PhotoLocationKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<PhotoLocationKeyword> photoLocationKeywords) {
        this.keywords = photoLocationKeywords;
    }

    public SatelliteImage getSatelliteImage() {
        return satelliteImage;
    }

    public void setSatelliteImage(SatelliteImage satelliteImage) {
        this.satelliteImage = satelliteImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationSatelliteImage photoLocationSatelliteImage = (PhotoLocationSatelliteImage) o;

        if ( ! Objects.equals(id, photoLocationSatelliteImage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoLocationSatelliteImage{" +
            "id=" + id +
            '}';
    }
    
    public static PhotoLocationSatelliteImage fromSatelliteImage(SatelliteImage s) {
    	
    	PhotoLocationSatelliteImage ps = new PhotoLocationSatelliteImage();
    	ps.setSatelliteImage(s);
    	return ps;
    }
}
