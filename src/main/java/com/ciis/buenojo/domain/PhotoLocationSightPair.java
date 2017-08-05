package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationSightPair.
 */
@Entity
@Table(name = "photo_location_sight_pair")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationSightPair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotNull
    @Min(value = 0)
    @Column(name = "satellite_x", nullable = false)
    private Integer satelliteX;

    @NotNull
    @Min(value = 0)
    @Column(name = "satellite_y", nullable = false)
    private Integer satelliteY;

    @NotNull
    @Min(value = 0)
    @Column(name = "satellite_tolerance", nullable = false)
    private Integer satelliteTolerance;

    @NotNull
    @Min(value = 0)
    @Column(name = "terrain_x", nullable = false)
    private Integer terrainX;

    @NotNull
    @Min(value = 0)
    @Column(name = "terrain_y", nullable = false)
    private Integer terrainY;

    @NotNull
    @Min(value = 0)
    @Column(name = "terrain_tolerance", nullable = false)
    private Integer terrainTolerance;

    @ManyToOne  
    private PhotoLocationExercise exercise;

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

    public Integer getSatelliteX() {
        return satelliteX;
    }

    public void setSatelliteX(Integer satelliteX) {
        this.satelliteX = satelliteX;
    }

    public Integer getSatelliteY() {
        return satelliteY;
    }

    public void setSatelliteY(Integer satelliteY) {
        this.satelliteY = satelliteY;
    }

    public Integer getSatelliteTolerance() {
        return satelliteTolerance;
    }

    public void setSatelliteTolerance(Integer satelliteTolerance) {
        this.satelliteTolerance = satelliteTolerance;
    }

    public Integer getTerrainX() {
        return terrainX;
    }

    public void setTerrainX(Integer terrainX) {
        this.terrainX = terrainX;
    }

    public Integer getTerrainY() {
        return terrainY;
    }

    public void setTerrainY(Integer terrainY) {
        this.terrainY = terrainY;
    }

    public Integer getTerrainTolerance() {
        return terrainTolerance;
    }

    public void setTerrainTolerance(Integer terrainTolerance) {
        this.terrainTolerance = terrainTolerance;
    }
    @JsonIgnore
    public PhotoLocationExercise getExercise() {
        return exercise;
    }

    public void setExercise(PhotoLocationExercise photoLocationExercise) {
        this.exercise = photoLocationExercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationSightPair photoLocationSightPair = (PhotoLocationSightPair) o;

        if ( ! Objects.equals(id, photoLocationSightPair.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoLocationSightPair{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", satelliteX='" + satelliteX + "'" +
            ", satelliteY='" + satelliteY + "'" +
            ", satelliteTolerance='" + satelliteTolerance + "'" +
            ", terrainX='" + terrainX + "'" +
            ", terrainY='" + terrainY + "'" +
            ", terrainTolerance='" + terrainTolerance + "'" +
            ", exercise='" + exercise.getId() + "'" +
            '}';
    }
}
