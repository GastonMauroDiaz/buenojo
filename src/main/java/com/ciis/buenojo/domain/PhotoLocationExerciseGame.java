package com.ciis.buenojo.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Transient;

public class PhotoLocationExerciseGame extends PhotoLocationExercise {



    private Set<PhotoLocationSatelliteImage> satelliteSlides = new HashSet<>();

    private Set<PhotoLocationImage> terrainSlides = new HashSet<>();

    public PhotoLocationExerciseGame(PhotoLocationExercise e){
    	super();
    	this.setId(e.getId());
    	this.setBeacon(e.getBeacon());
    	this.setDescription(e.getDescription());
    	this.setDifficulty(e.getDifficulty());
    	this.setExtraPhotosCount(e.getExtraPhotosCount());
    	this.setHigherLevel(e.getHigherLevel());
    	this.setLandscapeKeywords(e.getLandscapeKeywords());
    	this.setCourse(e.getCourse());
    	this.setLowerLevel(e.getLowerLevel());
    	this.setName(e.getName());
    	this.setSatelliteImages(e.getSatelliteImages());
    	this.setSightPairs(e.getSightPairs());
    	this.setTerrainPhoto(e.getTerrainPhoto());
    	this.setTotalScore(e.getTotalScore());
    	this.setTotalTimeInSeconds(e.getTotalTimeInSeconds());

    }

    public Set<PhotoLocationSatelliteImage> getSatelliteSlides() {
		return satelliteSlides;
	}

	public void setSatelliteSlides(Set<PhotoLocationSatelliteImage> satelliteSlides) {
		this.satelliteSlides = satelliteSlides;
	}

	public Set<PhotoLocationImage> getTerrainSlides() {
		return terrainSlides;
	}

	public void setTerrainSlides(Set<PhotoLocationImage> terrainSlides) {
		this.terrainSlides = terrainSlides;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationExerciseGame photoLocationExercise = (PhotoLocationExerciseGame) o;
        if ( ! Objects.equals(super.getId(), photoLocationExercise.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getId());
    }

    @Override
    public String toString() {
        return "PhotoLocationExerciseGame{" +
            "id=" + super.getId() +
            ", totalScore='" + super.getTotalScore() + "'" +
            ", totalTimeInSeconds='" + super.getTotalTimeInSeconds()+ "'" +
            '}';
    }


}
