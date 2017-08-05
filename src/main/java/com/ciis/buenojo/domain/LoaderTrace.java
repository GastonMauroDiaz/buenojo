package com.ciis.buenojo.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

import com.ciis.buenojo.domain.enumeration.LoaderResult;

import com.ciis.buenojo.domain.enumeration.LoaderType;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;

/**
 * A LoaderTrace.
 */
@Entity
@Table(name = "loader_trace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LoaderTrace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Enumerated(EnumType.STRING)
    @Column(name = "loader_result", nullable = false)
    private LoaderResult loaderResult;

    @NotNull        
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull        
    @Enumerated(EnumType.STRING)
    @Column(name = "loader_type", nullable = false)
    private LoaderType loaderType;

    @NotNull        
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;
    
    @Column(name = "result_log")
    private String resultLog;

    @NotNull        
    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    @OneToMany(mappedBy = "loaderTrace")
    private Set<LoadedExercise> loadedExercises;
    public Set<LoadedExercise> getLoadedExercises() {
		return loadedExercises;
	}

	public void setLoadedExercises(Set<LoadedExercise> loadedExercises) {
		this.loadedExercises = loadedExercises;
	}

	public LoaderTrace() {
    	super();
    }
    
    public LoaderTrace(ExerciseDataSetDTO dto){
    	super();
    	this.datasetName = dto.getName();
    	this.date = ZonedDateTime.now();
    	this.loaderType = LoaderType.fromExerciseType(dto.getType());
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoaderResult getLoaderResult() {
        return loaderResult;
    }

    public void setLoaderResult(LoaderResult loaderResult) {
        this.loaderResult = loaderResult;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LoaderType getLoaderType() {
        return loaderType;
    }

    public void setLoaderType(LoaderType loaderType) {
        this.loaderType = loaderType;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getResultLog() {
        return resultLog;
    }

    public void setResultLog(String resultLog) {
        this.resultLog = resultLog;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoaderTrace loaderTrace = (LoaderTrace) o;

        if ( ! Objects.equals(id, loaderTrace.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LoaderTrace{" +
                "id=" + id +
                ", loaderResult='" + loaderResult + "'" +
                ", author='" + author + "'" +
                ", loaderType='" + loaderType + "'" +
                ", date='" + date + "'" +
                ", resultLog='" + resultLog + "'" +
                ", datasetName='" + datasetName + "'" +
                '}';
    }
}
