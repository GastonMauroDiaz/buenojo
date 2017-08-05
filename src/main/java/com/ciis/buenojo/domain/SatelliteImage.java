package com.ciis.buenojo.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

import java.util.Objects;

import com.ciis.buenojo.domain.enumeration.SatelliteImageType;

/**
 * Represents an satellite image resource in the system.
 */
@Entity
@Table(name = "satellite_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SatelliteImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "meters", nullable = false)
    private Double meters;

    @NotNull
    @Column(name = "lon", nullable = false)
    private Double lon;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    @NotNull
    @Column(name = "resolution", nullable = false)
    private Double resolution;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotNull  
    private ImageResource image;
    
    @NotNull
    @Column(name = "copyright", nullable = false)
    private String copyright;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private SatelliteImageType imageType;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMeters() {
        return meters;
    }

    public void setMeters(Double meters) {
        this.meters = meters;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageResource getImage() {
        return image;
    }

    public void setImage(ImageResource image) {
        this.image = image;
    }

   
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public SatelliteImageType getImageType() {
        return imageType;
    }

    public void setImageType(SatelliteImageType imageType) {
        this.imageType = imageType;
    }

   

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SatelliteImage satelliteImage = (SatelliteImage) o;

        if ( ! Objects.equals(id, satelliteImage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SatelliteImage{" +
            "id=" + id +
            ", meters='" + meters + "'" +
            ", lon='" + lon + "'" +
            ", lat='" + lat + "'" +
            ", resolution='" + resolution + "'" +
            ", name='" + name + "'" +
            ", image='" + image + "'" +
            ", copyright='" + copyright + "'" +
            ", imageType='" + imageType + "'" +
            '}';
    }
}
