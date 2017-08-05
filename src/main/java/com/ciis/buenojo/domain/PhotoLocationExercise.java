package com.ciis.buenojo.domain;

import com.ciis.buenojo.domain.enumeration.PhotoLocationDifficulty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationExercise.
 */
@Entity
@Table(name = "photo_location_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationExercise extends Exercise implements Serializable {


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private PhotoLocationDifficulty difficulty;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_time_in_seconds", nullable = false)
    private Integer totalTimeInSeconds;
    @NotNull
    @Min(value = 0)
    @Column(name = "lower_level", nullable = false)
    private Integer lowerLevel;

    @NotNull
    @Min(value = 0)
    @Column(name = "higher_level", nullable = false)
    private Integer higherLevel;
    @NotNull
    @Min(value = 0)
    @Column(name = "extra_photos_count", nullable = false)
    private Integer extraPhotosCount;

    @OneToOne( cascade = CascadeType.REMOVE)
    private PhotoLocationBeacon beacon;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "exercise")  
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PhotoLocationSightPair> sightPairs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "photo_location_exercise_landscape_keyword",
               joinColumns = @JoinColumn(name="photo_location_exercises_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="landscape_keywords_id", referencedColumnName="ID"))
    private Set<PhotoLocationKeyword> landscapeKeywords = new HashSet<>();

    
    @OneToOne(cascade = CascadeType.REMOVE)
    private PhotoLocationImage terrainPhoto;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "photo_location_exercise_satellite_image", 
               joinColumns = @JoinColumn(name="photo_location_exercises_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="satellite_images_id", referencedColumnName="ID"))
    private Set<PhotoLocationSatelliteImage> satelliteImages = new HashSet<>();


    public Integer getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

    public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    public PhotoLocationDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(PhotoLocationDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getLowerLevel() {
		return lowerLevel;
	}

	public void setLowerLevel(Integer lowerLevel) {
		this.lowerLevel = lowerLevel;
	}

	public Integer getHigherLevel() {
		return higherLevel;
	}

	public void setHigherLevel(Integer higherLevel) {
		this.higherLevel = higherLevel;
	}

	public Integer getExtraPhotosCount() {
		return extraPhotosCount;
	}

	public void setExtraPhotosCount(Integer extraPhotosCount) {
		this.extraPhotosCount = extraPhotosCount;
	}

	public PhotoLocationBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(PhotoLocationBeacon photoLocationBeacon) {
        this.beacon = photoLocationBeacon;
    }

    public Set<PhotoLocationSightPair> getSightPairs() {
        return sightPairs;
    }

    public void setSightPairs(Set<PhotoLocationSightPair> photoLocationSightPairs) {
        this.sightPairs = photoLocationSightPairs;
    }

    public Set<PhotoLocationKeyword> getLandscapeKeywords() {
        return landscapeKeywords;
    }

    public void setLandscapeKeywords(Set<PhotoLocationKeyword> photoLocationKeywords) {
        this.landscapeKeywords = photoLocationKeywords;
    }

    public PhotoLocationImage getTerrainPhoto() {
        return terrainPhoto;
    }

    public void setTerrainPhoto(PhotoLocationImage photoLocationImage) {
        this.terrainPhoto = photoLocationImage;
    }

    public Set<PhotoLocationSatelliteImage> getSatelliteImages() {
        return satelliteImages;
    }

    public void setSatelliteImages(Set<PhotoLocationSatelliteImage> photoLocationSatelliteImages) {
        this.satelliteImages = photoLocationSatelliteImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationExercise photoLocationExercise = (PhotoLocationExercise) o;
        if ( ! Objects.equals(super.getId(), photoLocationExercise.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getId());
    }

    @Override
    public String toString() {
        return "PhotoLocationExercise{" +
            "id=" + super.getId() +
            ", totalScore='" + super.getTotalScore() + "'" +
            ", totalTimeInSeconds='" + totalTimeInSeconds + "'" +
            '}';
    }
}
